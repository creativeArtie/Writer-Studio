package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

/**
 * Prints the agent data
 */
class PdfSectionTitleHeader extends PdfSection{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfBlock> outputLines;
    private float startX;
    private float startY;

    public PdfSectionTitleHeader(float margins){
        super();
        baseMargins = margins;
    }

    void loadContent(DocumentData file, PdfDocument doc) throws IOException{
        startY = doc.getPage().getMediaBox().getHeight() - baseMargins;
        startX = baseMargins;


        PDFont font = PDType1Font.TIMES_ROMAN;
        float width = doc.getPage().getMediaBox().getWidth() - (baseMargins * 2);

        outputLines = new ArrayList<>();
        for (String line: file.getTitleTopText()){
            outputLines.add(new PdfBlock(width).setLeading(1)
                .appendText(line, font, 12));
        }

    }

    @Override
    public float getX(){
        return startX;
    }

    @Override
    public float getY(){
        return startY;
    }


    @Override
    public List<PdfBlock> getOutputBlocks(){
        return outputLines;
    }
}