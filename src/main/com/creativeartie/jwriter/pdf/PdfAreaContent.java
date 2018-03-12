package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

/**
 * Defines the placement of the text on the page.
 *
class PdfAreaContent extends PdfArea{
    private float baseMargins;
    private PDPage outputPage;
    private TreeSet<PdfArea> sections;

    public PdfAreaContent(DataContent file){
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
    protected List<PdfBlock> delegate(){
        return new ArrayList<>();
    }
}*/