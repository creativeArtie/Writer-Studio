package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

/**
 * Prints the title page header
 */
class PdfSectionTitleTop extends PdfSection{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfLine> outputLines;
    private float startX;
    private float startY;

    public PdfSectionTitleTop(DataTitle file, OutputPdfFile doc) throws IOException{
        super(file, doc);
        baseMargins = file.getMargin();

        startY = doc.getPage().getMediaBox().getHeight() - baseMargins;
        startX = baseMargins;

        PDFont font = file.getBaseFontType();
        int size = file.getBaseFontSize();

        outputLines = file.getTitleTopText(getWidth());
    }

    @Override
    public float getXLocation(){
        return startX;
    }

    @Override
    public float getYLocation(){
        return startY;
    }

    @Override
    public List<PdfLine> delegate(){
        return outputLines;
    }

}