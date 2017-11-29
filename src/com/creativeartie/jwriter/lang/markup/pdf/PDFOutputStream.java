package com.creativeartie.jwriter.lang.markup.pdf;

import java.util.Optional;
import java.util.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.font.*;
import java.io.*;

import static com.google.common.base.Preconditions.*;
import com.google.common.base.*;

public final class PDFOutputStream implements AutoCloseable{
    private static final int TOP = 700;
    private static final int MARGIN = 40;
    private static final int FONT_SIZE = 12;

    private PDDocument outDoc;
    private PDPage curPage;
    private PDPageContentStream outStream;
    private String fileName;
    private final float startX;
    private final float startY;
    private final float pageWidth;
    private float pageHeight;
    private float lineWidth;
    private float lineHeight;

    public PDFOutputStream(File file, PDFont font) throws IOException{
        fileName = file.getPath();
        outDoc = new PDDocument();
        PDPage page = new PDPage();

        PDRectangle mediabox = page.getMediaBox();

        pageWidth = mediabox.getWidth() - (2 * MARGIN);
        startX = mediabox.getLowerLeftX() + MARGIN;
        startY = mediabox.getUpperRightY() - MARGIN;
        lineWidth = 0f;
        lineHeight = ((font.getFontDescriptor().getCapHeight()) / 1000 *
            FONT_SIZE) * 2;
        newPage(page);
    }

    private void newPage(PDPage page) throws IOException{
        pageHeight = startY;
        curPage = page;
        outDoc.addPage(curPage);
        outStream = new PDPageContentStream(outDoc, curPage);
        outStream.beginText();
        outStream.newLineAtOffset(startX, startY);
    }

    private void newLine(PDFont font) throws IOException{
        if (pageHeight - lineHeight < MARGIN){
            endPage();
            newPage(new PDPage());
            outStream.setFont(font, FONT_SIZE);
        }
        pageHeight -= lineHeight;
        outStream.newLineAtOffset(0, -lineHeight);
        lineWidth = 0f;
    }

    private void endPage() throws IOException{
        outStream.endText();
        outStream.close();
    }

    public void close() throws IOException{
        endPage();
        outDoc.save(fileName);
        outDoc.close();
    }


    public void print(String text, PDFont font) throws IOException{
        showLines(text, font);
    }

    public void println(PDFont font) throws IOException{
        newLine(font);
    }

    public void println(String text, PDFont font) throws IOException{
        showLines(text, font);
        newLine(font);
    }

    private void showLines(String text, PDFont font) throws IOException{
        outStream.setFont(font, FONT_SIZE);
        List<String> lines = Splitter.on('\n').splitToList(text);
        if (lines.size() == 1){
            showText(lines.get(0), font);
        } else {
            for(String line: lines){
                showText(line, font);
                newLine(font);
            }
        }
    }

    private void showLine(String line, PDFont font) throws IOException{
        int last = 0;
        int next = line.indexOf(' ') + 1;
        while (next != -1){
            print(line.substring(last, next), font);
            last = next + 1;
            next = line.indexOf(' ', last);
        }
        if (next < -1){
            print(line.substring(last), font);
        }
    }

    private void showText(String text, PDFont font) throws IOException{
        float tmp = lineWidth + (FONT_SIZE * font.getStringWidth(text) / 1000);
        if (tmp < pageWidth){
            lineWidth = tmp;
            outStream.showText(text);
        } else {
            newLine(font);
            newLine(font);
            outStream.showText(text);
        }
    }
}