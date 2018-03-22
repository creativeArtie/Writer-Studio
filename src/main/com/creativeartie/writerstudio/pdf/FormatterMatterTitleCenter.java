package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Prints the title page center text
 */
class FormatterMatterTitleCenter extends FormatterMatterTitle{
    private Margin baseMargins;
    private List<FormatterItem> outputLines;
    private float divHeight;
    private float startX;
    private float startY;

    @Override
    protected void parseData(DataTitle data, StreamData output)
            throws IOException{
        baseMargins = data.getMargin();

        startY = output.getHeight() / 2 ;
        startX = baseMargins.getLeft();

        outputLines = data.getTitleCenterText(output);
        divHeight = 0;
        for (FormatterItem block: outputLines){
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
    public float getStartY(){
        return startY;
    }

    @Override
    public List<FormatterItem> delegate(){
        return outputLines;
    }

}