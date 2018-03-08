package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class PdfSection{
    private PDFont textFont;
    private int textSize;

    void render(PDPageContentStream output) throws IOException{
        output.beginText();
        output.newLineAtOffset(getX(), getY());
        for (PdfBlock block: getOutputBlocks()){
            block.render(output, this);
        }
        output.endText();
    }

    protected boolean setFont(PDFont font, int size){
        boolean update = false;
        if (textFont == null || ! textFont.equals(font)){
            update = true;
            textFont = font;
        }
        if (size != textSize){
            update = true;
            textSize = size;
        }
        return true;
    }


    abstract float getX();
    abstract float getY();
    abstract List<PdfBlock> getOutputBlocks();
}