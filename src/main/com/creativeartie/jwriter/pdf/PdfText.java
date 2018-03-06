package com.creativeartie.jwriter.pdf;

import java.util.*;
import java.io.*;
import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.base.*;

class PdfText{
    private static final String SPACE = " ";

    public static ArrayList<PdfText> createWords(String text, PDFont font,
            int size) throws IOException{

        PdfText space = new PdfText(SPACE, font, size, true);
        CharMatcher whitespace = CharMatcher.whitespace();

        ArrayList<PdfText> holder = new ArrayList<>();
        for (String word: Splitter.on(whitespace).omitEmptyStrings()
                .split(text)){
            holder.add(new PdfText(word, font, size, false));
        }
        int i = 0;
        ArrayList<PdfText> ans = new ArrayList<>();
        if (whitespace.indexIn(text) == 0){
            ans.add(space);
        }
        for (PdfText item: holder){
            ans.add(item);
            ans.add(space);
        }
        if (whitespace.lastIndexIn(text) == text.length() - 1){
            ans.add(space);
        }
        return ans;
    }

    private PDFont textFont;
    private int fontSize;
    private String outputText;
    private float textWidth;
    private float textHeight;
    private boolean spaceText;

    private PdfText(String word, PDFont font, int size, boolean space)
            throws IOException{
        outputText = word;
        textFont = font;
        fontSize = size;
        /// From https://stackoverflow.com/questions/13701017/calculation-string-width-in-pdfbox-seems-only-to-count-characters
        textWidth = textFont.getStringWidth(outputText) / 1000 * fontSize;
        /// From https://stackoverflow.com/questions/17171815/get-the-font-height-of-a-character-in-pdfbox
        textHeight = textFont.getFontDescriptor().getCapHeight() / 1000 * fontSize;
        spaceText = space;
    }

    public float getWidth() {
        return textWidth;
    }

    public float getHeight(){
        return textHeight;
    }

    public String getText(){
        return outputText;
    }

    public boolean isSpaceText(){
        return spaceText;
    }
}