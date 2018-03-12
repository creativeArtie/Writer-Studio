package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class PdfSection extends ForwardingList<PdfParagraph>{
    private float divWidth;

    public PdfSection(Data data, StreamPdfFile doc){
        divWidth = doc.getPage().getMediaBox().getWidth() - (data.getMargin() * 2);
    }

    public float getWidth(){
        return divWidth;
    }

    void render(PDPageContentStream output) throws IOException{
        output.beginText();
        StreamRender render = new StreamRender(output, getXLocation(),
            getYLocation(), divWidth);
        for (PdfParagraph block: this){
            render.changeAlign(block.getTextAlignment());
            for (PdfLine line: block){
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