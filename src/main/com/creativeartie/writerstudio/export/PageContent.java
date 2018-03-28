package com.creativeartie.writerstudio.export;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.export.value.*;

public class PageContent implements AutoCloseable{

    private final PDPage contentPage;
    private final PDPageContentStream contentStream;
    private PageMargin pageMargin;

    PageContent(Section section) throws IOException{
        PDDocument doc = section.getPdfDocument();
        contentPage = new PDPage();
        doc.addPage(contentPage);
        contentStream = new PDPageContentStream(doc, contentPage);
        pageMargin = new PageMargin(Utilities.cmToPoint(3f));
    }

    public PDPage getPage(){
        return contentPage;
    }

    public PDPageContentStream getContentStream(){
        return contentStream;
    }

    public float getRenderWidth(){
        return getWidth() - pageMargin.getLeft() - pageMargin.getRight();
    }

    public float getWidth(){
        return contentPage.getMediaBox().getWidth();
    }

    public float getHeight(){
        return contentPage.getMediaBox().getHeight();
    }

    public float getStartX(){
        return pageMargin.getLeft();
    }

    public float getStartY(PageAlignment aligment, MatterArea area){
        float height = contentPage.getMediaBox().getHeight();
        switch (aligment){
            case TOP:
                return height - pageMargin.getTop();
            case MIDDLE:
                height /= 2;
                for (DivisionLine line: area){
                    height += line.getHeight() / 2;
                }
                return height;
            case BOTTOM:
                height = pageMargin.getBottom();
                for (DivisionLine line : area){
                    height += line.getHeight();
                }
                return height;
        }
        return 0;
    }

    @Override
    public void close() throws IOException{
        contentStream.close();
    }
}