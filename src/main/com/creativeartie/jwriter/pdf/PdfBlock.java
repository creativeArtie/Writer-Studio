package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

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
    private boolean noEdited;
    private TextAlignment divAlignment;

    public PdfBlock(float width){
        this (width, TextAlignment.LEFT);
    }

    public PdfBlock(float width, TextAlignment alignment){
        divLines = new ArrayList<>();
        divLeading = 2;
        divFirstIndent = 0;
        divIndent = 0;
        divWidth = width;
        divAlignment = alignment;
        noEdited = false;
    }

    public PdfBlock setLeading(float leading){
        formatChanged();
        divLeading = leading;
        return this;
    }

    public PdfBlock setFirstIndent(float indent){
        formatChanged();
        divFirstIndent = indent;
        return this;
    }

    public PdfBlock setIndent(float indent){
        formatChanged();
        divIndent = indent;
        return this;
    }

    public float getHeight(){
        float ans = 0;
        for (PdfLine line: divLines){
            ans += line.getHeight();
        }
        return ans;
    }

    TextAlignment getTextAlignment(){
        return divAlignment;
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
        noEdited = true;
        return this;
    }

    public PdfBlock setTextAlignment(TextAlignment alignment){
        divAlignment = alignment;
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

    private void formatChanged(){
        // TODO redo adding text
        if (noEdited){
            throw new IllegalStateException("Format can not be change after text added.");
        }
    }
}