package com.creativeartie.jwriter.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class OutputPdfFile implements AutoCloseable{
    private final String saveFile;
    private final PDDocument pdfDocument;
    private final PdfAreaTitleHeader titleTop;
    private final PdfAreaTitleCenter titleCenter;
    private final PdfAreaTitleFooter titleBottom;
    private PDPageContentStream contentStream;

    private PDPage currentPage;

    public OutputPdfFile(String file, DataWriting data) throws IOException{
        pdfDocument = new PDDocument();
        saveFile = file;
        newPage();
        titleTop = new PdfAreaTitleHeader(data.getTitleData(), this);
        titleCenter = new PdfAreaTitleCenter(data.getTitleData(), this);
        titleBottom = new PdfAreaTitleFooter(data.getTitleData(), this);
    }

    private OutputPdfFile newPage() throws IOException{
        currentPage = new PDPage();
        pdfDocument.addPage(currentPage);
        contentStream = new PDPageContentStream(pdfDocument, currentPage);
        return this;
    }

    OutputPdfFile addPage() throws IOException{
        contentStream.close();
        newPage();
        return this;
    }

    public OutputPdfFile render() throws IOException{
        titleTop.render(contentStream);
        titleCenter.render(contentStream);
        titleBottom.render(contentStream);
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