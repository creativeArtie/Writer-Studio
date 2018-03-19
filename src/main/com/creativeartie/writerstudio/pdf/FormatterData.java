package com.creativeartie.writerstudio.pdf;

import java.util.*;
import java.io.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Decides the length of a text and if it need to be keep with the last text.
 */
final class FormatterData{
    private static final String SPACE = " ";

    public static ArrayList<FormatterData> createWords(String text, SizedFont font)
            throws IOException{

        FormatterData space = new FormatterData(SPACE, font, true);
        CharMatcher whitespace = CharMatcher.whitespace();

        ArrayList<FormatterData> holder = new ArrayList<>();
        for (String word: Splitter.on(whitespace).omitEmptyStrings()
                .split(text)){
            holder.add(new FormatterData(word, font, false));
        }
        int i = 0;
        boolean isFirst = true;
        ArrayList<FormatterData> ans = new ArrayList<>();
        if (whitespace.indexIn(text) == 0){
            ans.add(space);
        }
        for (FormatterData item: holder){
            if (isFirst){
                isFirst = false;
            } else {
                ans.add(space);
            }
            ans.add(item);
        }
        if (whitespace.lastIndexIn(text) == text.length() - 1){
            ans.add(space);
        }
        return ans;
    }

    private SizedFont textFont;
    private String outputText;
    private float textWidth;
    private float textHeight;
    private boolean spaceText;

    private FormatterData(String word, SizedFont font, boolean space)
            throws IOException{
        outputText = word;
        textFont = font;
        textWidth = textFont.getWidth(word);
        textHeight = textFont.getHeight();
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

    public SizedFont getFont(){
        return textFont;
    }

    public String toString(){
        return "\"" + outputText + "\"";
    }
}