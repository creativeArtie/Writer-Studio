package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

/**
 * Defines the placement of the text on the page.
 *
class PdfSectionContent extends PdfSection{
    private float baseMargins;
    private PDPage outputPage;
    private TreeSet<PdfSection> sections;

    public PdfSectionContent(DataContent file){
        super(file);
    }

    void render(PDPageContentStream output){}

    public float getRenderX(){
        return 0;
    }

    public float getXLocation(){
        return baseMargins;
    }

    public float getYLocation(){
        return baseMargins;
    }

    @Override
    protected List<PdfLine> delegate(){
        return new ArrayList<>();
    }
}*/