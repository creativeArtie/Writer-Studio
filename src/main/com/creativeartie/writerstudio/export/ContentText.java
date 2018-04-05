package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // ArrayList, List
import java.util.Optional;
import java.util.function.*;// Consumer

import com.google.common.base.*; // CharMatcher, Splitter

import com.creativeartie.writerstudio.export.value.*; // ContentFont, ContentPostEditor
import com.creativeartie.writerstudio.lang.*; // SpanBranch
import com.creativeartie.writerstudio.main.*; // Checker

import org.apache.pdfbox.pdmodel.common.*; // PDRectangle
import org.apache.pdfbox.pdmodel.interactive.action.*; // PDActionURI
import org.apache.pdfbox.pdmodel.interactive.annotation.*; // PDAnnotationLink

/**
 * Stores a text and its properties.
 *
 * Purposes:
 *
 * <ul>
 * <li> stores properites relating to a single span of text. </li>
 * <li> inform {@link DivisionLine.Line} of updates</li>
 * <li>render text related things</li>
 * <ul>
 */
class ContentText {

    /** for adding new ContentText with a space. */
    private static final String SPACE = " ";

    /** create {@linkplain ContentText} by splitting up the text.
     *
     * This is use to create {@link ContentText}.
     *
     * @param text
     *      the text to split up with; not null
     * @param font
     *      the font of the text; not null
     * @return
     *      a list of {@linkplain ContentText} splited by spaces.
     * @throws IOException
     *      exception with calcuatate text size
     */
    static ArrayList<ContentText> createWords(String text, ContentFont font)
            throws IOException{
        Checker.checkNotNull(text, "text");
        Checker.checkNotNull(font, "font");

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
    private Optional<String> targetPath;
    private Optional<SpanBranch> targetSpan;
    private Optional<Consumer<ContentText>> textChange;
    private final ContentFont textFont;
    private final float textHeight;
    private float textWidth;

    /** Copy construtor
     * @param original
     *      The original object to copy from; not null
     */
    ContentText(ContentText original){
        Checker.checkNotNull(original, "original");
        outputText = original.outputText;
        spaceText = original.spaceText;
        targetPath = original.targetPath;
        targetSpan = original.targetSpan;
        textChange = Optional.empty(); /// stop updating it's previous parent
        textFont = original.textFont;
        textHeight = original.textHeight;
        textWidth = original.textWidth;
    }

    /**
     * private constructor.
     * @param word
     *      the word to add; not null or empty
     * @param font
     *      the font of the text; not null
     * @param space
     *      is this a space character; must describe param word.
     * @see #createWords(String, ContentFont)
     */
    private ContentText(String word, ContentFont font, boolean space)
            throws IOException{
        assert word != null && !word.isEmpty();
        assert font != null;
        assert word == SPACE? space: !space;
        outputText = word;
        spaceText = space;
        targetPath = Optional.empty();
        targetSpan = Optional.empty();
        textFont = font;
        textChange = Optional.empty();
        textHeight = textFont.getHeight();
        textWidth = textFont.getWidth(word);
    }

    /**
     * Get the text.
     * @return answer
     * @see #setText(String)
     */
    String getText(){
        return outputText;
    }

    // TODO text isn't suitable to deal with whitespaces
    /** Change the text.
     * @param text
     *      the new text; not null
     * @return self
     * @throws IOException
     *      exception with calculating sizes
     * @see #getText()
     * @see #isSpaceText()
     */
    ContentText setText(String text) throws IOException{
        outputText = Checker.checkNotEmpty(text, "text");
        textWidth = textFont.getWidth(text);
        textChange.ifPresent(consume -> consume.accept(this));
        return this;
    }

    /**
     * Sets the listener.
     *
     * @param
     *      the consumer to set
     * @return self
     * @see #setText(String)
     */
    ContentText setListener(Consumer<ContentText> consumer){
        textChange = Optional.ofNullable(consumer);
        return this;
    }

    /**
     * Get the width of the text.
     * @return answer
     * @see #getHeight()
     */
    float getWidth() {
        return textWidth;
    }

    /**
     * Get the height of the text.
     * @return answer
     * @see #getWidth()
     */
    float getHeight(){
        return textHeight;
    }

    /**
     * Get the width of the text.
     * @return answer
     */
    ContentText setLinkPath(String path){
        targetPath = Optional.ofNullable(path);
        return this;
    }

    /**
     * Is the text is a space
     * @return answer
     * @see #getText()
     */
    boolean isSpaceText(){
        return spaceText;
    }

    /**
     * Get the font of the text
     * @return answer
     */
    ContentFont getFont(){
        return textFont;
    }

    /**
     * Get the footnote.
     * @return answer
     * @see setFootnote(Optional)
     */
    Optional<SpanBranch> getFootnote(){
        return targetSpan;
    }

    /**
     * Set the text target
     * @param span
     *      the span to set
     * @return self
     * @see getFootnote()
     */
    ContentText setFootnote(Optional<SpanBranch> span){
        Checker.checkNotNull(span, "span");
        targetSpan = span;
        return this;
    }

    /**
     * Add rendering that is not related to printing of text
     *
     * @param rect
     *      the location of the text
     * @return a list of function showing what need to be done
     * @see Division#getPostConsumer(PDRectangle)
     */
    List<ContentPostEditor> getPostTextConsumers(PDRectangle rect){
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
