package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

/**
 * Prints the title page header
 */
class PdfAreaTitleHeader extends PdfArea{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfBlock> outputLines;
    private float startX;
    private float startY;

    public PdfAreaTitleHeader(DataTitle file, OutputPdfFile doc) throws IOException{
        super(file);
        baseMargins = file.getMargin();

        startY = doc.getPage().getMediaBox().getHeight() - baseMargins;
        startX = baseMargins;

        PDFont font = file.getBaseFontType();
        int size = file.getBaseFontSize();
        float width = doc.getPage().getMediaBox().getWidth() - (baseMargins * 2);

        outputLines = new ArrayList<>();
        for (String line: file.getTitleTopText()){
            outputLines.add(new PdfBlock(width).setLeading(1)
                .appendText(line, font, size));
        }

    }

    public float getRenderX(){
        if (outputLines.isEmpty()){
            return startX;
        }
        return outputLines.get(0).getRenderX();
    }

    @Override
    public float getXLocation(){
        return startX;
    }

    @Override
    public float getYLocation(){
        return startY;
    }


    @Override
    public List<PdfBlock> getOutputBlocks(){
        return outputLines;
    }
}