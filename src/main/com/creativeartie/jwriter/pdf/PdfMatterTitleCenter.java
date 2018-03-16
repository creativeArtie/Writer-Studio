package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Prints the title page center text
 */
class PdfMatterTitleCenter extends PdfMatterTitle{
    private Margin baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfItem> outputLines;
    private float divHeight;
    private float startX;
    private float startY;

    @Override
    protected void parseData(InputTitle data, StreamData output)
            throws IOException{
        baseMargins = data.getMargin();

        startY = output.getHeight() / 2 ;
        startX = baseMargins.getLeft();

        outputLines = data.getTitleCenterText(output);
        divHeight = 0;
        for (PdfItem block: outputLines){
            startY += block.getHeight() / 2 ;
            divHeight += block.getHeight();
        }
    }

    @Override
    public float getHeight(){
        return divHeight;
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