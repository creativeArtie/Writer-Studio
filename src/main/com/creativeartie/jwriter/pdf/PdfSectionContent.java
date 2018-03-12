package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

/**
 * Defines the placement of the text on the page.
 */
class PdfSectionContent extends PdfSection{
    private float baseMargins;
    private ArrayList<PdfParagraph> outputLines;
    private PdfParagraph currentLine;
    private float height;

    public PdfSectionContent(DataContent data, StreamPdfFile file){
        super(data, file);
    }

    @Override
    public float getXLocation(){
        return baseMargins;
    }

    @Override
    public float getYLocation(){
        return baseMargins;
    }

    @Override
    protected List<PdfParagraph> delegate(){
        return ImmutableList.copyOf(outputLines);
    }
}