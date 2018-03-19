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
final class StreamTextRender{
    private PDPageContentStream contentStream;
    private TextAlignment textAlignment;
    private SizedFont textFont;
    private float sectionWidth;
    private FormatterMatter renderMatter;

    public StreamTextRender(PDPageContentStream output, FormatterMatter matter)
            throws IOException{
        contentStream = output;
        sectionWidth = matter.getWidth();
        renderMatter = matter;

        textAlignment = matter.get(0).getTextAlignment();
        textFont = matter.get(0).get(0).get(0).getFont();
    }


    void render() throws IOException{
        contentStream.beginText();
        contentStream.newLineAtOffset(renderMatter.getXLocation(),
            renderMatter.getYLocation());
        contentStream.setFont(textFont.getFont(), textFont.getSize());
        sectionWidth = renderMatter.getWidth();
        textAlignment = TextAlignment.LEFT;
        for (FormatterItem block: renderMatter){
            changeAlign(block.getTextAlignment());
            if (block.getPrefix().isPresent()){
                contentStream.newLineAtOffset(block.getPrefixDistance(), 0);
                contentStream.showText(block.getPrefix().get());
                contentStream.newLineAtOffset(-block.getPrefixDistance(), 0);
            }
            for (FormatterItem.Line line: block){
                contentStream.newLineAtOffset(line.getIndent(), 0);
                printText(line);
                nextLine(line.getHeight());
                contentStream.newLineAtOffset(-line.getIndent(), 0);
            }
        }
        contentStream.endText();
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

    void printText(FormatterItem.Line line) throws IOException{
        if (textAlignment == TextAlignment.RIGHT){
            contentStream.newLineAtOffset(-line.getWidth(), 0);
        } else if (textAlignment == TextAlignment.CENTER){
            contentStream.newLineAtOffset(-(line.getWidth() / 2), 0);
        }
        for (FormatterData text: line){
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