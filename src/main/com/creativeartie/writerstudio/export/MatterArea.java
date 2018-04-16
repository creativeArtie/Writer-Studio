package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // ArrayList, Collection, List

import com.google.common.collect.*; // FowardingList

import org.apache.pdfbox.pdmodel.*; //PDPageContentStream
import org.apache.pdfbox.pdmodel.common.*; //PDRectangle

import com.creativeartie.writerstudio.export.value.*; // (many)

import static com.creativeartie.writerstudio.main.Checker.*; // Checker

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
    /// PDF output
    private final PageContent outputPage;
    private final PDPageContentStream contentStream;
    /// division lines
    private final ArrayList<Division> divisionLines;
    /// sizes
    private float maxHeight;
    private float fillHeight;
    private float localY;
    private final float areaWidth;
    private float localX;
    /// font + alignment
    private ContentFont textFont;
    private final PageAlignment pageAlignment;
    private LineAlignment lineAlignment;
    /// post editing
    private final ArrayList<ContentPostEditor> postEditors;

    /** Only constructor.
     * @param page
     *      the page where this section is location; not null
     * @param alignment
     *      the alignment on the page; not null
     */
    MatterArea(PageContent page, PageAlignment alignment){
        outputPage = checkNotNull(page, "page");
        contentStream = page.getContentStream();

        divisionLines = new ArrayList<>();

        maxHeight = page.getRenderHeight(alignment);
        areaWidth = page.getRenderWidth();
        fillHeight = localX = localY = 0;

        textFont = null;
        lineAlignment = LineAlignment.LEFT;
        pageAlignment = checkNotNull(alignment, "alignment");

        postEditors = new ArrayList<>();
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
     * @see #render()
     */
    private void render(DivisionText block) throws IOException{
        assert block != null: "null block";
        changeAlign(block.getLineAlignment());
        if (block.hasPrefix()){
            /// move back to print prefix
            moveText(block.getPrefixDistance(), 0);
            printText(block.getPrefix());
            /// move forward to print text
            moveText(-block.getPrefixDistance(), 0);
        }
        for (DivisionText.Line line: block){
            /// move to indent
            moveText(line.getIndent(), 0);
            printLine(line);
            /// move to next line
            moveText(0, -line.getHeight());
            /// move to remove indent
            moveText(-line.getIndent(), 0);
        }
    }

    /** Print the text in the line
     * @param line
     *      the line to print; not null
     * @throws IOException
     *      exception with content rendering
     * @see #render(DivisionBlock)
     */
    private void printLine(DivisionText.Line line) throws IOException{
        assert line != null: "null line";
        /// move the text base on width and LineAlignment
        if (lineAlignment == LineAlignment.RIGHT){
            moveText(-line.getWidth(), 0);
        } else if (lineAlignment == LineAlignment.CENTER){
           moveText(-(line.getWidth() / 2), 0);
        }

        printText(line);

        /// move the alignment back
        if (lineAlignment == LineAlignment.RIGHT){
            moveText(line.getWidth(), 0);
        } else if (lineAlignment == LineAlignment.CENTER){
            moveText(line.getWidth() / 2, 0);
        }
    }

    private void printText(List<ContentText> line) throws IOException{
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

    /**Change the font of the text.
     * @param font
     *      the font to set; not null
     * @throws IOException
     *      exception with content rendering
     * @see #printLine(DivisionText.Line)
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

    /** Gets the height that this section needs.
     * @return answer
     * @see #checkHeight(DivisionText)
     * @see #checkHeight(DivisionText, float)
     * @see #reduceHeight(float)
     */
    float getHeight(){
        return fillHeight;
    }

    /** Check the height that this section + this item needs
     * @return answer
     * @see #getHeight()
     * @see #checkHeight(DivisionText, float)
     * @see #reduceHeight(float)
     */
    boolean checkHeight(DivisionText item){
        checkNotNull(item, "item");

        return item.getHeight() + fillHeight < maxHeight;
    }

    /** Check the height that this section + this item needs, along with the
     * footnote height.
     * @return answer
     * @see #getHeight()
     * @see #checkHeight(DivisionText)
     * @see #reduceHeight(float)
     */
    boolean checkHeight(DivisionText item, float footnote){
        checkNotNull(item, "item");

        return item.getHeight() + footnote + fillHeight < maxHeight;
    }

    /** Reduce the height.
     *
     * Negative number will increase height instead.
     *
     * @param height
     *      subtracting height
     * @return self
     * @see #getHeight()
     * @see #checkHeight()
     * @see #checkHeight(DivisionText)
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

    @Override
    public void add(int index, Division item){
        checkNotNull(item, "item");
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