package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

/**
 * Prints the title page footer
 */
class FormatterMatterTitleBottom extends FormatterMatterTitle{
    private Margin baseMargins;
    private float divHeight;
    private ArrayList<FormatterItem> outputLines;
    private float startX;
    private float startY;

    @Override
    protected void parseData(DataTitle data, StreamData output)
            throws IOException{
        baseMargins = data.getMargin();
        startY = baseMargins.getBottom();
        startX = baseMargins.getLeft();

        outputLines = data.getTitleBottomText(output);
        divHeight = 0;
        for (FormatterItem line: outputLines){
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
    public float getStartY(){
        return startY;
    }

    @Override
    protected List<FormatterItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }
}