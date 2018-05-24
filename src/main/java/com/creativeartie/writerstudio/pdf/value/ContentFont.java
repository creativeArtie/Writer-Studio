package com.creativeartie.writerstudio.pdf.value;

import java.awt.*;
import java.io.*;
import java.util.Objects;

import com.google.common.base.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Information about the font, to be filled by sub-classes. */
public abstract class ContentFont<T, U extends ContentFont<T, U>>{

    /** Describes what field is changed.
     *
     * Using constructor the normal way was to long and prone to
     * mistakes. Making builder class is too much work.
     */
    protected enum Key {
        /** Font size changed. */               SIZE,
        /** Font color changed. */              COLOR,

        /** Font bold format changed. */        BOLD,
        /** Font italics format changed. */     ITALICS,
        /** Font underline format changed. */   LINED,
        /** Font superscript format changed. */ SUPER,

        /** Font family changed. */             FONT;
    }

    private final int textSize;
    private final Color textColor;

    private final boolean textBold;
    private final boolean textItalics;
    private final boolean textLined;
    private final boolean textSuper;

    private final boolean textMono;
    private final T textFont; /// This depends on format

    /** Creating the first {@link ContentFont}. */
    protected ContentFont(){
        textSize = 12;
        textColor = Color.BLACK;

        textBold = false;
        textItalics = false;
        textLined = false;
        textSuper = false;

        textMono = false;
        textFont = buildFont(false, false, false, false);
    }

    /** Creating the updated {@link ContentFont}.
     *
     * @param old
     *      old {@linkplain ContentFont}.
     * @param edit
     *      edited field
     * @param replace
     *      replace value
     */
    protected ContentFont(ContentFont<T, U> old, Key edit, Object replace){
        argumentNotNull(old, "old");
        argumentNotNull(edit, "edit");

        textColor = check(Key.COLOR, edit, replace, old.textColor);
        textSize  = check(Key.SIZE,  edit, replace, old.textSize);

        textBold    = check(Key.BOLD,    edit, replace, old.textBold);
        textItalics = check(Key.ITALICS, edit, replace, old.textItalics);
        textLined   = check(Key.LINED,   edit, replace, old.textLined);
        textSuper   = check(Key.SUPER,   edit, replace, old.textSuper);

        if (Key.FONT == edit){
            /// Replaces font families
            textMono = (Boolean)replace;
            textFont = buildFont(textMono, textBold,
                textItalics, textMono);
        } else if (Key.ITALICS == edit || Key.BOLD == edit ||
                Key.SUPER == edit){
            /// Replaces font due to bold or italics, etc.
            textMono = old.textMono;
            textFont = buildFont(textMono, textBold,
                textItalics, textSuper);
        } else {
            /// No font changes
            textMono = old.textMono;
            textFont = old.textFont;
        }
    }

    /** Check the type of data.
     *
     * @param match
     *      match to key
     * @param actual
     *      actual key
     * @param obj
     *      setting/ checking object
     * @param old
     *      old value
     * @return result
     * @see #ContentFont(ContentFont, Key, Object)
     */
    @SuppressWarnings("unchecked") /// guaranteed by getClass javadoc
    private <V> V check(Key match, Key actual, Object obj, V old){
        Class<? extends V> clazz = (Class<? extends V>)old.getClass();
        return match == actual?
                argumentClass(obj, match.toString(), clazz): old;
    }

    /** Gets the new font.
     *
     * @param mono
     *      is mono text
     * @param bold
     *      is bold text
     * @param italics
     *      is italics text
     * @param superscript
     *      is superscript text
     * @return answer
     * @see #ContentFont()
     * @see #ContentFont(ContentFont, Key, Object)
     */
    protected abstract T buildFont(boolean mono, boolean bold,
        boolean italics, boolean superscript);

    /** Gets the font size.
     *
     * @return answer
     */
    public int getSize(){
        return textSize;
    }

    /** Change the font size
     *
     * @param size
     *      new font size
     * @return result
     */
    public U changeSize(int size){
        return produce(this, Key.SIZE, size);
    }

    /** Gets the font color.
     *
     * @return answer
     */
    public Color getColor(){
        return textColor;
    }

    /** Change the font color.
     *
     * @param color
     *      changing color
     * @return result
     */
    public U changeFontColor(Color color){
        argumentNotNull(color, "color");
        return produce(this, Key.COLOR, color);
    }

    /** Change bold formatting on or off.
     *
     * @param b
     *      new boolean.
     * @return result
     */
    public U changeBold(boolean b){
        return produce(this, Key.BOLD, b);
    }

    /** Change italics formatting on or off.
     *
     * @param b
     *      new boolean
     * @return result
     */
    public U changeItalics(boolean b){
        return produce(this, Key.ITALICS, b);
    }

    /** Check if font is a underline.
     *
     * @return answer
     */
    public boolean isUnderline(){
        return textLined;
    }

    /** Change underline formatting on or off.
     *
     * @param b
     *      new boolean
     * @return result
     */
    public U changeUnderline(boolean b){
        return produce(this, Key.LINED, b);
    }

    /** Change font to superscript.
     *
     * @return result
     */
    public U changeToSuperscript(){
        return produce(this, Key.SUPER, true);
    }

    /** Check if font is a superscript.
     *
     * @return answer
     */
    public boolean isSuperscript(){
        return textSuper;
    }

    /** Change font to normal script.
     *
     * @return result
     */
    public U changeToNoramlScript(){
        return produce(this, Key.SUPER, false);
    }

    /** Gets the font object.
     *
     * @return answer
     */
    public T getFont(){
        return textFont;
    }

    /** Change font to a serif font type.
     *
     * As oppose to {@link #changeToMono()}.
     *
     * @return result
     */
    public U changeToSerif(){
        return produce(this, Key.FONT, false);
    }

    /** Change font to a mono font type.
     *
     *
     * As oppose to {@link #changeToSerif()}.
     * @return result
     */
    public U changeToMono(){
        return produce(this, Key.FONT, true);
    }

    /** Produce a new {@link ContentFont} with the correct {@code T}
     * class paramemter.
     *
     * @param font
     *      reference font
     * @param key
     *      update key
     * @param value
     *      new value
     */
    protected abstract U produce(ContentFont<T, U> font, Key key, Object value);

    /** Gets the width of the text using this font.
     *
     * @param text
     *      calculating text
     * @return answer
     */
    public abstract float getWidth(String text);

    /** Gets the height of the text using this font.
     *
     * @return answer
     */
    public abstract float getHeight();

    @Override
    public boolean equals(Object obj){
        if (obj != null && obj instanceof ContentFont){
            ContentFont other = (ContentFont) obj;
            return Objects.equals(textFont, other.textFont) &&
                textSize == other.textSize &&
                textSuper == other.textSuper &&
                Objects.equals(textColor, other.textColor);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(textFont, textSize, textSuper, textColor);
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .add("font", textFont)
            .add("size", textSize)
            .add("color", textColor)
            .toString();
    }
}
