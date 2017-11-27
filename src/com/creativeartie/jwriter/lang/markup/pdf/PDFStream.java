package com.creativeartie.jwriter.lang.markup.pdf;

import java.util.Optional;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import java.io.*;

import static com.google.common.base.Preconditions.*;
import com.google.common.base.*;

public final class PDFStream implements AutoCloseable{
    private PDDocument outDoc;
    private PDPage curPage;
    private PDPageContentStream outStream;
    private String fileName;
    private PDFont textFont;
    private int lineCount;

    public PDFStream(String file) throws IOException{
        fileName = file;
        outDoc = new PDDocument();
        textFont = PDType1Font.HELVETICA_BOLD;
        lineCount = 0;
        startPage();
    }

    public PDFStream newPage() throws IOException{
        outStream.close();
        startPage();
        return this;
    }

    private PDFStream startPage() throws IOException{
        curPage = new PDPage();
        outDoc.addPage(curPage);
        outStream = new PDPageContentStream(outDoc, curPage);
        return this;
    }

    public class Line {
        private StringBuilder output;
        private PDFont textFont;
        private float lineWidth;
        private final float pageWidth;

        Line(PDFont font){
            pageWidth = curPage.getMediaBox().getWidth();
            textFont = font;
            output = new StringBuilder();
        }

        boolean addText(String text) throws IOException{
            float textWidth = textFont.getStringWidth(text);
            System.out.print(lineWidth + " + " + textWidth + " = ");
            System.out.print(lineWidth + textWidth);
            System.out.println("(" + pageWidth + ")");
            if (lineWidth + textWidth > pageWidth){
                return false;
            }
            output.append(text);
            lineWidth += textWidth;
            return true;
        }

        void showLine() throws IOException{
            System.out.println(output);
            lineCount++;
            outStream.beginText();
            outStream.setFont(textFont, 12);
            outStream.newLineAtOffset(100, 10 * lineCount);
            outStream.showText(output.toString());
            outStream.endText();
        }
    }

    public Line newLine() throws IOException{
        return new Line(textFont);
    }

    public void endPage() throws IOException{
        outStream.close();
    }

    public void close() throws IOException{
        endPage();
        outDoc.save(fileName);
        outDoc.close();
    }
}