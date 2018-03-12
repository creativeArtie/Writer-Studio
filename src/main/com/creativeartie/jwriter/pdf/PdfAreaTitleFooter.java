package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.jwriter.pdf.value.*;


/**
 * Prints the title page footer
 */
class PdfAreaTitleFooter extends PdfArea{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<PdfBlock> outputLines;
    private float startX;
    private float startY;

    public PdfAreaTitleFooter(DataTitle file, OutputPdfFile doc) throws IOException{
        super(file);
        baseMargins = file.getMargin();
        startY = baseMargins;
        startX = baseMargins;


        PDFont font = file.getBaseFontType();
        int size = file.getBaseFontSize();
        float width = doc.getPage().getMediaBox().getWidth() - (baseMargins * 2);
        System.out.println(width);

        outputLines = new ArrayList<>();
        for (String line: file.getTitleBottomText()){
            PdfBlock add = new PdfBlock(width).setLeading(1)
                .setTextAlignment(TextAlignment.RIGHT)
                .appendText(line, font, size);
            startY += add.getHeight();
            outputLines.add(add);
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