package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // ArrayList, Collection, List

import com.google.common.collect.*; // FowardingList

import org.apache.pdfbox.pdmodel.*; //PDPageContentStream
import org.apache.pdfbox.pdmodel.common.*; //PDRectangle

import com.creativeartie.writerstudio.export.value.*; // (many)
import com.creativeartie.writerstudio.main.*; // Checker

/** Insert text and graphics into a section of a page.
 * Purpose
 * <ul>
 * <li> Render text</li>
 * <li> Calculate height</li>
 * <li> Decide when to render other things</li>
 * <li> Calculate rectangle for text and line positions</li>
 * <li> Stores the list of line to print (as an List object). </li>
 * </ul>
 */
final class MatterArea extends ForwardingList<Division> {
    private final PageContent outputPage;
    private final PDPageContentStream contentStream;
    private final PageAlignment pageAlignment;
    private float maxHeight;
    private final float areaWidth;

    private final ArrayList<Division> divisionLines;
    private float fillHeight;
    private float localX;
    private float localY;
    private ContentFont textFont;
    private LineAlignment lineAlignment;
    private final ArrayList<ContentPostEditor> postEditors;

    /** Only constructor.
     * @param page
     *      the page where this section is location; not null
     * @param alignment
     *      the alignment on the page; not null
     */
    MatterArea(PageContent page, PageAlignment alignment){
        outputPage = Checker.checkNotNull(page, "page");
        contentStream = page.getContentStream();
        pageAlignment = Checker.checkNotNull(alignment, "alignment");
        maxHeight = page.getRenderHeight(alignment);
        areaWidth = page.getRenderWidth();

        divisionLines = new ArrayList<>();
        fillHeight = localX = localY = 0;
        textFont = null;
        lineAlignment = LineAlignment.LEFT;
        postEditors = new ArrayList<>();
    }

    /** Reduce the height.
     *
     * Negative number will increase height instead.
     *
     * @param height
     *      subtracting height
     * @return self
     */
    MatterArea reduceHeight(float height){
        maxHeight -= height;
        return this;
    }

    /** Gets the page alignment
     * @return answer
     */
    PageAlignment getPageAlignment(){
        return pageAlignment;
    }

    /** Gets the height that this section needs.
     * @return answer
     * @see checkHeight(DivisionText)
     * @see chechHeight(DivisionText, float)
     */
    float getHeight(){
        return fillHeight;
    }

    /** Check the height that this section + this item needs
     * @return answer
     * @see checkHeight()
     * @see chechHeight(DivisionText, float)
     */
    boolean checkHeight(DivisionText item){
        Checker.checkNotNull(item, "item");
        return item.getHeight() + fillHeight < maxHeight;
    }

    /** Check the height that this section + this item needs, along with the
     * footnote height.
     * @return answer
     * @see checkHeight()
     * @see chechHeight(DivisionText)
     */
    boolean checkHeight(DivisionText item, float footnote){
        Checker.checkNotNull(item, "item");
        return item.getHeight() + footnote + fillHeight < maxHeight;
    }

    /** Render the text and graphics.
     * @throws IOException
     *         exception with content rendering
     */
    MatterArea render() throws IOException{
        contentStream.beginText();

        /// Initital placement
        float leading = 0;
        float x = outputPage.getStartX();
        if (! isEmpty()){
            leading = get(0).getStartY();
        }
        float y = outputPage.getStartY(this) - leading;
        moveText(x, y);

        /// show text and graphics
        for (Division block: this){
            x = localX;
            y = localY;
            if (block instanceof DivisionText){
                render((DivisionText) block);
            }

            /// create text related graphics
            PDRectangle rect = new PDRectangle(x, y,
                block.getWidth(), block.getHeight());
            postEditors.addAll(block.getPostTextConsumers(rect));
        }
        contentStream.endText();

        /// render all non-text graphics
        for (ContentPostEditor consumer: postEditors){
            consumer.edit(outputPage.getPage(), contentStream);
        }
        return this;
    }


