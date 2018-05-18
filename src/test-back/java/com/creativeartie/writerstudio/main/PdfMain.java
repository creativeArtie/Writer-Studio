package com.creativeartie.writerstudio.main;

import java.io.*;

import java.io.File;
import java.io.IOException;

import com.creativeartie.writerstudio.pdf.*;
import com.creativeartie.writerstudio.lang.markup.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.google.common.base.CharMatcher;

public class PdfMain {

    public static void main(String args[]) throws IOException {
        exportTest();
        // superscriptSupportTest();
    }

    private static void exportTest() throws IOException {

        File file = new File("build/resources/test/pdf-long.txt");
        WritingFile use = WritingFile.newSampleFile(file);
        try (WritingExporter out = new WritingExporter("test.pdf")){
            out.export(use);
        }
    }

    private static void superscriptSupportTest() throws IOException{
        PDDocument pdfDocument = new PDDocument();
        PDPage currentPage = new PDPage();
        pdfDocument.addPage(currentPage);
        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, currentPage);


        PDFont font = PDType0Font.load(pdfDocument, PdfMain.class.getResourceAsStream(
            "/data/fonts/FreeSerif.ttf"));

        contentStream.beginText();
        contentStream.newLineAtOffset(100, 50);
        contentStream.setFont(font, 12);
        contentStream.showText("abc⁰¹²³⁴⁵⁶⁷⁸⁹ⁱᵛˣˡᶜᵈᵐ");
        contentStream.close();
        pdfDocument.save("test.pdf");
        pdfDocument.close();
    }
}