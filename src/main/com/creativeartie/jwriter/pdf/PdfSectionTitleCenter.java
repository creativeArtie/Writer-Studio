package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

/**
 * Prints the title page center text
 */
class PdfSectionTitleCenter extends PdfSection{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfParagraph> outputLines;
    private float startX;
    private float startY;

    public PdfSectionTitleCenter(DataTitle file, StreamPdfFile doc) throws IOException{
        super(file, doc);
        baseMargins = file.getMargin();

        startY = doc.getPage().getMediaBox().getHeight() / 2 ;
        startX = baseMargins;

        PDFont font = file.getBaseFontType();
        int size = file.getBaseFontSize();

        outputLines = file.getTitleCenterText(getWidth());
        for (PdfParagraph block: outputLines){
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
    public List<PdfParagraph> delegate(){
        return outputLines;
    }

}