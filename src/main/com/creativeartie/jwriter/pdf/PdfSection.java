package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class PdfSection extends ForwardingList<PdfLine>{
    private float divWidth;

    public PdfSection(Data data, OutputPdfFile doc){
        divWidth = doc.getPage().getMediaBox().getWidth() - (data.getMargin() * 2);
    }

    public float getWidth(){
        return divWidth;
    }

    void render(PDPageContentStream output) throws IOException{
        output.beginText();
        OutputRender render = new OutputRender(output, getXLocation(),
            getYLocation(), divWidth);
        for (PdfLine block: this){
            render.changeAlign(block.getTextAlignment());
            for (PdfDiv line: block){
                // TODO change indent
                render.printText(line);
                render.nextLine(line.getHeight());
            }
        }
        output.endText();
    }

    abstract float getXLocation();
    abstract float getYLocation();
}