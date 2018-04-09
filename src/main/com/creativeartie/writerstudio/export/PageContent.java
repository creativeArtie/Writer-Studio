package com.creativeartie.writerstudio.export;

import java.util.*; // Optional
import java.io.*; //AutoCloseable, IOException

import org.apache.pdfbox.pdmodel.*; // PDPage, PDPageContentStream

import com.creativeartie.writerstudio.export.value.*; // PageAlignment, PageMargin, Utilities
import com.creativeartie.writerstudio.main.*; // Checker

/** Render a single page in the document.
 * Purpose:
 * <ul>
 * <li>Render a page in a document</li>
 * <li>Create the neccesary class for rendering</li>
 * <li>Calculate the starting part for a {@link MatterArea}</li>
 * </ul>
 */
public final class PageContent implements AutoCloseable{

    private final PDPage contentPage;
    private final PDPageContentStream contentStream;
    private final PageMargin pageMargin;
    private Optional<MatterArea> pageHeader;

    /** Only constructor.
     * @param section
     *      the section that this page belong to,
     */
    PageContent(Section section) throws IOException{
        Checker.checkNotNull(section, "section");
        PDDocument doc = section.getPdfDocument();
        ///contentPage
        contentPage = new PDPage();
        doc.addPage(contentPage);
        /// others
        contentStream = new PDPageContentStream(doc, contentPage);
        pageMargin = new PageMargin(Utilities.cmToPoint(3f));
        pageHeader = Optional.empty();
    }

    /** Gets the page that is being render on
     * @return answer
     */
    public PDPage getPage(){
        return contentPage;
    }

    /** Gets the output stream for the page
     * @return answer
     */
    public PDPageContentStream getContentStream(){
        return contentStream;
    }

    /** Gets the rendering width
     * @return answer
     * @see #getWidth()
     * @see #getRenderHeight(PageAlignment)
     * @see #getHeight()
     */
    public float getRenderWidth(){
        return getWidth() - pageMargin.getLeft() - pageMargin.getRight();
    }

    /** Gets the page width
     * @return answer
     * @see #getRenderWidth()
     * @see #getRenderHeight(PageAlignment)
     * @see #getHeight()
     */
    public float getWidth(){
        return contentPage.getMediaBox().getWidth();
    }

    /** Gets the rendering height
     * @param alignment
     *      where to put the content
     * @return answer
     * @see #getWidth()
     * @see #getRenderWidth()
     * @see #getHeight()
     */
    public float getRenderHeight(PageAlignment alignment){
        float base = getHeight() - pageMargin.getTop() - pageMargin.getBottom();
        switch (alignment){
        case CONTENT:
            return base - pageHeader.map(h -> h.getHeight()).orElse(0f);
        case THIRD:
            return base / 3 * 2;
        default:
            return base;
        }
    }

    /** Gets the page height
     * @return answer
     * @see #getWidth()
     * @see #getRenderWidth()
     * @see #getRenderHeight(PageAlignment)
     */
    public float getHeight(){
        return contentPage.getMediaBox().getHeight();
    }

    /** Set the header to assists {@link #getRendeingHeight(PageHeight}.
     * @param header
     *      the value
     * @return self
     */
    public PageContent setHeader(MatterArea header){
        pageHeader = Optional.ofNullable(header);
        return this;
    }

    /** Get the starting x.
     * @return answer
     */
    public float getStartX(){
        return pageMargin.getLeft();
    }

    /** Get the starting y.
     * @param area
     *      the area to render
     * @return answer
     */
    public float getStartY(MatterArea area){
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

    @Override
    public void close() throws IOException{
        contentStream.close();
    }
}