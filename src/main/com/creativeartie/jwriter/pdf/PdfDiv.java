package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.font.*;

class PdfDiv{
    private ArrayList<PdfLine> divLines;
    private float divWidth;
    private float divFirstIndent;
    private float divIndent;
    private float divLeading;

    public PdfDiv(float width){
        divLines = new ArrayList<>();
        divLeading = 2;
        divFirstIndent = 0;
        divIndent = 0;
        divWidth = width;
    }

    public PdfDiv setLeading(float leading){
        divLeading = leading;
        return this;
    }

    public PdfDiv setFirstIndent(float indent){
        divFirstIndent = indent;
        return this;
    }

    public PdfDiv setIndent(float indent){
        divIndent = indent;
        return this;
    }

    public PdfDiv appendText(String text, Font font, int size){
        PdfLine line;
        if (divLines.isEmpty()){
            line = new PdfLine(divWidth - divFirstIndent, divLeading)
            divLines.add(line);
        } else {
            line = divLines.get(divLines.size() - 1);
        }
        return this;
    }
}