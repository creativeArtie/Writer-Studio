package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Prints the title page header
 */
class FormatterMatterTitleTop extends FormatterMatterTitle{
    private Margin baseMargins;
    private List<FormatterItem> outputLines;
    private float divHeight;
    private float startX;
    private float startY;

    @Override
    protected void parseData(DataTitle data, StreamData output)
            throws IOException{
        baseMargins = data.getMargin();

        startY = output.getHeight() - baseMargins.getTop();
        startX = baseMargins.getLeft();

        outputLines = data.getTitleTopText(output);
        divHeight = 0;
        for (FormatterItem line: outputLines){
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
    public List<FormatterItem> delegate(){
        return outputLines;
    }

}