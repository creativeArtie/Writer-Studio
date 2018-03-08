package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.font.*;

/**
 * Decides if the text can be filled in a single line, or if it needs a second
 * line.
 */
class PdfLine{
    private ArrayList<PdfText> inputText;
    private float maxWidth;
    private float curWidth;
    private float textLeading;
    private float textHeight;

    public PdfLine(float width, float leading){
        this(width, leading, new ArrayList<>());
    }

    public PdfLine(float width, float leading, ArrayList<PdfText> text){
        inputText = text;
        maxWidth = width;
        textLeading = leading;
        textHeight = 0;
        curWidth = 0;
    }

    public ArrayList<PdfText> appendText(String string, PDFont font, int size)
            throws IOException{
        return appendText(PdfText.createWords(string, font, size));
    }

    public ArrayList<PdfText> appendText(ArrayList<PdfText> texts){
        ArrayList<PdfText> overflow = null;
        for (PdfText text: texts){
            if (overflow == null){
                if (curWidth + text.getWidth() > maxWidth){
                    overflow = new ArrayList<>();
                    int last = inputText.size() - 1;
                    if (! inputText.get(last).isSpaceText()){
                        overflow.add(inputText.remove(last));
                    }
                    overflow.add(text);
                    curWidth += text.getWidth();
                } else {
                    if (text.getHeight() > textHeight){
                        textHeight = text.getHeight();
                    }
                    if (! inputText.isEmpty() || ! text.isSpaceText()){
                        /// Don't a space in the begining of the text
                        inputText.add(text);
                    }
                }
            } else {
                overflow.add(text);
            }
        }
        return overflow == null? new ArrayList<>(): overflow;
    }

    public float getHeight(){
        return textHeight * textLeading;
    }

    public float getWidth(){
        return curWidth;
    }
}