    /** Render the text and graphics.
     * @param block
     *      the text to render; not null
     * @throws IOException
     *      exception with content rendering
     * @see #render
     */
    private void render(DivisionText block) throws IOException{
        assert block != null: "null block";
        changeAlign(block.getLineAlignment());
        if (block.getPrefix().isPresent()){
            /// move back to print prefix
            moveText(block.getPrefixDistance(), 0);
            contentStream.showText(block.getPrefix().get());
            /// move forward to print text
            moveText(-block.getPrefixDistance(), 0);
        }
        for (DivisionText.Line line: block){
            /// move to indent
            moveText(line.getIndent(), 0);
            printText(line);
            /// move to next line
            moveText(0, -line.getHeight());
            /// move to remove indent
            moveText(-line.getIndent(), 0);
        }
    }

    /** Change the alignment of text lines
     * @param next
     *      the alignment to set; not null
     * @throws IOException
     *      exception with content rendering
     * @see #render(DivisionBlock)
     */
    private void changeAlign(LineAlignment next) throws IOException{
        assert next != null: "null next";
        if (lineAlignment == next){
            return;
        }
        switch (lineAlignment){
        case CENTER:
            switch(next){
            case RIGHT:  moveText((areaWidth / 2), 0); break;
            case CENTER: assert false;                 break;
            default:     moveText(-(areaWidth / 2), 0);
            } break;
        case RIGHT:
            switch(next){
            case RIGHT:  assert false;                  break;
            case CENTER: moveText(-(areaWidth / 2), 0); break;
            default:     moveText(-areaWidth, 0);
            } break;
        default: switch (next){
            case RIGHT:  moveText(areaWidth, 0);     break;
            case CENTER: moveText(areaWidth / 2, 0); break;
            default:     moveText(0, 0);
            }
        }
        lineAlignment = next;
    }

    /** Print the text in the line
     * @param line
     *      the line to print; not null
     * @throws IOException
     *      exception with content rendering
     * @see #render(DivisionBlock)
     */
    private void printText(DivisionText.Line line) throws IOException{
        assert line != null: "null line";
        /// move the text base on width and LineAlignment
        if (lineAlignment == LineAlignment.RIGHT){
            moveText(-line.getWidth(), 0);
        } else if (lineAlignment == LineAlignment.CENTER){
           moveText(-(line.getWidth() / 2), 0);
        }

        /// more setup
        float textLocalX = localX;
        for (ContentText text: line){
            PDRectangle rect = new PDRectangle(textLocalX, localY,
                text.getWidth(), text.getHeight());

            /// adding into the contentStream
            changeFont(text.getFont());
            contentStream.showText(text.getText());
            postEditors.addAll(text.getPostTextConsumers(rect));

            /// update the x for the start x rectangle
            textLocalX += text.getWidth();
        }

        /// move the alignment back
        if (lineAlignment == LineAlignment.RIGHT){
            moveText(line.getWidth(), 0);
        } else if (lineAlignment == LineAlignment.CENTER){
            moveText(line.getWidth() / 2, 0);
        }
    }

    /**Change the font of the text.
     * @param font
     *      the font to set; not null
     * @throws IOException
     *      exception with content rendering
     * @see #printText(DivisionText.Line)
     */
    private void changeFont(ContentFont font) throws IOException{
        assert font != null: "null font";
        if (textFont == null || ! font.equals(textFont)){
            contentStream.setNonStrokingColor(font.getColor());
            contentStream.setFont(font.getFont(), font.getSize());
            textFont = font;
        }
    }

    /** Moves thext by x and y position. Also update the postion of text
     * (Skiping typing {@code @see}; too many to list.)
     * @param x
     *      the x parameter for
     *      {@linkplain PDPageContentStream#newLineAtOffset(float, float)}
     * @param y
     *      the y parameter for
     *      {@linkplain PDPageContentStream#newLineAtOffset(float, float)}
     * @throws IOException
     *      exception with content rendering
     */
    private void moveText(float x, float y) throws IOException{
        localX += x;
        localY += y;
        contentStream.newLineAtOffset(x, y);
    }

    @Override
    public void add(int index, Division item){
        Checker.checkNotNull(item, "item");
        divisionLines.add(index, item);
        fillHeight += item.getHeight();
    }

    @Override
    public boolean add(Division line){
        return standardAdd(line);
    }

    @Override
    public boolean addAll(Collection<? extends Division> c) {
        return standardAddAll(c);
    }

    @Override
    protected List<Division> delegate(){
        return divisionLines;
    }
}