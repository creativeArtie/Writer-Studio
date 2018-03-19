package com.creativeartie.writerstudio.pdf.value;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.font.*;

public final class SizedFont{

    private static final String TIMES_ROMAN = "times";
    private static final String COURIER = "courier";

    public enum Style {
        BOLD, ITALICS, BOTH, NONE;
    }

    public static SizedFont newTimesRoman(int size){
        return newTimesRoman(size, Style.NONE);
    }

    public static SizedFont newTimesRoman(int size, Style style){
        return new SizedFont(getTimesRoman(style), size, TIMES_ROMAN, style);
    }

    private static PDFont getTimesRoman(Style style){
        switch (style){
        case BOLD:
            return PDType1Font.TIMES_BOLD;
        case BOTH:
            return PDType1Font.TIMES_BOLD_ITALIC;
        case ITALICS:
            return PDType1Font.TIMES_ITALIC;
        default:
            return PDType1Font.TIMES_ROMAN;
        }
    }

    public static SizedFont newCourier(int size){
        return newCourier(size, Style.NONE);
    }

    public static SizedFont newCourier(int size, Style style){
        return new SizedFont(getCourier(style), size, COURIER, style);
    }

    private static PDFont getCourier(Style style){
        switch (style){
        case BOLD:
            return PDType1Font.COURIER_BOLD;
        case BOTH:
            return PDType1Font.COURIER_BOLD_OBLIQUE;
        case ITALICS:
            return PDType1Font.COURIER_OBLIQUE;
        default:
            return PDType1Font.COURIER;
        }
    }

    private final PDFont textFont;
    private final int textSize;
    private final String fontName;
    private final Style fontStyle;

    private SizedFont(PDFont font, int size, String name, Style style){
        textFont = font;
        textSize = size;
        fontName = name;
        fontStyle = style;
    }

    public PDFont getFont(){
        return textFont;
    }

    public int getSize(){
        return textSize;
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

    public SizedFont changeStyle(Style style){
        switch (fontName){
        case TIMES_ROMAN:
            return newTimesRoman(textSize, style);
        default:
            return newCourier(textSize, style);
        }
    }

    public SizedFont changeSize(int size){
        return new SizedFont(textFont, size, fontName, fontStyle);
    }

    public SizedFont changeToTime(){
        return newTimesRoman(textSize, fontStyle);
    }

    public SizedFont changeToCourier(){
        return newTimesRoman(textSize, fontStyle);
    }

    @Override
    public boolean equals(Object obj){
        if (obj != null && obj instanceof SizedFont){
            SizedFont other = (SizedFont) obj;
            return textFont.equals(other.textFont) &&
                textSize == other.textSize;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(textFont, textSize);
    }

}