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
    private static final int LINE = -15;

    private PDDocument outDoc;
    private PDPage curPage;
    private PDPageContentStream outStream;
    private String fileName;
    private float startX;
    private float startY;
    private float pageWidth;
    private float lineWidth;

    public PDFOutputStream(File file) throws IOException{
        fileName = file.getPath();
        outDoc = new PDDocument();
        PDPage page = new PDPage();

        PDRectangle mediabox = page.getMediaBox();

        pageWidth = mediabox.getWidth() - (2 * MARGIN);
        startX = mediabox.getLowerLeftX() + MARGIN;
        startY = mediabox.getUpperRightY() - MARGIN;
        lineWidth = 0f;
        newPage(page);
    }

    private void newPage(PDPage page) throws IOException{
        curPage = page;
        outDoc.addPage(curPage);
        outStream = new PDPageContentStream(outDoc, curPage);
        outStream.beginText();
        outStream.newLineAtOffset(startX, startY);
    }

    private void newLine() throws IOException{
        System.out.println();
        outStream.newLineAtOffset(0, LINE);
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
        print(text, font, 12);
    }

    public void print(String text, PDFont font, int size) throws IOException{
        printString(text, font, size);
    }

    public void println(String text, PDFont font) throws IOException{
        println(text, font, 12);
    }

    public void println(String text, PDFont font, int size) throws IOException{
        printString(text, font, size);
        newLine();
    }

    private void printString(String text, PDFont font, int size)
            throws IOException{
        outStream.setFont(font, size);
        List<String> lines = Splitter.on('\n').splitToList(text);
        if (lines.size() == 1){
            showText(lines.get(0), font, size);
        } else {
            for(String line: lines){
                showText(line, font, size);
                newLine();
            }
        }
    }

    private void showText(String line, PDFont font, int size) throws IOException{

        for(String text: Splitter.on(' ').omitEmptyStrings().split(line)){
            String out = text + ' ';
            float tmp = lineWidth + (size * font.getStringWidth(out) / 1000);
            if(tmp < pageWidth){
                lineWidth = tmp;
                System.out.println(lineWidth + "\t" + pageWidth + ":" + text);
                outStream.showText(out + " ");
            } else {
                newLine();
                System.out.println(lineWidth + "\t" + pageWidth + ":" + text);
                outStream.showText(out + " ");
            }
        }
    }
}