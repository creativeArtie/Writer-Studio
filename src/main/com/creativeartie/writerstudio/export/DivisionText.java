package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // ArrayList, List, Optional

import com.google.common.collect.*; // ForwardingList, ImmuableList

import com.creativeartie.writerstudio.export.value.*; // ContentFont, ContentPostEditor, LineAlignment
import com.creativeartie.writerstudio.main.*; // Checker

import org.apache.pdfbox.pdmodel.common.*; // PDRectangle

/** A {@link Division} for text.
 */
class DivisionText extends ForwardingList<DivisionText.Line>
    implements Division{

    /** Split {@link DivisionText} across the page, kept line empty.
     *
     * @param item
     *      item to copy format; not null
     * @return answer
     */
    static final DivisionText splitItem(DivisionText item){
        Checker.checkNotNull(item, "item");
        DivisionText ans = copyFormat(item);
        ans.divFirstIndent = ans.divIndent;
        ans.divPrefix = Optional.empty();
        ans.divPrefixDistance = 0f;
        return ans;
    }

    /** Create {@link DivisionText} with copied format, kept line empty.
     *
     * @param item
     *      item to copy format; not null
     * @return answer
     */
    static final DivisionText copyFormat(DivisionText item){
        Checker.checkNotNull(item, "item");
        DivisionText ans = new DivisionText(item.divWidth, item.divAlignment);
        ans.divLeading = item.divLeading;
        ans.divFirstIndent = item.divFirstIndent;
        ans.divIndent = item.divIndent;
        ans.divPrefix = item.divPrefix;
        ans.divPrefixDistance = item.divPrefixDistance;
        ans.divBottomSpacing = item.divBottomSpacing;
        return ans;
    }

    /** A single line in the division.
     * Purpose:
     * <ul>
     * <li>Store a list of {@link ContentText} </li>
     * <li>Deciding if a text fit and what left over</li>
     * <li>Where to start the text</li>
     * </ul>
     */
    final class Line extends ForwardingList<ContentText>{
        private final ArrayList<ContentText> inputText;
        private float curWidth;
        private final float maxWidth;
        private float textHeight;
        private final float lineIndent;

        /** Private constrcutor.
         * @param width
         *      width of the line
         * @param indent
         *      the line indent
         * @see #getLine()
         * @see #appendText(List)
         * @see #reflowText()
         */
        private Line(float width, float indent){
            inputText = new ArrayList<>();
            curWidth = 0;
            maxWidth = width;
            textHeight = 0;
            lineIndent = indent;
        }

        /** Get line indent.
         * @return answer
         */
        float getIndent(){
            return lineIndent;
        }

        /** Append text unless the line is too full.
         * @param texts
         *      text to append; not null
         * @return text overflowed
         * @see #appendText(List overflow, Line line)
         */
        private ArrayList<ContentText> appendText(List<ContentText> texts){
            assert texts != null;
            ArrayList<ContentText> overflow = null;
            for (ContentText text: texts){

                /// Ignores the first space in a new line
                if (inputText.isEmpty() && text.isSpaceText()){
                    continue;
                } // else
                if (overflow == null){
                    if (curWidth + text.getWidth() > maxWidth){

                        /// Appending text does not fit line
                        overflow = new ArrayList<>();
                        if (inputText.isEmpty()){
                            /// Text can not fit line
                            inputText.add(text);
                            text.setListener(data -> reflowText());
                            curWidth = text.getWidth();
                            continue;
                        }
                        int last = inputText.size() - 1;
                        if (! inputText.get(last).isSpaceText()){
                            overflow.add(inputText.remove(last));
                        }
                        overflow.add(text);
                    } else {

                        /// text fit line
                        if (text.getHeight() > textHeight){
                            textHeight = text.getHeight();
                        }
                        inputText.add(text);
                        curWidth += text.getWidth();
                    }
                } else {

                    ///adds the overflow
                    overflow.add(text);
                }
            }

            /// don't return null array list
            return overflow == null? new ArrayList<>(): overflow;
        }

        /** Calculate line height
         * @return answer
         */
        float getHeight(){
            return (divLines.get(divLines.size() - 1) == this?
                    divBottomSpacing: 0f /// bottom spacing for last text
                ) + textHeight * divLeading;
        }

        /** Calculate text height
         * @return answer
         */
        float getTextHeight(){
            return textHeight;
        }

        /** Calculate current line height
         * @return answer
         */
        float getWidth(){
            return curWidth;
        }

        @Override
        protected List<ContentText> delegate(){
            return ImmutableList.copyOf(inputText);
        }
    }

    private final ArrayList<Line> divLines;
    private float divWidth;
    private LineAlignment divAlignment;
    private float divLeading;
    private float divFirstIndent;
    private float divIndent;
    private Optional<String> divPrefix;
    private float divPrefixDistance;
    private float divBottomSpacing;

    /** Construtor with a user defined width
     *
     * @param width
     *      the width of the line
     */
    DivisionText(float width){
        this (width, LineAlignment.LEFT);
    }

    /** Construtor with a user defined width and alignment
     *
     * @param width
     *      the width of the line
     * @param alignment
     *      the user defined alginment; not null
     */
    DivisionText(float width, LineAlignment alignment){
        Checker.checkNotNull(alignment, "alignment");
        divLines = new ArrayList<>();
        divWidth = width;
        divAlignment = alignment;
        divLeading = 2;
        divFirstIndent = 0;
        divIndent = 0;
        divPrefix = Optional.empty();
        divPrefixDistance = 0f;
        divBottomSpacing = 0;
    }

    /** Set line width
     * @return self
     */
    final DivisionText setWidth(float width){
        divWidth = width;
        reflowText();
        return this;
    }

    /** Get line alignment.
     * @return answer
     */
    final LineAlignment getLineAlignment(){
        return divAlignment;
    }

    /** Set line alignment.
     * @param alignment
     *      the value; not null
     * @return self
     */
    final DivisionText setLineAlignment(LineAlignment alignment){
        Checker.checkNotNull(alignment, "alignment");
        divAlignment = alignment;
        return this;
    }

    /** Get text leading.
     * @return answer
     */
    final float getLeading(){
        return divLeading;
    }

    /** Set text leading.
     * @return self
     */
    final DivisionText setLeading(float leading){
        divLeading = leading;
        reflowText();
        return this;
    }


    /** Set first line indent.
     * @param indent
     *      the value
     * @return self
     */
    final DivisionText setFirstIndent(float indent){
        divFirstIndent = indent;
        reflowText();
        return this;
    }

    /** Set non first line indent.
     * @param indent
     *      the value
     * @return self
     */
    final DivisionText setIndent(float indent){
        divIndent = indent;
        reflowText();
        return this;
    }

    /** Get line prefix.
     * @return answer
     */
    final Optional<String> getPrefix(){
        return divPrefix;
    }

    /** Get line prefix distance.
     * @return answer
     */
    final float getPrefixDistance(){
        return divPrefixDistance;
    }

    /** Set line prefix and distance
     * @param prefix
     *      the text to insert; not null
     * @param distance
     *      the indent to print
     * @return self
     */
    final DivisionText setPrefix(String prefix, float distance){
        divPrefix = Optional.ofNullable(prefix);
        divPrefixDistance = distance;
        return this;
    }

    /**  Set bottom spacing.
     * @param padding
     *      the value
     * @return self
     */
    final DivisionText setBottomSpacing(float padding){
        divBottomSpacing = padding;
        reflowText();
        return this;
    }

    @Override
    public final float getHeight(){
        float ans = 0;
        for (Line line: divLines){
            ans += line.getHeight();
        }
        return ans;
    }

    @Override
    public final float getStartY(){
        return isEmpty()? 0: get(0).getTextHeight() * (getLeading());
    }

    @Override
    public final float getWidth(){
        return divWidth;
    }

    @Override
    public final List<ContentPostEditor> getPostTextConsumers(PDRectangle rect){
        return new ArrayList<>();
    }

    /** Append taken from a line.
     * @param line
     *      the list of {@linkplain ContentText} to add
     * @return the actual text added
     * @see #appendText(String, ContentFont)
     * @see #appendTextList(String, ContentFont)
     */
    final List<ContentText> addLine(Line line){
        /// Create copy
        ArrayList<ContentText> add = new ArrayList<>();
        for (ContentText text: line){
            add.add(new ContentText(text));
        }

        /// add and return
        appendText(add, getLine());
        return add;
    }

    /** Append taken from a {@linkplain String}
     * @param text
     *      the text to extract {@link ContentText}
     * @param font
     *      the font of the text
     * @return self
     * @see #addLine(Line)
     * @see #appendTextList(String, ContentFont)
     */
    final DivisionText appendText(String text, ContentFont font)
            throws IOException{
        /// Append text to the previous line
        appendTextList(text, font);
        return this;
    }

    /** Append taken from a {@linkplain String}
     * @param text
     *      the text to extract {@link ContentText}
     * @param font
     *      the font of the text
     * @return the actual text added
     * @throws IOException
     *         exception with content adding
     * @see #appendText(String, ContentFont)
     * @see #addLine(Line)
     */
    final ArrayList<ContentText> appendTextList(String text, ContentFont font)
            throws IOException{
        /// Append text to the previous line
        ArrayList<ContentText> data = ContentText.createWords(text, font);
        appendText(data, getLine());
        return data;
    }

    /** Get or create the first line.
     * @return answer
     * @see #addLine(Line)
     * @see #appendTextList(String, ContentFont)
     */
    private final Line getLine(){
        if (divLines.isEmpty()){
            Line line = new Line(divWidth - divFirstIndent, divFirstIndent);
            divLines.add(line);
            return line;
        }
        return divLines.get(divLines.size() - 1);
    }

    /** Append text recusively
     * @param overflow
     *      the text to add
     * @param line
     *      the line to append the text to
     * @see #appendText(List)
     * @see #addLine(Line)
     * @see #appendTextList(String, ContentFont)
     * @see #reflowText()
     */
    private final void appendText(List<ContentText> overflow, Line line){
        assert overflow != null;
        assert line != null;
        /// recursively call children
        appendText(line.appendText(overflow));
    }

    /** Create a new a line append text
     * Had the base case of the recusive method.
     *
     * @param overflow
     *      parameter for {@linkplain #appendText(List, Line)
     * @see #appendText(List, Line)
     */
    private final void appendText(List<ContentText> overflow){
        if (overflow.isEmpty()) return;
        Line line = new Line(divWidth - divIndent, divIndent);
        divLines.add(line);
        appendText(overflow, line);
    }

    @Override
    protected final List<Line> delegate(){
        return ImmutableList.copyOf(divLines);
    }

    /** Reflow the text.
     * (Skiping typing {@code @see}; too many to list.)
     */
    private final void reflowText(){
        /// Load the data
        ArrayList<ContentText> data = new ArrayList<>();
        for (Line line: divLines){
            data.addAll(line);
        }

        /// Clear lines
        divLines.clear();

        /// Add new line
        Line first = new Line(divWidth - divFirstIndent, divFirstIndent);
        divLines.add(first);

        /// re-enter text
        appendText(data, first);
    }
}