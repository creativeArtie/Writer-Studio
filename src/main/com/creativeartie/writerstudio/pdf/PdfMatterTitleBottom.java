package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

/**
 * Prints the title page footer
 */
class PdfMatterTitleBottom extends PdfMatterTitle{
    private Margin baseMargins;
    private PDPage outputPage;
    private float divHeight;
    private ArrayList<PdfItem> outputLines;
    private float startX;
    private float startY;

    @Override
    protected void parseData(InputTitle data, StreamData output)
            throws IOException{
        baseMargins = data.getMargin();
        startY = baseMargins.getBottom();
        startX = baseMargins.getLeft();

        outputLines = data.getTitleBottomText(output);
        divHeight = 0;
        for (PdfItem line: outputLines){
            startY += line.getHeight();
            divHeight += line.getHeight();
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
    protected List<PdfItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }
}