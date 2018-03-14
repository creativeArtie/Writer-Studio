package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

/**
 * Prints the page header.
 */
class PdfMatterHeader extends PdfMatter{

    private float baseMargins;
    private ArrayList<PdfItem> outputLines;
    private PdfItem currentLine;
    private float height;

    @Override
    float getWidth(){
        return 0f;
    }

    @Override
    public float getXLocation(){
        return baseMargins;
    }

    @Override
    public float getYLocation(){
        return baseMargins;
    }

    @Override
    protected List<PdfItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }
}