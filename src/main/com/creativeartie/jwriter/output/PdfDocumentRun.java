package com.creativeartie.jwriter.output;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.Optional;

import com.google.common.base.*;

import com.itextpdf.layout.element.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.events.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;

class PdfDocumentRun{

    private class PageMargins{
        private ArrayList<Float> pageMargins;
        private Optional<Float> currentMargin;
        private float defaultMargin;
        private int marginPointer;
        private boolean isLastPage;

        PageMargins(float margin){
            pageMargins = new ArrayList<>();
            currentMargin = Optional.empty();
            defaultMargin = margin;
            marginPointer = 0;
            isLastPage = true;
        }

        void restart(){
            marginPointer = 0;
            isLastPage = false;
        }

        void addMargin(){
            pageMargins.add(currentMargin.orElse(defaultMargin));
            currentMargin = Optional.empty();
        }

        void setMargin(IBlockElement div){
            currentMargin = Optional.of(PdfPageRender.getElementHeight(
                pageFootnotes.get(pageFootnotes.size() -1), defaultMargin));
        }

        boolean isLastPage(){
            return isLastPage;
        }

        float nextMargin(){
            if (marginPointer < pageMargins.size()){
                return pageMargins.get(marginPointer++);
            }
            marginPointer++;
            isLastPage = true;
            return currentMargin.orElse(defaultMargin);
        }
    }


    private ArrayList<IBlockElement> addParagraphs;
    private ArrayList<Div> pageFootnotes;
    private PdfDocument writeDoc;
    private Document useDoc;
    private PageMargins pageMargins;

    private int count;
    public PdfDocumentRun(float margin){
        addParagraphs = new ArrayList<>();
        pageMargins = new PageMargins(margin);
        pageFootnotes = new ArrayList<>();
        pageFootnotes.add(new Div());
        count = 0;
        try {
            writeDoc = new PdfDocument(new PdfWriter("Test" + (count++) + ".pdf"));
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
        PdfPage page = writeDoc.addNewPage();
        useDoc = new Document(writeDoc);
        addListener();
    }

    private void addListener(){
        writeDoc.addEventHandler(PdfDocumentEvent.END_PAGE, evt -> endPage());
    }

    private void endPage(){
        float margin = pageMargins.nextMargin();
        if (pageMargins.isLastPage()){
            pageMargins.addMargin();
            pageFootnotes.add(new Div());
        }
        System.out.println(count + "\t" + margin + "\t" +
            pageMargins.marginPointer + " " + pageMargins.pageMargins);
        useDoc.setBottomMargin(margin);
    }

    public void close(){
        writeDoc.close();
    }

    public void addParagraph(Paragraph para){
        addParagraphs.add(para);
        useDoc.add(para);
    }

    public void addFootnote(Paragraph para){
        Div last = pageFootnotes.get(pageFootnotes.size() - 1);
        last.add(para);
        writeDoc.close();

        pageMargins.restart();
        pageMargins.setMargin(last);
        try {
            writeDoc = new PdfDocument(new PdfWriter("Test" + (count++) + ".pdf"));
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
        useDoc = new Document(writeDoc);
        writeDoc.addEventHandler(PdfDocumentEvent.END_PAGE, evt -> endPage());
        for (IBlockElement line: addParagraphs){
            useDoc.add(line);
        }
    }

    public float marginOnPage(int page){
        if (page < pageMargins.pageMargins.size()){
            return pageMargins.pageMargins.get(page);
        }
        return pageMargins.currentMargin.orElse(pageMargins.defaultMargin);
    }

    public Div footnoteOnPage(int page){
        if (page < pageFootnotes.size()){
            return pageFootnotes.get(page);
        }
        return pageFootnotes.get(pageFootnotes.size() - 1);
    }

    public ArrayList<IBlockElement> getContent(){
        return addParagraphs;
    }
}