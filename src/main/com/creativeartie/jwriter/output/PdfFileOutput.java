package com.creativeartie.jwriter.output;

import java.io.*;

import com.creativeartie.jwriter.file.*;
import com.itextpdf.kernel.pdf.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

/// Format follow: https://www.scribophile.com/academy/how-to-format-a-novel-manuscript
public class PdfFileOutput implements Closeable{

    private ManuscriptFile fileInput;
    private PdfDocument pdfDocument;
    private PdfPageRender pageRender;


    public PdfFileOutput(File output) throws IOException{
        PdfWriter writer = new PdfWriter(output);
        pdfDocument = new PdfDocument(writer);
    }

    public void render(OutputInfo info){
        fileInput = info.getOutputDoc();
        new PdfTitleRender(info, this).render();
        PdfContentRender content = new PdfContentRender(info, this);
        for (Span span: info.getWritingText()){
            renderContent(content, (SectionSpan) span);
        }
        content.completed();
    }

    private void renderContent(PdfContentRender content, SectionSpan section){
        for (Span child: section){
            if (child instanceof LinedSpan){
                content.render((LinedSpan) child);
            } else if (child instanceof SectionSpan){
                renderContent(content, (SectionSpan) child);
            }
        }
    }

    PdfPage addNewPage(){
        return pdfDocument.addNewPage();
    }

    PdfDocument getPdfDocument(){
        return pdfDocument;
    }

    @Override
    public void close(){
        pdfDocument.close();
    }
}