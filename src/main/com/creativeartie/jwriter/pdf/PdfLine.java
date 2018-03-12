package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.collect.*;

/**
 * Decides if the text can be filled in a single line, or if it needs a second
 * line.
 */
class PdfLine extends ForwardingList<PdfData>{
    private ArrayList<PdfData> inputText;
    private float maxWidth;
    private float curWidth;
    private float textLeading;
    private float textHeight;

    public PdfLine(float width, float leading){
        this(width, leading, new ArrayList<>());
    }

    public PdfLine(float width, float leading, ArrayList<PdfData> text){
        inputText = text;
        maxWidth = width;
        textLeading = leading;
        textHeight = 0;
        curWidth = 0;
    }

    public ArrayList<PdfData> appendText(String string, PDFont font, int size)
            throws IOException{
        return appendText(PdfData.createWords(string, font, size));
    }

    public ArrayList<PdfData> appendText(ArrayList<PdfData> texts){
        // System.out.println(texts);
        ArrayList<PdfData> overflow = null;
        for (PdfData text: texts){
            // System.out.printf("%5.0f %5.0f, %5.0f\t", curWidth, text.getWidth(), maxWidth);
            // System.out.println(text);
            if (overflow == null){
                if (curWidth + text.getWidth() > maxWidth){

                    overflow = new ArrayList<>();
                    int last = inputText.size() - 1;
                    if (! inputText.get(last).isSpaceText()){
                        overflow.add(inputText.remove(last));
                    }
                    overflow.add(text);
                } else {
                    if (text.getHeight() > textHeight){
                        textHeight = text.getHeight();
                    }
                    if (! inputText.isEmpty() || ! text.isSpaceText()){
                        /// Don't a space in the begining of the text
                        inputText.add(text);
                        curWidth += text.getWidth();
                    }
                }
            } else {
                overflow.add(text);
            }
        }
        // System.out.println(curWidth);
        // System.out.println(overflow);
        return overflow == null? new ArrayList<>(): overflow;
    }

    public float getHeight(){
        return textHeight * textLeading;
    }

    public float getWidth(){
        return curWidth;
    }

    @Override
    protected List<PdfData> delegate(){
        return ImmutableList.copyOf(inputText);
    }
}