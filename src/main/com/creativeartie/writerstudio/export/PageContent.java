package com.creativeartie.writerstudio.export;

import java.util.*;
import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.export.value.*;

public class PageContent implements AutoCloseable{

    private final PDPage contentPage;
    private final PDPageContentStream contentStream;
    private Optional<MatterArea> pageHeader;
    private PageMargin pageMargin;

    PageContent(Section section) throws IOException{
        PDDocument doc = section.getPdfDocument();
        contentPage = new PDPage();
        doc.addPage(contentPage);
        contentStream = new PDPageContentStream(doc, contentPage);
        pageMargin = new PageMargin(Utilities.cmToPoint(3f));
        pageHeader = Optional.empty();
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

    public float getRenderHeight(PageAlignment alignment){
        float base = getHeight() - pageMargin.getTop() - pageMargin.getBottom();
        switch (alignment){
        case CONTENT:
            return base - pageHeader.map(h -> h.getHeight()).orElse(0f);
        case THIRD:
            return base / 3 * 2;
        default:
            return base;
        }
    }
    public float getHeight(){
        return contentPage.getMediaBox().getHeight();
    }

    public PageContent setHeader(MatterArea header){
        pageHeader = Optional.of(header);
        return this;
    }

    public float getStartX(){
        return pageMargin.getLeft();
    }

    public float getStartY(PageAlignment alignment, MatterArea area){
        float height = contentPage.getMediaBox().getHeight();
        switch (alignment){
            case TOP:
                return height - pageMargin.getTop();
            case MIDDLE:
                height /= 2;
                for (Division line: area){
                    height += line.getHeight() / 2;
                }
                return height;
            case BOTTOM:
                height = pageMargin.getBottom();
                for (Division line : area){
                    height += line.getHeight();
                }
                return height;
            case CONTENT:
                return height - pageMargin.getTop() -
                    pageHeader.map(h -> h.getHeight()).orElse(0f);
            case THIRD:
                return height - ( height / 3);
        }
        return 0;
    }

    @Override
    public void close() throws IOException{
        contentStream.close();
    }
}