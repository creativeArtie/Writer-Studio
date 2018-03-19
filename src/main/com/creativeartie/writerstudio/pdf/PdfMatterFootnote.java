package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Prints the footnotes and do some preview of changes.
 */
class PdfMatterFootnote extends PdfMatter{
    private Margin baseMargins;
    private ArrayList<PdfItem> outputLines;
    private PdfItem currentLine;

    private boolean baisicUnprepared;
    private float divWidth;
    private float divHeight;
    private float startY;
    private float startX;

    public PdfMatterFootnote(){
        baseMargins = null;
        outputLines = new ArrayList<>();
        currentLine = null;
        divWidth = 0;
        divHeight = 0;
        baisicUnprepared = true;
    }

    public PdfMatterFootnote setBasics(Input content, StreamData output){
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

    public Optional<PdfItem> addContentLine(){
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
    protected List<PdfItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }
}