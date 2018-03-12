package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Defines the placement of the text on the page.
 */
final class OutputRender{
    private PDPageContentStream contentStream;
    private TextAlignment textAlignment;
    private PDFont textFont;
    private int textSize;
    private float sectionWidth;

    public OutputRender(PDPageContentStream output, float x, float y, float w)
            throws IOException{
        contentStream = output;
        sectionWidth = w;

        textAlignment = TextAlignment.LEFT;
        textFont = PDType1Font.TIMES_ROMAN;
        textSize = 12;

        output.newLineAtOffset(x, y);
        output.setFont(textFont, textSize);
    }

    void changeAlign(TextAlignment next) throws IOException{
        if (textAlignment == next){
            return;
        }
        switch (textAlignment){
        case CENTER:
            switch(next){
            case RIGHT:
                contentStream.newLineAtOffset((sectionWidth / 2), 0);
                break;
            case CENTER:
                assert false;
                break;
            default:
                contentStream.newLineAtOffset(-(sectionWidth / 2), 0);
            }
            break;
        case RIGHT:
            switch(next){
            case RIGHT:
                assert false;
                break;
            case CENTER:
                contentStream.newLineAtOffset(-(sectionWidth / 2), 0);
                break;
            default:
                contentStream.newLineAtOffset(-sectionWidth, 0);
            }
            break;
        default:
            switch (next){
            case RIGHT:
                contentStream.newLineAtOffset(sectionWidth, 0);
                break;
            case CENTER:
                contentStream.newLineAtOffset(sectionWidth / 2, 0);
                break;
            default:
                assert false;
            }

        }
        textAlignment = next;
    }

    void nextLine(float y) throws IOException{
        float x;
        switch (textAlignment){
        case RIGHT:
            x = sectionWidth;
            break;
        default:
            x = 0;
        }
        contentStream.newLineAtOffset(0, -y);
    }

    void printText(PdfDiv line) throws IOException{
        if (textAlignment == TextAlignment.RIGHT){
            contentStream.newLineAtOffset(-line.getWidth(), 0);
        } else if (textAlignment == TextAlignment.CENTER){
            contentStream.newLineAtOffset(-(line.getWidth() / 2), 0);
        }
        for (PdfData text: line){
            changeFont(text.getFont(), text.getSize());
            contentStream.showText(text.getText());
        }
        if (textAlignment == TextAlignment.RIGHT){
            contentStream.newLineAtOffset(line.getWidth(), 0);
        } else if (textAlignment == TextAlignment.CENTER){
            contentStream.newLineAtOffset(line.getWidth() / 2, 0);
        }
    }

    void changeFont(PDFont font, int size) throws IOException{
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
}