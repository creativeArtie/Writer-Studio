package com.creativeartie.writer.pdf.value;

import java.io.*; // IOException

import org.apache.pdfbox.pdmodel.*; // PDPage, PDPageContentStream

public interface ContentPostEditor{
    public void edit(PDPage page, PDPageContentStream stream) throws IOException;
}