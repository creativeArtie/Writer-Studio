package com.creativeartie.writerstudio.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;

public class StreamPdfFile implements AutoCloseable{
    private final String saveFile;
    private final PDDocument pdfDocument;
    private PDPageContentStream contentStream;
    private FormatterSectionTitle titlePage;
    private FormatterSectionContent writtenContent;
    private PDFont serifFont;
    private PDFont monoFont;

    private PDPage currentPage;

    public StreamPdfFile(String file) throws IOException{
        pdfDocument = new PDDocument();
        // TODO load font
        saveFile = file;
        newPage();
    }

    public StreamData newStreamData(){
        return new StreamData(){
            private int pageNumber = 1;
            @Override
            public int getPageNumber(){
                return pageNumber;
            }

            @Override
            public float getWidth(){
                return currentPage.getMediaBox().getWidth();
            }

            @Override
            public float getHeight(){
                return currentPage.getMediaBox().getHeight();
            }

            @Override
            public StreamData resetPageNumber(){
                pageNumber = 1;
                return this;
            }

            @Override
            public StreamData toNextPage(){
                pageNumber++;

                return this;
            }
        };
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

    PDPageContentStream getContentStream(){
        return contentStream;
    }

    StreamPdfFile renderText(FormatterMatter matter) throws IOException{
        if (! matter.isEmpty()){
            new StreamTextRender(this, matter).render();
        }
        return this;
    }

    PDPage getPage(){
        return currentPage;
    }

    @Override
    public void close() throws IOException{
        contentStream.close();
        pdfDocument.save(saveFile);
        pdfDocument.close();
    }
}