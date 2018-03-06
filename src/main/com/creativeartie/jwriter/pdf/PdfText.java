package com.creativeartie.jwriter.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.font.*;

class PdfText{

    private PDFont textFont;
    private int fontSize;
    private String outputText;

    private PdfText(String word, PDFont font, int size){
        outputText = word;
        textFont = font;
        fontSize = size;
    }

    public float getWidth() throws IOException{
        /// From https://stackoverflow.com/questions/13701017/calculation-string-width-in-pdfbox-seems-only-to-count-characters
        return textFont.getStringWidth(outputText) / 1000 * fontSize;
    }

    public float getHeight(){
        /// From https://stackoverflow.com/questions/17171815/get-the-font-height-of-a-character-in-pdfbox
        return textFont.getFontDescriptor().getCapHeight() / 1000 * fontSize;
    }

    public String getText(){
        return outputText;
    }
}