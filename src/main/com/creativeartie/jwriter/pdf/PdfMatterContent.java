package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Prints text on a page with ability to change height and detect overflow.
 */
class PdfMatterContent extends PdfMatter{
    private Margin baseMargins;
    private ArrayList<PdfItem> outputLines;
    private PdfItem currentLine;

    private boolean baisicUnprepared;
    private float divWidth;
    private float divHeight;
    private float startY;
    private float startX;

    public PdfMatterContent(){
        baseMargins = null;
        outputLines = new ArrayList<>();
        currentLine = null;
        divWidth = 0;
        divHeight = 0;
        baisicUnprepared = true;
    }

    public PdfMatterContent setBasics(Input content, StreamPdfFile output){
        baseMargins = content.getMargin();
        startY = output.getPage().getMediaBox().getHeight() - baseMargins
            .getBottom();
        startX = baseMargins.getLeft();
        divWidth = content.getMargin().calcluateWidth(output.getPage());
        baisicUnprepared = false;
        return this;
    }

    public Optional<PdfItem> addContentLine(PdfItem item){
        outputLines.add(item);
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