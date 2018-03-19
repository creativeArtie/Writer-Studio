package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Prints the footnotes and do some preview of changes.
 */
class FormatterMatterFootnote extends FormatterMatter{
    private Margin baseMargins;
    private ArrayList<FormatterItem> outputLines;
    private FormatterItem currentLine;

    private boolean baisicUnprepared;
    private float divWidth;
    private float divHeight;
    private float startY;
    private float startX;

    public FormatterMatterFootnote(){
        baseMargins = null;
        outputLines = new ArrayList<>();
        currentLine = null;
        divWidth = 0;
        divHeight = 0;
        baisicUnprepared = true;
    }

    public FormatterMatterFootnote setBasics(Data content, StreamData output){
        baseMargins = content.getMargin();
        startY = output.getHeight() - baseMargins.getBottom();
        startX = baseMargins.getLeft();
        divWidth = output.getRenderWidth(baseMargins);
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