package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Prints the page header.
 */
class FormatterMatterHeader extends FormatterMatter{
    private Margin baseMargins;
    private ArrayList<FormatterItem> outputLines;
    private FormatterItem currentLine;

    private boolean baisicUnprepared;
    private float divWidth;
    private float divHeight;
    private float startY;
    private float startX;
    private float pageNumber;

    public FormatterMatterHeader(){
        baseMargins = null;
        outputLines = new ArrayList<>();
        currentLine = null;
        divWidth = 0;
        divHeight = 0;
        baisicUnprepared = true;
    }

    public FormatterMatterHeader setBasics(DataContent content, StreamData output)
            throws IOException{
        baseMargins = content.getMargin();
        startY = output.getHeight() - baseMargins.getTop();
        startX = baseMargins.getLeft();
        divWidth = output.getRenderWidth(baseMargins);

        outputLines.addAll(content.getHeader(output));
        divHeight = 0;
        for (FormatterItem item : outputLines){
            divHeight += item.getHeight();
        }

        baisicUnprepared = false;
        return this;
    }

    @Override
    public float getHeight(){
        return divHeight;
    }

    public Optional<FormatterItem> addContentLine(){
        return Optional.empty();
    }

    private void isReady(){
        if (baisicUnprepared){
            throw new IllegalStateException("addBasics(...) had not be called");
        }
    }

    @Override
    float getWidth(){
        isReady();
        return divWidth;
    }

    @Override
    public float getXLocation(){
        isReady();
        return startX;
    }

    @Override
    public float getYLocation(){
        isReady();
        return startY;
    }

    @Override
    protected List<FormatterItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }
}