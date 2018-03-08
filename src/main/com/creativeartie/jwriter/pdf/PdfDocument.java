package com.creativeartie.jwriter.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class PdfDocument implements AutoCloseable{
    private final String saveFile;
    private final PDDocument pdfDocument;
    private final PdfSectionTitleHeader titleTop;
    private PDPageContentStream contentStream;

    private PDPage currentPage;

    public PdfDocument(String file) throws IOException{
        pdfDocument = new PDDocument();
        saveFile = file;
        newPage();
        titleTop = new PdfSectionTitleHeader(72);
    }

    public PdfDocument loadContent(DocumentData data){
        titleTop.loadContent(data, this);
        return this;
    }

    private PdfDocument newPage() throws IOException{
        currentPage = new PDPage();
        pdfDocument.addPage(currentPage);
        contentStream = new PDPageContentStream(pdfDocument, currentPage);
        return this;
    }

    PdfDocument addPage() throws IOException{
        contentStream.close();
        return this;
    }

    public PdfDocument render() throws IOException{
        titleTop.render(contentStream);
        return this;
    }

    public PDPage getPage(){
        return currentPage;
    }

    @Override
    public void close() throws IOException{
        contentStream.close();
        pdfDocument.save(saveFile);
        pdfDocument.close();
    }
}