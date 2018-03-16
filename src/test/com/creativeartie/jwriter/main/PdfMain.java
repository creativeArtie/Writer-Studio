package com.creativeartie.jwriter.main;

import java.io.*;

import java.io.File;
import java.io.IOException;

import com.creativeartie.jwriter.output.*;
import com.creativeartie.jwriter.pdf.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.creativeartie.jwriter.export.*;
/*
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
*/
public class PdfMain {

    public static void main(String args[]) throws IOException {
        File file = new File("data/pdf-long.txt");
        WritingText doc = new WritingText(file);
        ManuscriptFile use = ManuscriptFile.withManuscript(doc);

        try (StreamPdfFile output = new StreamPdfFile("test.pdf")){
            new PdfWriting().setData(new InputWriting(use), output).render();
        }

        /*File out = new File("test.pdf");
        try (PdfFileOutput output = new PdfFileOutput(out)){
            output.render(new OutputInfo(use));
        }
        // FileExporter.PDF_MANUSCRIPT.exportFile(use, out);
        /*
        Paragraph para = new Paragraph("Hello World!");
        createPdf(para, "test1.pdf");
        createPdf(para, "test2.pdf");*/
    }
}