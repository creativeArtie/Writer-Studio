package com.creativeartie.writerstudio.export;

import java.awt.*; // Color
import java.io.*; // IOException
import java.util.*; // Optional

import com.creativeartie.writerstudio.export.value.*; // ContentFont
import com.creativeartie.writerstudio.file.*; // FieldType
import com.creativeartie.writerstudio.lang.*; // Span, SpanBranch
import com.creativeartie.writerstudio.lang.markup.*; // (many)

import static com.creativeartie.writerstudio.main.Checker.*;

/** A {@link Division} of {@link FormattedSpan}.
 */
class DivisionTextFormatted extends DivisionText{

    private final Optional<SectionContent<?>> contentData;
    private final WritingExporter parentDoc;

    /** Constructor without a {@link SectionContent}.
     *
     * This will not add footnote
     *
     * @param width
     *      rendering width
     */
    DivisionTextFormatted(float width, WritingExporter parent){
        super(width);
        checkNotNull(parent, "parent");
        contentData = Optional.empty();
        parentDoc = parent;
    }

    /** Constructor with a {@link SectionContent}.
     *
     * This is required to add footnotes.
     * @param content
     *      the parent content; not null
     */
    DivisionTextFormatted(SectionContent<?> content){
        super(checkNotNull(content, "content").getPage().getRenderWidth());
        contentData = Optional.of(content);
        parentDoc = content.getParent();
    }


    /** Add Content with {@link TextDataSpanPrint}
     * @param span
     *      the content to add; not null or empty
     * @return self
     * @throws IOException
     *         exception with content parsing
     * @see addContent(FormattedSpan)
     */
    final DivisionTextFormatted addContent(TextDataSpanPrint span)
            throws IOException{
        checkNotEmpty(span, "span");

        setLeading(1f);

        // set LineAlignment
        switch (span.getFormat()){
        case RIGHT:
            setLineAlignment(LineAlignment.RIGHT);
            break;
        case CENTER:
            setLineAlignment(LineAlignment.CENTER);
            break;
        case LEFT:
        default:
        }

        // append text
        if (span.getData().isPresent()){
            addContent(span.getData().get());
        } else {
            appendText(" ", parentDoc.new PdfFont());
        }
        return this;
    }

    /** Add Content with {@link FormattedSpan}
     * @param span
     *      the content to add; not null or empty
     * @return self
     * @throws IOException
     *         exception with content parsing
     * @see addContent(TextDataSpanPrint)
     */
    final DivisionTextFormatted addContent(FormattedSpan span)
            throws IOException{
        checkNotEmpty(span, "span");

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
     *      the span to extract the font; not null
     * @see addContent(FormattedSpan)
     */
    private final ContentFont addFont(FormatSpan span){
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
     *      the span to sort; not null
     * @param font
     *      the font of the span; not null
     * @throws IOException
     *         exception with content parsing
     * @see addContent(FormattedSpan)
     */
    private final void parseContent(FormatSpan span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

        if (span instanceof FormatSpanContent){
            parseContent((FormatSpanContent) span, font);
        } else if (span instanceof FormatSpanLink){
            parseContent((FormatSpanLink) span, font);
        } else if (span instanceof FormatSpanPointId){
            parseContent((FormatSpanPointId) span, font);
        } else if (span instanceof FormatSpanPointKey){
            parseContent((FormatSpanPointKey) span, font);
        }
    }

    /** Parse a {@link FormatSpanContent}.
     *
     * @param span
     *      the span to parse; not null
     * @param font
     *      the font of the span; not null
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpan, ContentFont)
     */
    private final void parseContent(FormatSpanContent span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

        String text = span.getText();
        appendText(text, font);
    }

    /** Parse a {@link FormatSpanLink}.
     *
     * @param span
     *      the span to parse; not null
     * @param font
     *      the font of the span; not null
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpan, ContentFont)
     */
    private final void parseContent(FormatSpanLink span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";
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
            for (ContentText content: appendText(text, font)) {
                content.setLinkPath(path.get());
            }
        } else {
            appendText(text, font);
        }
    }

    /** Parse a {@link FormatSpanPointId}.
     *
     * @param span
     *      the span to parse; not null
     * @param font
     *      the font of the span; not null
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpan, ContentFont)
     */
    private final void parseContent(FormatSpanPointId span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

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

    /** Add span as footnote.
     * @param span
     *      the span to parse; not null
     * @param font
     *      the font of the span; not null
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpanPointId, ContentFont)
     */
    private final void addFootnote(SpanBranch span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

        if (contentData.isPresent()){
            SectionContent<?> page = contentData.get();
            for (ContentText content: appendText(
                page.getFootnote().addFootnote(span), font
            )){
                content.setFootnote(Optional.of(span));
            }
        }
    }

    /** Add span as field text.
     * @param span
     *      the span to parse; not null
     * @param font
     *      the font of the span; not null
     * @throws IOException
     *         exception with content parsing
     * @see parseContent(FormatSpanPointId, ContentFont)
     */
    private final void parseContent(FormatSpanPointKey span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

        switch (FieldType.findField(span.getField())){
        case PAGE_NUMBER:
            if(contentData.isPresent()){
                appendText(contentData.get().getPageNumber() + "", font);
            } else {
                appendText("1", font);
            }
            break;
        case WORD_COUNT:
            int count = ((WritingText)span.getDocument()).getPublishTotal();
            if (count < 999){
                appendText("< 1000", font);
            } else {
                appendText(Utilities.round(count), font);
            }
        }
    }
}