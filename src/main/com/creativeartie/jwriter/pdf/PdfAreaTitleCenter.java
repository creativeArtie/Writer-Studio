package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

/**
 * Prints the title page center text
 */
class PdfAreaTitleCenter extends PdfArea{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfBlock> outputLines;
    private float startX;
    private float startY;

    public PdfAreaTitleCenter(DataTitle file, OutputPdfFile doc) throws IOException{
        super(file, doc);
        baseMargins = file.getMargin();

        startY = doc.getPage().getMediaBox().getHeight() / 2 ;
        startX = baseMargins;

        PDFont font = file.getBaseFontType();
        int size = file.getBaseFontSize();

        outputLines = file.getTitleCenterText(getWidth());
        for (PdfBlock block: outputLines){
            startY += block.getHeight() / 2 ;
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
    public List<PdfBlock> delegate(){
        return outputLines;
    }

}