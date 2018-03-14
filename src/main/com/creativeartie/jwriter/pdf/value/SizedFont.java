package com.creativeartie.jwriter.pdf.value;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.font.*;

public class SizedFont{
    private PDFont textFont;
    private int textSize;

    public SizedFont(PDFont font, int size){
        textFont = font;
        textSize = size;
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