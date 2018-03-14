package com.creativeartie.jwriter.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class PdfWriting {
    private PdfSectionTitle titlePage;
    private PdfSectionContent contentPages;

    public PdfWriting(){
        titlePage = new PdfSectionTitle();
        contentPages = new PdfSectionContent();
    }

    public PdfWriting setData(InputWriting data, StreamPdfFile output) throws
            IOException{
        titlePage.setData(data, output);
        contentPages.setData(data, output);
        return this;
    }

    public PdfWriting render() throws IOException{
        titlePage.render();
        contentPages.render();
        return this;
    }
}