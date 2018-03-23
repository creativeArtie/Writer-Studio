package com.creativeartie.writerstudio.pdf.value;

import java.io.*;
import java.util.Objects;
import java.awt.*;

import org.apache.pdfbox.pdmodel.font.*;

import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.base.*;

public final class SizedFont{

    public static SizedFont newSerif(int size){
        return new SizedFont(getSerif(false, false), size, Color.BLACK,
            false, false, false, false);
    }

    private static PDFont getSerif(boolean bold, boolean italics){
        return bold?
            (italics? PDType1Font.TIMES_BOLD_ITALIC: PDType1Font.TIMES_BOLD):
            (italics? PDType1Font.TIMES_ITALIC: PDType1Font.TIMES_ROMAN);
    }

    public static SizedFont newCourier(int size){
        return new SizedFont(getCourier(false, false), size, Color.BLACK, false,
            false, false, false);
    }

    private static PDFont getCourier(boolean bold, boolean italics){
        return bold?
            (italics? PDType1Font.COURIER_BOLD_OBLIQUE: PDType1Font.COURIER_BOLD):
            (italics? PDType1Font.COURIER_OBLIQUE: PDType1Font.COURIER);
    }

    private final PDFont textFont;
    private final int textSize;
    private final Color textColor;
    private final boolean textBold;
    private final boolean textItalics;
    private final boolean textUnderline;
    private final boolean textSuperscript;

    private SizedFont(PDFont font, int size, Color color,
            boolean bold, boolean italics, boolean underline, boolean superscript){
        assert font != null: "Null font.";
        textFont = font;
        textSize = size;
        textColor = color;
        textBold = bold;
        textItalics = italics;
        textUnderline = underline;
        textSuperscript = superscript;
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
        return new SizedFont(textFont, size, textColor, textBold,
            textItalics, textUnderline, textSuperscript);
    }

    public SizedFont changeToTime(){
        PDFont font = getSerif(textBold, textItalics);
        if (font == textFont){
            return this;
        }
        return new SizedFont(font, textSize, textColor, textBold, textItalics,
            textUnderline, textSuperscript);
    }

    public SizedFont changeToCourier(){
        PDFont font = getCourier(textBold, textItalics);
        if (font == textFont){
            return this;
        }
        return new SizedFont(font, textSize, textColor, textBold, textItalics,
            textUnderline, textSuperscript);
    }

    public SizedFont changeFontColor(Color color){
        checkNotNull(color, "color");
        if (color == textColor){
            return this;
        }
        return new SizedFont(textFont, textSize, color, textBold, textItalics,
            textUnderline, textSuperscript);
    }

    public SizedFont changeBold(boolean b){
        return new SizedFont(getFont(textBold, textItalics), textSize,
            textColor, b, textItalics, textUnderline, textSuperscript);
    }

    public SizedFont changeItalics(boolean b){
        return new SizedFont(getFont(textBold, textItalics), textSize,
            textColor, textBold, b, textUnderline, textSuperscript);
    }

    private PDFont getFont(boolean bold, boolean italics){
        if (textFont.getName().equals("Times-Roman")){
            return getSerif(textBold, textItalics);
        }
        return getCourier(textBold, textItalics);
    }

    public SizedFont changeUnderline(boolean b){
        if (textUnderline == b){
            return this;
        }
        return new SizedFont(textFont, textSize, textColor, textBold,
            textItalics, b, textSuperscript);
    }

    public SizedFont asSuperscript(){
        return new SizedFont(textFont, textSize, textColor, textBold,
            textItalics, textUnderline, true);
    }

    public SizedFont asNormal(){
        return new SizedFont(textFont, textSize, textColor, textBold,
            textItalics, textUnderline, false);
    }

    public boolean isSuperscript(){
        return textSuperscript;
    }

    public boolean isUnderline(){
        return textUnderline;
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
                textSuperscript == other.textSuperscript &&
                Objects.equals(textColor, other.textColor);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(textFont, textSize, textSuperscript, textColor);
    }

}