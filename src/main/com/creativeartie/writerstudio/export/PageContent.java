package com.creativeartie.writerstudio.export;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.export.value.*;

public class PageContent implements AutoCloseable{

    private final PDPage contentPage;
    private final PDPageContentStream contentStream;

    PageContent(PDDocument doc) throws IOException{
        contentPage = new PDPage();
        doc.addPage(contentPage);
        contentStream = new PDPageContentStream(doc, contentPage);
    }

    public MatterArea newArea(PageAlignment alignment){
        switch(alignment){
            case TOP:
            case MIDDLE:
            case BOTTOM:
        }
    }

    @Override
    public void close() throws IOException{
        contentStream.close();
    }
}