package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;
import com.creativeartie.writerstudio.main.*;

/**
 * Defines the placement of the text on the page.
 */
final class StreamTextRender{
    private PDPageContentStream contentStream;
    private PDPage contentPage;
    private TextAlignment textAlignment;
    private SizedFont textFont;
    private float sectionWidth;
    private FormatterMatter renderMatter;
    private float localX;
    private float localY;
    private ArrayList<IOExceptionBiConsumer<PDPage, PDPageContentStream>> postText;

    public StreamTextRender(StreamPdfFile file, FormatterMatter matter)
            throws IOException{
        contentPage = file.getPage();
        contentStream = file.getContentStream();
        sectionWidth = matter.getWidth();
        renderMatter = matter;

        textAlignment = null;
        textFont = null;
        postText = new ArrayList<>();
    }

    void render() throws IOException{
        contentStream.beginText();
        moveText(renderMatter.getXLocation(), renderMatter.getYLocation());
        sectionWidth = renderMatter.getWidth();
        textAlignment = TextAlignment.LEFT;
        for (FormatterItem block: renderMatter){
            changeAlign(block.getTextAlignment());
            if (block.getPrefix().isPresent()){
                moveText(block.getPrefixDistance(), 0);
                contentStream.showText(block.getPrefix().get());
                moveText(-block.getPrefixDistance(), 0);
            }
            for (FormatterItem.Line line: block){
                moveText(line.getIndent(), 0);
                printText(line);
                nextLine(line.getHeight());
                moveText(-line.getIndent(), 0);
            }
        }
        contentStream.endText();
        textFont = null;
        for (IOExceptionBiConsumer<PDPage, PDPageContentStream> consumer: postText){
            consumer.acceptThrows(contentPage, contentStream);
        }
    }

    void changeAlign(TextAlignment next) throws IOException{
        if (textAlignment == next){
            return;
        }
        switch (textAlignment){
        case CENTER:
            switch(next){
            case RIGHT:
                moveText((sectionWidth / 2), 0);
                break;
            case CENTER:
                assert false;
                break;
            default:
                moveText(-(sectionWidth / 2), 0);
            }
            break;
        case RIGHT:
            switch(next){
            case RIGHT:
                assert false;
                break;
            case CENTER:
                moveText(-(sectionWidth / 2), 0);
                break;
            default:
                moveText(-sectionWidth, 0);
            }
            break;
        default:
            switch (next){
            case RIGHT:
                moveText(sectionWidth, 0);
                break;
            case CENTER:
                moveText(sectionWidth / 2, 0);
                break;
            default:
                moveText(0, 0);
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
        moveText(0, -y);
    }

    void printText(FormatterItem.Line line) throws IOException{
        if (textAlignment == TextAlignment.RIGHT){
            moveText(-line.getWidth(), 0);
        } else if (textAlignment == TextAlignment.CENTER){
            moveText(-(line.getWidth() / 2), 0);
        }
        float textLocalX = localX;
        for (FormatterData text: line){
            PDRectangle rect = new PDRectangle(textLocalX, localY,
                text.getWidth(), text.getHeight());
            changeFont(text.getFont());
            contentStream.showText(text.getText());
            postText.addAll(text.getPostTextConsumers(rect));
            textLocalX += text.getWidth();
        }
        if (textAlignment == TextAlignment.RIGHT){
            moveText(line.getWidth(), 0);
        } else if (textAlignment == TextAlignment.CENTER){
            moveText(line.getWidth() / 2, 0);
        }
    }

    private void changeFont(SizedFont font) throws IOException{
        if (textFont == null || ! font.equals(textFont)){
            contentStream.setNonStrokingColor(font.getColor());
            contentStream.setFont(font.getFont(), font.getSize());
            textFont = font;
        }
    }

    private void moveText(float x, float y) throws IOException{
        localX += x;
        localY += y;
        contentStream.newLineAtOffset(x, y);
    }

}