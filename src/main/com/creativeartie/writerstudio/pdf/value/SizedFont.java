package com.creativeartie.writerstudio.pdf.value;

import java.io.*;
import java.util.Objects;
import java.awt.*;

import org.apache.pdfbox.pdmodel.font.*;

import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.base.*;

public final class SizedFont{

    public static SizedFont newTimesRoman(int size){
        return new SizedFont(getTimesRoman(false, false), size, Color.BLACK,
            false, false, false);
    }

    private static PDFont getTimesRoman(boolean bold, boolean italics){
        return bold?
            (italics? PDType1Font.TIMES_BOLD_ITALIC: PDType1Font.TIMES_BOLD):
            (italics? PDType1Font.TIMES_ITALIC: PDType1Font.TIMES_ROMAN);
    }

    public static SizedFont newCourier(int size){
        return new SizedFont(getCourier(false, false), size, Color.BLACK, false,
            false, false);
    }

    private static PDFont getCourier(boolean bold, boolean italics){
        return bold?
            (italics? PDType1Font.COURIER_BOLD_OBLIQUE: PDType1Font.COURIER_BOLD):
            (italics? PDType1Font.COURIER_OBLIQUE: PDType1Font.COURIER);
    }

    private final PDFont fontName;
    private final int fontSize;
    private final Color fontColor;
    private final boolean fontBold;
    private final boolean fontItalics;
    private final boolean fontUnderline;

    private SizedFont(PDFont font, int size, Color color,
            boolean bold, boolean italics, boolean underline){
        assert font != null: "Null font.";
        fontName = font;
        fontSize = size;
        fontColor = color;
        fontBold = bold;
        fontItalics = italics;
        fontUnderline = underline;
    }

    public PDFont getFont(){
        return fontName;
    }

    public int getSize(){
        return fontSize;
    }

    public Color getColor(){
        return fontColor;
    }

    public float getWidth(String text) throws IOException{
        /// From https://stackoverflow.com/questions/13701017/calculation-string-width-in-pdfbox-seems-only-to-count-characters
        return fontName.getStringWidth(text) / 1000 * fontSize;
    }

    public float getHeight(){
        PDFontDescriptor descipter = fontName.getFontDescriptor();
        return (descipter.getCapHeight() + descipter.getXHeight()) / 1000 *
            fontSize;
    }

    public SizedFont changeSize(int size){
        if (size == fontSize){
            return this;
        }
        return new SizedFont(fontName, size, fontColor, fontBold,
            fontItalics, fontUnderline);
    }

    public SizedFont changeToTime(){
        PDFont font = getTimesRoman(fontBold, fontItalics);
        if (font == fontName){
            return this;
        }
        return new SizedFont(font, fontSize, fontColor, fontBold, fontItalics,
            fontUnderline);
    }

    public SizedFont changeToCourier(){
        PDFont font = getCourier(fontBold, fontItalics);
        if (font == fontName){
            return this;
        }
        return new SizedFont(font, fontSize, fontColor, fontBold, fontItalics,
            fontUnderline);
    }

    public SizedFont changeFontColor(Color color){
        checkNotNull(color, "color");
        if (color == fontColor){
            return this;
        }
        return new SizedFont(fontName, fontSize, color, fontBold, fontItalics,
            fontUnderline);
    }

    public SizedFont changeBold(boolean b){
        return new SizedFont(getFont(fontBold, fontItalics), fontSize,
            fontColor, b, fontItalics, fontUnderline);
    }

    public SizedFont changeItalics(boolean b){
        return new SizedFont(getFont(fontBold, fontItalics), fontSize,
            fontColor, fontBold, b, fontUnderline);
    }

    private PDFont getFont(boolean bold, boolean italics){
        if (fontName.getName().equals("Times-Roman")){
            return getTimesRoman(fontBold, fontItalics);
        }
        return getCourier(fontBold, fontItalics);
    }

    public SizedFont changeUnderline(boolean b){
        if (fontUnderline == b){
            return this;
        }
        return new SizedFont(fontName, fontSize, fontColor, fontBold,
            fontItalics, b);
    }

    public boolean isUnderline(){
        return fontUnderline;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .add("font", fontName)
            .add("size", fontSize)
            .add("color", fontColor)
            .toString();
    }


    @Override
    public boolean equals(Object obj){
        if (obj != null && obj instanceof SizedFont){
            SizedFont other = (SizedFont) obj;
            return Objects.equals(fontName, other.fontName) &&
                fontSize == other.fontSize &&
                Objects.equals(fontColor, other.fontColor);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(fontName, fontSize, fontColor);
    }

}