package com.creativeartie.writerstudio.pdf;

import java.io.*; // IOException
import java.util.*; // ArrayList, List
import java.util.Optional;
import java.util.function.*;// Consumer

import com.google.common.base.*; // CharMatcher, Splitter

import org.apache.pdfbox.pdmodel.common.*; // PDRectangle
import org.apache.pdfbox.pdmodel.interactive.action.*; // PDActionURI
import org.apache.pdfbox.pdmodel.interactive.annotation.*; // PDAnnotationLink

import com.creativeartie.writerstudio.pdf.value.*; // ContentFont, ContentPostEditor
import com.creativeartie.writerstudio.lang.*; // SpanBranch
import static com.creativeartie.writerstudio.main.Checker.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Stores a text and its properties.
 *
 * Purposes:
 *
 * <ul>
 * <li> stores properites relating to a single span of text. </li>
 * <li> inform {@link DivisionLine.Line} of updates</li>
 * <li>render text related things</li>
 * <ul>
 */
final class ContentText {

    private static final String SPACE = " ";

    /** Create {@linkplain ContentText} by splitting up the text.
     *
     * This is use to create {@link ContentText}.
     *
     * @param text
     *      the text to split up with; not null
     * @param font
     *      the font of the text; not null
     * @return
     *      a list of {@linkplain ContentText} splited by spaces.
     * @see DivisonText#appendText(String, ContentFont)
     * @see SectionContentMain#parse(LinedSpanLevelList, String)
     */
    static ArrayList<ContentText> createWords(String text, ContentFont font)
            throws IOException{
        argumentNotNull(text, "text");
        argumentNotNull(font, "font");

        /// Setup for repeating uses
        ContentText space = new ContentText(SPACE, font, true);
        CharMatcher whitespace = CharMatcher.whitespace();

        /// Split the text into words
        ArrayList<ContentText> holder = new ArrayList<>();
        for (String word: Splitter.on(whitespace).omitEmptyStrings().split(text)
        ){
            holder.add(new ContentText(word, font, false));
        }
        int i = 0;
        boolean isFirst = true;
        ArrayList<ContentText> ans = new ArrayList<>();
        if (whitespace.indexIn(text) == 0){
            ans.add(space);
        }

        /// Turn String into ContentText
        for (ContentText item: holder){
            if (isFirst){
                isFirst = false;
            } else {
                ans.add(space);
            }
            ans.add(item);
        }

        /// Add the last space if necessary
        if (whitespace.lastIndexIn(text) == text.length() - 1){
            ans.add(space);
        }
        return ans;
    }

    private String outputText;
    private boolean spaceText;
    private Optional<Consumer<ContentText>> textChange;

    private final float textHeight;
    private float textWidth;

    private final ContentFont textFont;
    private Optional<String> targetPath;
    private Optional<SpanBranch> footnoteSpan;

    /** Creates a {@linkplain ContentText} by copying.
     * 
     * @param original
     *      original object
     * @see DivisionText#addLine(DivisionText.Line)
     */
    ContentText(ContentText original){
        argumentNotNull(original, "original");

        outputText = original.outputText;
        spaceText = original.spaceText;
        textChange = Optional.empty(); /// stop updating it's previous parent

        textHeight = original.textHeight;
        textWidth = original.textWidth;

        textFont = original.textFont;
        targetPath = original.targetPath;
        footnoteSpan = original.footnoteSpan;
    }

    /** Creates a {@linkplain ContentText}.
     * 
     * @param text
     *      content text
     * @param font
     *      text font
     * @param space
     *      is space text
     * @see #createWords(String, ContentFont)
     */
    private ContentText(String text, ContentFont font, boolean space){
        assert text != null && !text.isEmpty();
        assert font != null;
        assert text == SPACE? space: !space;

        outputText = text;
        spaceText = space;
        textChange = Optional.empty();

        textHeight = font.getHeight();
        textWidth = font.getWidth(text);

        textFont = font;
        targetPath = Optional.empty();
        footnoteSpan = Optional.empty();
    }

    /** Get the text.
     *
     * @return answer
     * @see DivisionText.Line#toString()
     * @see MatterArea MatterArea.printText(List)
     */
    String getText(){
        return outputText;
    }

    /** Change the text.
     * 
     * <b> TODO: text isn't suitable to deal with whitespaces</b>
     * @param text
     *      the new text; not null
     * @return self
     * @see DivisionTextNote#setNumbering(String)
     * @see PageFootnote#resetFootnote(ContentText)
     */
    ContentText setText(String text) {
        argumentNotEmpty(text, "text");
        CharMatcher whitespace = CharMatcher.whitespace();
        outputText = whitespace.matchesAllOf(text)? SPACE:
            whitespace.trimAndCollapseFrom(text, ' ');
        textWidth = textFont.getWidth(text);
        textChange.ifPresent(consume -> consume.accept(this));
        return this;
    }

    /** Check if the text is a space.
     * 
     * @return answer
     * @see DivisionText DivisionText.appendText(String)
     */
    boolean isSpaceText(){
        return spaceText;
    }

    /** Sets the listener.
     * 
     * @param
     *      the consumer to set
     * @return self
     * @see DivisionText DivisionText.appendText(String)
     */
    ContentText setListener(Consumer<ContentText> consumer){
        textChange = Optional.ofNullable(consumer);
        return this;
    }

    /** Get the height of the text.
     * 
     * @return answer
     * @see MatterArea MatterArea.printText(List)
     */
    float getHeight(){
        return textHeight;
    }

    /** Get the width of the text.
     * 
     * @return answer
     * @see #getHeight()
     */
    float getWidth() {
        return textWidth;
    }

    /** Get the font of the text
     * @return answer
     */
    ContentFont getFont(){
        return textFont;
    }

    /** Get the width of the text.
     * @return answer
     */
    ContentText setLinkPath(String path){
        targetPath = Optional.ofNullable(path);
        return this;
    }

    /** Get the footnote.
     * @return answer
     * @see setFootnote(Optional)
     */
    Optional<SpanBranch> getFootnote(){
        return footnoteSpan;
    }

    /** Set the text target
     * @param span
     *      the span to set
     * @return self
     * @see getFootnote()
     */
    ContentText setFootnote(Optional<SpanBranch> span){
        checkNotNull(span, "span");
        footnoteSpan = span;
        return this;
    }

    /** Add rendering that is not related to printing of text
     *
     * @param rect
     *      the location of the text
     * @return a list of function showing what need to be done
     * @see Division#getPostConsumer(PDRectangle)
     */
    List<ContentPostEditor> getPostTextConsumers(PDRectangle rect){
        checkNotNull(rect, "rect");

        ArrayList<ContentPostEditor> ans = new ArrayList<>();

        /// For underlining
        if (textFont.isUnderline()){
            ans.add((page, stream) -> {
                stream.setStrokingColor(textFont.getColor());
                /// -2 = location
                stream.moveTo(rect.getLowerLeftX(), rect.getLowerLeftY() - 2);
                stream.lineTo(rect.getUpperRightX(), rect.getLowerLeftY() - 2);
                stream.stroke();
            });
        }

        /// For links
        targetPath.ifPresent(path -> ans.add((page, stream) ->{
            PDAnnotationLink link = new PDAnnotationLink();
            PDActionURI action = new PDActionURI();
            action.setURI(path);
            link.setAction(action);
            link.setRectangle(rect);
            page.getAnnotations().add(link);
        }));
        return ans;
    }

    @Override
    public String toString(){
        return "\"" + outputText + "\"";
    }
}
