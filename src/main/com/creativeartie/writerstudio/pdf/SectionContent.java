package com.creativeartie.writerstudio.pdf;

import java.io.*; // IOException
import java.util.*; // List

import com.creativeartie.writerstudio.pdf.value.*; // PageAlignment
import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.*; // SpanBranch
import com.creativeartie.writerstudio.lang.markup.*; // SpanBranch, TextDataSpanPrint

import static com.creativeartie.writerstudio.main.Checker.*;

/** A {@link Section} with mulitple pages.
 *
 * A page can also contain footnotes and a header
 *
 * @param T
 *      rending {@link SpanBranch} class.
 */
abstract class SectionContent<T extends SpanBranch> extends Section {
    private ManuscriptFile outputData;
    private PageContent currentPage;
    private int pageNumber;

    private MatterArea contentArea;

    private PageFootnote pageFootnote;
    private float footnoteHeight;

    /** Only constructor.
     *
     * @param parent
     *      input parent data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     */
    SectionContent(WritingExporter parent) throws IOException{
        super(parent);
        currentPage = new PageContent(this);
        pageNumber = 1;

        contentArea = null;

        pageFootnote = new PageFootnote(this);
        footnoteHeight = 0f;
    }

    /** Go to the next page
     *
     * @param alignment
     *      content pag alignment
     * @throws IOException
     *      exceptions thrown from uses of other classes
     */
    void nextPage(PageAlignment alignment) throws IOException{
        /// if the contentArea is null, then it is the new page
        if (contentArea == null){
            contentArea = new MatterArea(currentPage, alignment);
            if (alignment == PageAlignment.CONTENT) {
                contentArea.reduceHeight(footnoteHeight);
            }
            return;
        }
        /// System.out.println("Next Page"); // More dubug code printing

        /// render and close the current page
        contentArea.render();
        pageFootnote.nextPage().render();
        currentPage.close();

        pageNumber++;

        /// create the next page
        currentPage = new PageContent(this);
        addHeader();
        contentArea = new MatterArea(currentPage, alignment);

        /// change contentArea, as needed
        if (alignment == PageAlignment.CONTENT) {
            contentArea.reduceHeight(footnoteHeight);
        }
    }

    /** Adds a header for all content pages.
     *
     * @param data
     *      the input data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #parseHeader(ManuscriptData)
     */
    final void addHeader(ManuscriptFile data) throws IOException{
        outputData = data;
        addHeader();
    }

    /** Renders the header on the page
     *
     * @param data
     *      the input data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #addHeader(ManuscriptFile)
     * @see #nextPage(PageAlignment)
     */
    private final void addHeader() throws IOException{
        MatterArea header = parseHeader(outputData);
        if (header != null){
            currentPage.setHeader(header);
            header.render();
            footnoteHeight = header.getHeight();
        }
    }

    /** Created the {@link MatterArea} for the header provided from
     * {@link #addHeader(ManuscriptFile)}.
     *
     * @param data
     *      the input data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #addHeader(ManuscriptFile)
     */
    protected abstract MatterArea parseHeader(ManuscriptFile data)
        throws IOException;


    /** Add a line of text from a span.
     *
     * @param span
     *      the span to add
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #parseSpan(T)
     * @see #addLine(DivisionText)
     */
    void addLine(T span) throws IOException{
        DivisionText found = parseSpan(span);
        if (found != null){
            addLine(found);
        }
    }

    /** Parse a line of text from a {@link Span}.
     *
     * The span is provided by {@link #addLine(T)}
     *
     * @param span
     *      the span to add
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #addLine(T)
     */
    protected abstract DivisionText parseSpan(T span) throws IOException;

    void addLines(List<TextDataSpanPrint> lines) throws IOException{
        for (DivisionTextFormatted line: DivisionTextFormatted.newPrintLines(
                this, lines)){
            addLine(line);
        }
    }

    /** Adds a line of text.
     *
     * @param div
     *      the line to add
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #addLine(T)
     */
    void addLine(DivisionText div) throws IOException{
        /// create the contentArea as needed
        if (contentArea == null){
            nextPage(PageAlignment.CONTENT);
        }

        /// setup for adding line
        DivisionText allows = DivisionText.copyFormat(div);
        DivisionText checker = DivisionText.copyFormat(div);
        DivisionText overflow = null;
        DivisionText.Line last = null;
        for (DivisionText.Line line: div){
            float footnote = pageFootnote.getHeight(line);
            checker.addLine(line);
            /* /// debug code by printing the sizes
            System.out.println(line);
            float tm = getPage().getMargin().getTop();
            float hA = getPage().getHeader().map(s -> s.getHeight()).orElse(0f);
            float cA = contentArea.getHeight();
            float fA = footnote;
            float bm = getPage().getMargin().getBottom();
            float ac = tm + hA + cA + fA + bm;
            float h = getPage().getHeight();
            ///                 123456   123456   123456   123456 + 123456
            System.out.println("   Top +   Head +   cont. +  foot + bottom");
            System.out.printf("%6.2f + %6.2f + %6.2f + %6.2f + %6.2f ", tm, hA, cA, fA, bm);
            System.out.printf("= %6.2f < %6.2f\n", ac, h);
            */
            if (contentArea.checkHeight(checker, footnote)){
                /// content + footnote fits size
                if (last != null){
                    allows.addLine(last);
                    pageFootnote.insertPending(last);
                }
                last = line;
            } else {
                if (overflow == null){
                    if(allows.isEmpty()){
                        /// nothing fits
                        nextPage(PageAlignment.CONTENT);
                        addLine(div);
                        return;
                    }
                    /// partial fits
                    overflow = DivisionText.splitItem(div);
                }

                /// change the footnote number
                for (ContentText content: overflow.addLine(line)){
                    pageFootnote.resetFootnote(content);
                }
            }
        }
        if (last != null){
            allows.addLine(last);
            pageFootnote.insertPending(last);
        }

        /// add the allowed content
        if (! allows.isEmpty()){
            contentArea.add(allows);
        }
        /*
        float tm = getPage().getMargin().getTop();
        float hA = getPage().getHeader().map(s -> s.getHeight()).orElse(0f);
        float cA = contentArea.getHeight();
        float fA = pageFootnote.getHeight();
        float bm = getPage().getMargin().getBottom();
        float ac = tm + hA + cA + fA + bm;
        float h = getPage().getHeight();
        System.out.printf("%6.2f + %6.2f + %6.2f + %6.2f + %6.2f ", tm, hA, cA, fA, bm);
        System.out.printf("= %6.2f < %6.2f\n", ac, h);
        */
        /// recursive call for the overflow content
        if (overflow != null && ! overflow.isEmpty()){
            nextPage(PageAlignment.CONTENT);
            addLine(overflow);
        }
    }

    /** get the output data
     * @return answer
     */
    protected final ManuscriptFile getOutputData(){
        return outputData;
    }

    /** get the rendering page
     * @return answer
     */
    final PageContent getPage(){
        return currentPage;
    }

    /** get the content page number
     * @return answer
     */
    final int getPageNumber(){
        return pageNumber;
    }

    /** get the current page footnote
     * @return answer
     */
    final PageFootnote getFootnote(){
        return pageFootnote;
    }


    /** creates a new division text line
     *
     * It is the same as {@code new DeivisionTextFormatted(this)}.
     * @return answer
     */
    protected final DivisionTextFormatted newFormatDivision(){
        return new DivisionTextFormatted(this);
    }

    @Override
    public final void close() throws IOException{
        if (contentArea != null) contentArea.render();
        pageFootnote.nextPage().render();
        currentPage.close();
    }
}