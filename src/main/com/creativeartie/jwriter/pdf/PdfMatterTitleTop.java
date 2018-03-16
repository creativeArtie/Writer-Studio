package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Prints the title page header
 */
class PdfMatterTitleTop extends PdfMatterTitle{
    private Margin baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfItem> outputLines;
    private float startX;
    private float startY;

    @Override
    protected void parseData(InputTitle data, StreamData output)
            throws IOException{
        baseMargins = data.getMargin();

        startY = output.getHeight() - baseMargins.getTop();
        startX = baseMargins.getLeft();

        outputLines = data.getTitleTopText(output);
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