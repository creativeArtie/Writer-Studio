package com.creativeartie.writerstudio.pdf;

import java.awt.*; // Color
import java.io.*; // IOException
import java.util.*; // ArrayList Optional
import java.util.List;
import java.util.function.*; // Supplier

import com.creativeartie.writerstudio.pdf.value.*; // ContentFont
import com.creativeartie.writerstudio.lang.*; // Span, SpanBranch
import com.creativeartie.writerstudio.lang.markup.*; // (many)

import static com.creativeartie.writerstudio.main.Checker.*;

/** A {@link Division} of {@link FormattedSpan}.
 */
class DivisionTextFormatted extends DivisionText{

    /** Create new lines with a list of {@link TextDataSpanPrint} with a
     * {@link SectionContennt}.
     * @param section
     *      for section
     * @param spans
     *      adding spans; not null or empty
     * @return self
     * @see newPrintLines(float, writing, List)
     */
    static List<DivisionTextFormatted> newPrintLines(SectionContent<?> section,
            List<TextDataSpanPrint> spans) throws IOException{
        checkNotNull(section, "section");
        checkNotEmpty(spans, "spans");
        return newPrintLines(() -> new DivisionTextFormatted(section), spans);
    }

    /** Create new lines with a list of {@link TextDataSpanPrint} without a
     * {@link SectionContent)
     * @param width
     *      page width
     * @param writing
     *      export writing; not null
     * @param spans
     *      adding spans; not null or empty
     * @return self
     * @see newPrintLines(SectionContent, List)
     */
    static List<DivisionTextFormatted> newPrintLines(float width,
            WritingExporter writing, List<TextDataSpanPrint> spans)
            throws IOException{
        checkNotNull(writing, "writing");
        checkNotEmpty(spans, "spans");
        return newPrintLines(() -> new DivisionTextFormatted(width, writing),
            spans);
    }

    /** Create new lines with a list of {@link TextDataSpanPrint}
     * @param width
     *      page width
     * @param writing
     *      export writing; not null
     * @param spans
     *      adding spans; not null or empty
     * @return self
     * @see newPrintLines(SectionContent, List)
     * @see newPrintLines(float, writing, List)
     */
    private static final List<DivisionTextFormatted> newPrintLines(
            Supplier<DivisionTextFormatted> supplier,
            List<TextDataSpanPrint> spans) throws IOException{
        checkNotNull(supplier, "supplier");
        checkNotEmpty(spans, "spans");

        /// Setup list and create first line
        ArrayList<DivisionTextFormatted> ans = new ArrayList<>();
        DivisionTextFormatted line = supplier.get();
        float leading = 1f;
        line.setLeading(1f);

        for (TextDataSpanPrint span: spans){

            /// set LineAlignment
            switch (span.getFormat()){
            case RIGHT:
                line.setLineAlignment(LineAlignment.RIGHT);
                break;
            case CENTER:
                line.setLineAlignment(LineAlignment.CENTER);
                break;
            case LEFT:
            default:
            }

            Optional<FormattedSpan> text = span.getData()
                .filter(t -> t.getPublishTotal() > 0);

            /// append text
            if (text.isPresent()){
                ans.add(line.addContent(text.get()));
                line = supplier.get();
                leading = 1f;
                line.setLeading(leading);
            } else {
                leading += 1f;
                line.setLeading(leading);
            }
        }
        return ans;
    }

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


    /** Add Content with {@link FormattedSpan}
     * @param span
     *      the content to add; not null or empty
     * @return self
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
     * @see parseContent(FormatSpan, ContentFont)
     */
    private final void parseContent(FormatSpanContent span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

        String text = span.getRendered();
        appendText(text, font);
    }

    /** Parse a {@link FormatSpanLink}.
     *
     * @param span
     *      the span to parse; not null
     * @param font
     *      the font of the span; not null
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
     * @see parseContent(FormatSpan, ContentFont)
     */
    private final void parseContent(FormatSpanPointId span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

        /// This is not a note
        if (span.getIdType() != DirectoryType.RESEARCH){
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
                    .map(s -> s.getRendered()).orElse("")
                , font);

            }
        }
    }

    /** Add span as footnote.
     * @param span
     *      the span to parse; not null
     * @param font
     *      the font of the span; not null
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
     * @see parseContent(FormatSpanPointId, ContentFont)
     */
    private final void parseContent(FormatSpanPointKey span, ContentFont font)
            throws IOException{
        assert span != null: "null span";
        assert font != null: "null font";

        FieldType type = FieldType.findField(span.getField());
        if (type == null){
            appendText(" ", font);
        }

        switch (type){
        case PAGE_NUMBER:
            if(contentData.isPresent()){
                appendText(contentData.get().getPageNumber() + "", font);
            } else {
                appendText("1", font);
            }
            break;
        case WORD_COUNT:
            int count = parentDoc.getDataFile().getDocument().getPublishTotal();
            if (count < 999){
                appendText("< 1000", font);
            } else {
                appendText(Utilities.round(count), font);
            }
        }
    }
}