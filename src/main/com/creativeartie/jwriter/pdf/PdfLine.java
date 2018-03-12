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
class PdfLine extends ForwardingList<PdfDiv>{
    private ArrayList<PdfDiv> divLines;
    private float divWidth;
    private float divFirstIndent;
    private float divIndent;
    private float divLeading;
    private boolean noEdited;
    private TextAlignment divAlignment;

    public PdfLine(float width){
        this (width, TextAlignment.LEFT);
    }

    public PdfLine(float width, TextAlignment alignment){
        divLines = new ArrayList<>();
        divLeading = 2;
        divFirstIndent = 0;
        divIndent = 0;
        divWidth = width;
        divAlignment = alignment;
        noEdited = false;
    }

    public PdfLine setLeading(float leading){
        formatChanged();
        divLeading = leading;
        return this;
    }

    public PdfLine setFirstIndent(float indent){
        formatChanged();
        divFirstIndent = indent;
        return this;
    }

    public PdfLine setIndent(float indent){
        formatChanged();
        divIndent = indent;
        return this;
    }

    public float getHeight(){
        float ans = 0;
        for (PdfDiv line: divLines){
            ans += line.getHeight();
        }
        return ans;
    }

    TextAlignment getTextAlignment(){
        return divAlignment;
    }

    public PdfLine appendText(String text, PDFont font, int size)
            throws IOException{
        PdfDiv line;
        if (divLines.isEmpty()){
            line = new PdfDiv(divWidth - divFirstIndent, divLeading);
            divLines.add(line);
        } else {
            line = divLines.get(divLines.size() - 1);
        }
        appendText(line.appendText(text, font, size));
        noEdited = true;
        return this;
    }

    public PdfLine setTextAlignment(TextAlignment alignment){
        divAlignment = alignment;
        return this;
    }

    private void appendText(ArrayList<PdfData> overflow){
        if (overflow.isEmpty()) return;
        PdfDiv line = new PdfDiv(divWidth - divIndent, divLeading);
        divLines.add(line);
        appendText(line.appendText(overflow));
    }

    @Override
    protected List<PdfDiv> delegate(){
        return ImmutableList.copyOf(divLines);
    }

    private void formatChanged(){
        // TODO redo adding text
        if (noEdited){
            throw new IllegalStateException("Format can not be change after text added.");
        }
    }
}