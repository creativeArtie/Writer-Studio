package com.creativeartie.writerstudio.pdf.value;

import java.io.*;
import java.util.Optional;
import java.util.Objects;
import java.util.function.Function;
import java.awt.*;

import org.apache.pdfbox.pdmodel.font.*;

import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.base.*;

public final class SizedFont{

    public interface FontChanger{
        public PDFont getFont(boolean mono, boolean bold, boolean italics,
            boolean superscript);
    }

    /** Describes what field is changed. Using constructor the normal way was
     * to long and prone to mistakes. Making builder class is too much work. */
    private enum Key {
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

    public SizedFont(FontChanger changer){
        this(changer, false, 12, Color.BLACK, false, false, false, false);
    }

    private SizedFont(SizedFont res, Key edit, Object replace){
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

    private SizedFont(FontChanger changer, boolean mono, int size, Color color,
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

    public float getWidth(String text) throws IOException{
        /// From https://stackoverflow.com/questions/13701017/calculation-string-width-in-pdfbox-seems-only-to-count-characters
        return textFont.getStringWidth(text) / 1000 * textSize;
    }

    public float getHeight(){
        PDFontDescriptor descipter = textFont.getFontDescriptor();
        return (descipter.getCapHeight() + descipter.getXHeight()) / 1000 *
            textSize;
    }

    public SizedFont changeSize(int size){
        if (size == textSize){
            return this;
        }
        Object key = new Object();
        return new SizedFont(this, Key.SIZE, 12);
    }

    public SizedFont changeToSerif(){
        return new SizedFont(this, Key.FONT, false);
    }

    public SizedFont changeToMono(){
        return new SizedFont(this, Key.FONT, true);
    }

    public SizedFont changeFontColor(Color color){
        checkNotNull(color, "color");
        return new SizedFont(this, Key.COLOR, color);
    }

    public SizedFont changeBold(boolean b){
        return new SizedFont(this, Key.BOLD, b);
    }

    public SizedFont changeItalics(boolean b){
        return new SizedFont(this, Key.ITALICS, b);
    }

    public SizedFont changeUnderline(boolean b){
        return new SizedFont(this, Key.LINED, b);
    }

    public SizedFont changeToSuperscript(){
        return new SizedFont(this, Key.SUPER, true);
    }

    public SizedFont changeToNoramlScript(){
        return new SizedFont(this, Key.SUPER, false);
    }

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
        if (obj != null && obj instanceof SizedFont){
            SizedFont other = (SizedFont) obj;
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