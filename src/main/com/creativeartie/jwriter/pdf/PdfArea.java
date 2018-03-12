package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class PdfArea {
    private PDPageContentStream contentStream;
    private PDFont textFont;
    private int textSize;
    private float textPointer;
    private float leftMargin;

    public PdfArea(Data data){
        leftMargin = data.getMargin();
        textPointer = 0;
    }

    void render(PDPageContentStream output) throws IOException{
        contentStream = output;
        output.beginText();
        textPointer = getRenderX();
        output.newLineAtOffset(textPointer + leftMargin, getYLocation());
        for (PdfBlock block: getOutputBlocks()){
            System.out.println(block);
            textPointer = block.render(output, this);
        }
        output.endText();
    }

    void setFont(PDFont font, int size) throws IOException{
        boolean update = false;
        if (textFont == null || ! textFont.equals(font)){
            update = true;
            textFont = font;
        }
        if (size != textSize){
            update = true;
            textSize = size;
        }
        if (update){
            contentStream.setFont(font, size);
        }
    }

    float getPointer(){
        return textPointer;
    }

    abstract float getRenderX();
    abstract float getXLocation();
    abstract float getYLocation();
    abstract List<PdfBlock> getOutputBlocks();
}