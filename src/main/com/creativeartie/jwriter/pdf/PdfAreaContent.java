package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

/**
 * Defines the placement of the text on the page.
 */
class PdfAreaContent extends PdfArea{
    private float baseMargins;
    private PDPage outputPage;
    private TreeSet<PdfArea> sections;

    public PdfAreaContent(){
        super();
    }

    void render(PDPageContentStream output){}

    public float getX(){
        return baseMargins;
    }

    public float getY(){
        return baseMargins;
    }

    List<PdfBlock> getOutputBlocks(){
        return null;
    }
}