package com.creativeartie.writerstudio.export;

import java.awt.*; // Color
import java.io.*; // IOException
import java.util.*; // Optional

import com.creativeartie.writerstudio.export.value.*; // ContentFont
import com.creativeartie.writerstudio.lang.*; // Span, SpanBranch
import com.creativeartie.writerstudio.lang.markup.*; // (many)
import com.creativeartie.writerstudio.main.*; // Checker

/**
 * A {@link Division} of {@link FormatSpanMain}
 */
class DivisionTextFormatted extends DivisionText{

    private final SectionContent<?> contentData;
    private final WritingExporter parentDoc;

    /** Only constructor
     * @param content
     *      the parent content
     */
    DivisionTextFormatted(SectionContent<?> content){
        super(Checker.checkNotNull(content, "content").getPage()
            .getRenderWidth());
        contentData = content;
        parentDoc = content.getParent();
    }

    /** Add Content
     * @param span
     *      the content to add
     * @return self
     * @throws IOException
     *         exception with content parsing
     */
    DivisionTextFormatted addContent(FormatSpanMain span) throws IOException{
        Checker.checkNotEmpty(span, "span");
        for(Span child: span){
            if (child instanceof FormatSpan){
                FormatSpan format = (FormatSpan) child;
                ContentFont font = addFont(format);
                parseContent(format, font);
            }
        }
        return this;
    }

    /** Create the font of a {@link FormatSpan}.
     *
     * It does not work with the subclass of {@linkplain FormatSpan}
     * @param span
     *      the span to extract the font
     * @see addContent(FormatSpanMain)
     */
    private ContentFont addFont(FormatSpan span){
        assert span != null;
        ContentFont font = parentDoc.new PdfFont();
        if (span.isCoded()){
            font = font.changeToMono();
        }
        font = font.changeBold(span.isBold());
        font = font.changeItalics(span.isItalics());
        font = font.changeUnderline(span.isUnderline());
        return font;
    }

    /** Sort and parse the {@link FormatSpan}.
     *
     * @param span
     *      the span to sort
     * @param font
     *      the font of the span
     * @throws IOException
     *         exception with content parsing
     * @see addContent(FormatSpanMain)
     */
    private void parseContent(FormatSpan span, ContentFont font)
            throws IOException{
        if (span instanceof FormatSpanContent){
            parseContent((FormatSpanContent) span, font);
        } else if (span instanceof FormatSpanLink){
            parseContent((FormatSpanLink) span, font);
        } else if (span instanceof FormatSpanDirectory){
            parseContent((FormatSpanDirectory) span, font);
        }
    }

    /** Parse a {@link FormatSpanContent}.
     *
     * @param span
     *      the span to parse
     * @param font
     *      the font of the span
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpan, ContentFont)
     */
    private void parseContent(FormatSpanContent span, ContentFont font)
            throws IOException{
        String text = span.getText();
        appendText(text, font);
    }

    /** Parse a {@link FormatSpanLink}.
     *
     * @param span
     *      the span to parse
     * @param font
     *      the font of the span
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpan, ContentFont)
     */
    private void parseContent(FormatSpanLink span, ContentFont font)
            throws IOException{
        /// Edit the font
        font = font.changeFontColor(Color.BLUE);

        /// Get the text and setup for path
        String text = span.getText();
        Optional<String> path = Optional.empty();

        /// When the span is a FormatSpanLinkDirect
        if (span instanceof FormatSpanLinkDirect){
            path = Optional.of(((FormatSpanLinkDirect)span).getPath());

        /// When the span is a FormatSpanLinkRef
        } else if (span instanceof FormatSpanLinkRef){
            path = ((FormatSpanLinkRef) span).getPathSpan()
                /// f = SpanBranch
                .filter(f -> f instanceof LinedSpanPointLink)
                .map(s -> (LinedSpanPointLink) s)
                /// s = LinedSpanPointLink
                .map(s -> s.getPath());
        }

        /// Add the path
        if (path.isPresent()){
            for (ContentText content: appendTextList(text, font)) {
                content.setLinkPath(path.get());
            }
        } else {
            appendText(text, font);
        }
    }

    /** Parse a {@link FormatSpanDirectory}.
     *
     * @param span
     *      the span to parse
     * @param font
     *      the font of the span
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpan, ContentFont)
     */
    private void parseContent(FormatSpanDirectory span, ContentFont font)
            throws IOException{
        /// This is not a note
        if (span.getIdType() != DirectoryType.NOTE){
            font = font.changeToSuperscript();
        }

        /// get the target
        Optional<SpanBranch> base = span.getTarget();
        Optional<LinedSpanPointNote> note = base /// separated for NoteCard
            .filter(t -> t instanceof LinedSpanPointNote)
            .map(t -> (LinedSpanPointNote) t);

        /// For endnote
        Optional<String> text = note
            .filter(t -> t.getDirectoryType() == DirectoryType.ENDNOTE)
            .map(t -> parentDoc.addEndnote(t));
        if (text.isPresent()){
            appendText(text.get(), font);
            return;
        }

        /// For footnote
        note = note.filter(t -> t.getDirectoryType() == DirectoryType.FOOTNOTE);
        if (note.isPresent()){
            addFootnote(note.get(), font);
            return;
        }

        /// Fot note card
        Optional<LinedSpanCite> citation = base
            .filter(s -> s instanceof NoteCardSpan)
            .map(s -> (NoteCardSpan) s)
            /// s = NoteCardSpan
            .flatMap(s -> parentDoc.addCitation(s));
        if (citation.isPresent()){
            LinedSpanCite cite = citation.get();

            /// Add in text citation as footnote
            if (cite.getFieldType() == InfoFieldType.FOOTNOTE){
                font = font.changeToSuperscript();
                addFootnote(cite, font);

            /// Add in text citation into the main text
            } else {
                appendText(cite.getData()
                    /// s = InfoDataSpan
                    .filter(s -> s instanceof InfoDataSpanText)
                    .map(s -> ((InfoDataSpanText)s).getData())
                    /// s = ContentSpan
                    .map(s -> s.getText()).orElse("")
                , font);

            }
        }
    }

    /** Add span as footnote
     * @param span
     *      the span to parse
     * @param font
     *      the font of the span
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpanDirectory, ContentFont)
     */
    private void addFootnote(SpanBranch span, ContentFont font)
        throws IOException{
        for (ContentText content: appendTextList(
            contentData.getFootnote().addFootnote(span), font
        )){
            content.setFootnote(Optional.of(span));
        }
    }

}