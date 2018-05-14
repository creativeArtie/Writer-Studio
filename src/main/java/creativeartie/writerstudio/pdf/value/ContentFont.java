package com.creativeartie.writerstudio.pdf.value;

import java.awt.*; // Color
import java.io.*; // IOException
import java.util.Optional;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.base.*; // MoreObjects

import org.apache.pdfbox.pdmodel.font.*; // PDFont

import static com.creativeartie.writerstudio.main.Checker.*;


public abstract class ContentFont{

    public interface FontChanger{
        public PDFont getFont(boolean mono, boolean bold, boolean italics,
            boolean superscript);
    }

    /** Describes what field is changed. Using constructor the normal way was
     * to long and prone to mistakes. Making builder class is too much work. */
    protected enum Key {
        FONT, SIZE, COLOR, BOLD, ITALICS, LINED, SUPER;
    }

    private final PDFont textFont;
    private final boolean textMono;
    private final int textSize;
    private final Color textColor;
    private final boolean textBold;
    private final boolean textItalics;
    private final boolean textLined;
    private final boolean textSuper;
    private final FontChanger fontChanger;

    protected ContentFont(FontChanger changer){
        this(changer, false, 12, Color.BLACK, false, false, false, false);
    }

    protected ContentFont(ContentFont res, Key edit, Object replace){
        textColor   = edit == Key.COLOR?   (Color)   replace: res.textColor;
        textSize    = edit == Key.SIZE?    (Integer) replace: res.textSize;
        textBold    = edit == Key.BOLD?    (Boolean) replace: res.textBold;
        textItalics = edit == Key.ITALICS? (Boolean) replace: res.textItalics;
        textLined   = edit == Key.LINED?   (Boolean) replace: res.textLined;
        textSuper   = edit == Key.SUPER?   (Boolean) replace: res.textSuper;
        fontChanger = res.fontChanger;
        if (Key.FONT == edit){
            /// Replaces font families
            textMono = (Boolean)replace;
            textFont = fontChanger.getFont(textMono, textBold, textItalics,
                textMono);
        } else if (Key.ITALICS == edit || Key.BOLD == edit || Key.SUPER == edit){
            /// Replaces font due to bold or italics, etc.
            textMono = res.textMono;
            textFont = fontChanger.getFont(textMono, textBold, textItalics,
                textSuper);
        } else {
            textMono = res.textMono;
            textFont = res.textFont;
        }
    }

    private ContentFont(FontChanger changer, boolean mono, int size, Color color,
            boolean bold, boolean italics, boolean underline, boolean superscript){
        textFont = changer.getFont(mono, bold, italics, superscript);
        textMono = mono;
        textSize = size;
        textColor = color;
        textBold = bold;
        textItalics = italics;
        textLined = underline;
        textSuper = superscript;
        fontChanger = changer;
    }

    public PDFont getFont(){
        return textFont;
    }

    public int getSize(){
        return textSize;
    }

    public Color getColor(){
        return textColor;
    }

    public ContentFont changeSize(int size){
        if (size == textSize){
            return this;
        }
        Object key = new Object();
        return produce(this, Key.SIZE, 12);
    }

    public ContentFont changeToSerif(){
        return produce(this, Key.FONT, false);
    }

    public ContentFont changeToMono(){
        return produce(this, Key.FONT, true);
    }

    public ContentFont changeFontColor(Color color){
        checkNotNull(color, "color");
        return produce(this, Key.COLOR, color);
    }

    public ContentFont changeBold(boolean b){
        return produce(this, Key.BOLD, b);
    }

    public ContentFont changeItalics(boolean b){
        return produce(this, Key.ITALICS, b);
    }

    public ContentFont changeUnderline(boolean b){
        return produce(this, Key.LINED, b);
    }

    public ContentFont changeToSuperscript(){
        return produce(this, Key.SUPER, true);
    }

    public ContentFont changeToNoramlScript(){
        return produce(this, Key.SUPER, false);
    }

    public abstract ContentFont produce(ContentFont font, Key key,
        Object value);

    public abstract float getWidth(String text) throws IOException;
    public abstract float getHeight() throws IOException;

    public boolean isSuperscript(){
        return textSuper;
    }

    public boolean isUnderline(){
        return textLined;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .add("font", textFont)
            .add("size", textSize)
            .add("color", textColor)
            .toString();
    }


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

}