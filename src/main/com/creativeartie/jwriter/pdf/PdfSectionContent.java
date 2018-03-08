package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

/**
 * Defines the placement of the text on the page.
 */
class PdfSectionContent extends PdfSection{
    private float baseMargins;
    private PDPage outputPage;
    private TreeSet<PdfSection> sections;

    public PdfSectionContent(){
        super();
    }

    void loadContent(DocumentData file, PdfDocument doc){
    }

    void render(PDPageContentStream output){}

    public float getX(){
        return baseMargins;
    }
    public float getY(){
        return baseMargins;
    }
}