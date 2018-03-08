package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class PdfSection{

    private ArrayList<PdfBlock> childBlocks;

    public PdfSection(){
        childBlocks = new ArrayList<>();
    }

    abstract void loadContent(DocumentData file, PdfDocument doc);

    abstract void render(PDPageContentStream output) throws IOException;


    abstract float getX();
    abstract float getY();
}