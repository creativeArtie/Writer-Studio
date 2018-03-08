package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.collect.*;

/**
 * Represent a single line of writing text, like paragraph, list item, text box,
 * etc.
 */
class PdfBlock extends ForwardingList<PdfLine>{
    private ArrayList<PdfLine> divLines;
    private float divWidth;
    private float divFirstIndent;
    private float divIndent;
    private float divLeading;

    public PdfBlock(float width){
        divLines = new ArrayList<>();
        divLeading = 2;
        divFirstIndent = 0;
        divIndent = 0;
        divWidth = width;
    }

    public PdfBlock setLeading(float leading){
        divLeading = leading;
        return this;
    }

    public PdfBlock setFirstIndent(float indent){
        divFirstIndent = indent;
        return this;
    }

    public PdfBlock setIndent(float indent){
        divIndent = indent;
        return this;
    }

    public PdfBlock appendText(String text, PDFont font, int size)
            throws IOException{
        PdfLine line;
        if (divLines.isEmpty()){
            line = new PdfLine(divWidth - divFirstIndent, divLeading);
            divLines.add(line);
        } else {
            line = divLines.get(divLines.size() - 1);
        }
        appendText(line.appendText(text, font, size));
        return this;
    }

    private void appendText(ArrayList<PdfText> overflow){
        if (overflow.isEmpty()) return;
        PdfLine line = new PdfLine(divWidth - divIndent, divLeading);
        divLines.add(line);
        appendText(line.appendText(overflow));
    }

    @Override
    protected List<PdfLine> delegate(){
        return ImmutableList.copyOf(divLines);
    }
}