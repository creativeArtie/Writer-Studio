package com.creativeartie.writerstudio.export;

import java.util.*; // Optional
import java.io.*; //AutoCloseable, IOException

import org.apache.pdfbox.pdmodel.*; // PDPage, PDPageContentStream

import com.creativeartie.writerstudio.export.value.*; // PageAlignment, PageMargin, Utilities

import static com.creativeartie.writerstudio.main.Checker.*;

/** Render a single page in the document.
 *
 * Purpose:
 * <ul>
 * <li>Render a page in a document</li>
 * <li>Create the neccesary class for rendering</li>
 * <li>Calculate the starting part for a {@link MatterArea}</li>
 * </ul>
 */
final class PageContent implements AutoCloseable{

    private final PDPage contentPage;
    private final PDPageContentStream contentStream;

    private final PageMargin pageMargin;
    private Optional<MatterArea> pageHeader;

    /** Only constructor.
     * @param section
     *      the section that this page belong to
     * @throws IOException
     *         exception with page creation
     */
    PageContent(Section section) throws IOException{
        checkNotNull(section, "section");
        PDDocument doc = section.getPdfDocument();

        contentPage = new PDPage();
        doc.addPage(contentPage);

        contentStream = new PDPageContentStream(doc, contentPage);

        pageMargin = new PageMargin(Utilities.cmToPoint(3f));
        pageHeader = Optional.empty();
    }

    /** Gets the page that is being render on
     * @return answer
     * @see #getContentStream()
     */
    PDPage getPage(){
        return contentPage;
    }

    /** Gets the output stream for the page
     * @return answer
     * @see #getPage()
     */
    PDPageContentStream getContentStream(){
        return contentStream;
    }

    /** Get the starting y.
     * @param area
     *      the area to render
     * @return answer
     * @see #getHeight()
     * @see #getRenderHeight()
     */
    float getStartY(MatterArea area){
        float height = contentPage.getMediaBox().getHeight();
        switch (area.getPageAlignment()){
        case TOP:
            return height - pageMargin.getTop();
        case MIDDLE:
            height /= 2;
            for (Division line: area){
                height += line.getHeight() / 2;
            }
            return height;
        case BOTTOM:
            height = pageMargin.getBottom();
            for (Division line : area){
                height += line.getHeight();
            }
            return height;
        case CONTENT:
            return height - pageMargin.getTop() -
                pageHeader.map(h -> h.getHeight()).orElse(0f);
        case THIRD:
            return height - ( height / 3);
        }
        // default:
        return 0f;
    }

    /** Gets the page height
     * @return answer
     * @see #getStartY()
     * @see #getRenderHeight(PageAlignment)
     */
    float getHeight(){
        return contentPage.getMediaBox().getHeight();
    }

    /** Gets the rendering height
     * @param alignment
     *      where to put the content
     * @return answer
     * @see #getHeight()
     * @see #getStartY()
     */
    float getRenderHeight(PageAlignment alignment){
        float base = getHeight() - pageMargin.getTop() - pageMargin.getBottom();
        switch (alignment){
        case CONTENT:
            // TODO somehow pageHeader didn't reduce the height properly
            // work around had been made in SectionContent#footnoteHeight
            return base - pageHeader.map(h -> h.getHeight()).orElse(0f);
        case THIRD:
            return base / 3 * 2;
        default:
            return base;
        }
    }

    /** Get the starting x.
     * @return answer
     * @see #getWidth()
     * @see #getRenderWidth()
     */
    float getStartX(){
        return pageMargin.getLeft();
    }

    /** Gets the page width
     * @return answer
     * @see #getStartX()
     * @see #getRenderWidth()
     */
    float getWidth(){
        return contentPage.getMediaBox().getWidth();
    }

    /** Gets the rendering width
     * @return answer
     * @see #getStartX()
     * @see #getWidth()
     */
    float getRenderWidth(){
        return getWidth() - pageMargin.getLeft() - pageMargin.getRight();
    }

    /** Gets the page margin
     * @return answer
     */
    PageMargin getMargin(){
        return pageMargin;
    }

    /** Set the header to assists {@link #getRendeingHeight(PageHeight}.
     * @param header
     *      the value
     * @return self
     */
    PageContent setHeader(MatterArea header){
        pageHeader = Optional.ofNullable(header);
        return this;
    }

    @Override
    public void close() throws IOException{
        contentStream.close();
    }
}