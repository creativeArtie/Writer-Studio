package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class PdfMatter extends ForwardingList<PdfItem>{

    void render(PDPageContentStream output) throws IOException{
        output.beginText();
        StreamRender render = new StreamRender(output,
            getXLocation(), getYLocation(), getWidth());
        for (PdfItem block: this){
            render.changeAlign(block.getTextAlignment());
            for (PdfItem.Line line: block){
                // TODO change indent
                render.printText(line);
                render.nextLine(line.getHeight());
            }
        }
        output.endText();
    }

    abstract float getXLocation();
    abstract float getYLocation();
    abstract float getWidth();
}