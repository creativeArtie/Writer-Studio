package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.jwriter.pdf.value.*;

import com.google.common.collect.*;

/**
 * Prints the title page footer
 */
class PdfMatterTitleBottom extends PdfMatter<DataTitle>{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfItem> outputLines;
    private float startX;
    private float startY;

    @Override
    protected void parseData(DataTitle data, StreamPdfFile output)
            throws IOException{
        baseMargins = data.getMargin();
        startY = baseMargins;
        startX = baseMargins;

        PDFont font = data.getBaseFontType();
        int size = data.getBaseFontSize();

        outputLines = data.getTitleBottomText(getWidth());
        for (PdfItem line: outputLines){
            startY += line.getHeight();
        }
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
    protected List<PdfItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }
}