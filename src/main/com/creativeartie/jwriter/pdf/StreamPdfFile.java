package com.creativeartie.jwriter.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class StreamPdfFile implements AutoCloseable{
    private final String saveFile;
    private final PDDocument pdfDocument;
    private PDPageContentStream contentStream;
    private PdfWritingTitle titlePage;

    private PDPage currentPage;

    public StreamPdfFile(String file, DataWriting data) throws IOException{
        pdfDocument = new PDDocument();
        saveFile = file;
        newPage();

        titlePage = new PdfWritingTitle(data, this);
    }

    private StreamPdfFile newPage() throws IOException{
        currentPage = new PDPage();
        pdfDocument.addPage(currentPage);
        contentStream = new PDPageContentStream(pdfDocument, currentPage);
        return this;
    }

    StreamPdfFile addPage() throws IOException{
        contentStream.close();
        newPage();
        return this;
    }

    public StreamPdfFile render() throws IOException{
        titlePage.render();
        return this;
    }

    PDPageContentStream getContentStream(){
        return contentStream;
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