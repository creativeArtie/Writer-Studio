package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Defines the placement of the text on the page.
 */
final class StreamRender{
    private PDPageContentStream contentStream;
    private TextAlignment textAlignment;
    private SizedFont textFont;
    private float sectionWidth;

    public StreamRender(PDPageContentStream output, float x, float y, float w)
            throws IOException{
        contentStream = output;
        sectionWidth = w;

        textAlignment = TextAlignment.LEFT;
        textFont = new SizedFont(PDType1Font.TIMES_ROMAN, 12);

        output.newLineAtOffset(x, y);
        output.setFont(textFont.getFont(), textFont.getSize());
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

    void printText(PdfItem.Line line) throws IOException{
        if (textAlignment == TextAlignment.RIGHT){
            contentStream.newLineAtOffset(-line.getWidth(), 0);
        } else if (textAlignment == TextAlignment.CENTER){
            contentStream.newLineAtOffset(-(line.getWidth() / 2), 0);
        }
        for (PdfData text: line){
            changeFont(text.getFont());
            contentStream.showText(text.getText());
        }
        if (textAlignment == TextAlignment.RIGHT){
            contentStream.newLineAtOffset(line.getWidth(), 0);
        } else if (textAlignment == TextAlignment.CENTER){
            contentStream.newLineAtOffset(line.getWidth() / 2, 0);
        }
    }

    void changeFont(SizedFont font) throws IOException{
        if (! textFont.equals(font)){
            textFont = font;
            contentStream.setFont(font.getFont(), font.getSize());
        }
    }
}