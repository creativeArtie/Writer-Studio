package com.creativeartie.jwriter.main;

import java.io.*;

import java.io.File;
import java.io.IOException;

import com.creativeartie.jwriter.output.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.creativeartie.jwriter.export.*;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class MainTest {

    public static void main(String args[]) throws IOException {
        File file = new File("data/pdf-long.txt");
        WritingText doc = new WritingText(file);
        ManuscriptFile use = ManuscriptFile.withManuscript(doc);

        File out = new File("test.pdf");
        try (PdfFileOutput output = new PdfFileOutput(out)){
            output.render(new OutputInfo(use));
        }
        // FileExporter.PDF_MANUSCRIPT.exportFile(use, out);
        /*
        Paragraph para = new Paragraph("Hello World!");
        createPdf(para, "test1.pdf");
        createPdf(para, "test2.pdf");*/
    }

    public static void createPdf(Paragraph para, String des) throws IOException {
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(des);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf);

        //Add paragraph to the document
        document.add(para);

        //Close document
        document.close();
    }
}