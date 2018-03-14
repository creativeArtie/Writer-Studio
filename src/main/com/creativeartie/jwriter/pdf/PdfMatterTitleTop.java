package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

/**
 * Prints the title page header
 */
class PdfMatterTitleTop extends PdfMatterTitle<DataTitle>{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfItem> outputLines;
    private float startX;
    private float startY;

    @Override
    protected void parseData(DataTitle data, StreamPdfFile output)
            throws IOException{
        baseMargins = data.getMargin();

        startY = output.getPage().getMediaBox().getHeight() - baseMargins;
        startX = baseMargins;

        PDFont font = data.getBaseFontType();
        int size = data.getBaseFontSize();

        outputLines = data.getTitleTopText(getWidth());
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
    public List<PdfItem> delegate(){
        return outputLines;
    }

}