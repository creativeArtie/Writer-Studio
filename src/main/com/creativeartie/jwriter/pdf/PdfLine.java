package com.creativeartie.jwriter.pdf;

import java.util.*;

import org.apache.pdfbox.pdmodel.font.*;

class PdfLine{
    private ArrayList<PdfText> inputText;
    private float pageWidth;
    private int firstLineIndent;
    private int baseIndent;

    public PdfLine(float width){
        inputText = new ArrayList<>();
        pageWidth = width;
    }
}