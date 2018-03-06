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

    private ArrayList<IBlockElement> addParagraphs;
    private ArrayList<PdfPageLayout> pageFootnotes;
    private PdfDocument writeDoc;
    private Document useDoc;
    private float pageMargin;
    private int pageNumber;

    private int count;
    public PdfDocumentRun(float margin){
        addParagraphs = new ArrayList<>();
        pageMargin = margin;
        pageFootnotes = new ArrayList<>();
        pageFootnotes.add(new PdfPageLayout(margin));
        pageFootnotes.add(new PdfPageLayout(margin));
        pageNumber = 0;

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

    PdfPageLayout getPage(int page){
        return pageFootnotes.get(page);
    }

    private void addListener(){
        writeDoc.addEventHandler(PdfDocumentEvent.END_PAGE, evt -> endPage());
    }

    private void endPage(){
        if (pageNumber == pageFootnotes.size()){
            pageFootnotes.add(new PdfPageLayout(pageMargin));
        }

        PdfPageLayout page = pageFootnotes.get(pageNumber);
        System.out.println(count + "\t" + pageNumber + "\t" + pageFootnotes);
        useDoc.setBottomMargin(page.getMargin());
        pageNumber++;
    }

    public void close(){
        writeDoc.close();
    }

    public void addParagraph(Paragraph para){
        addParagraphs.add(para);
        useDoc.add(para);
    }

    public void addFootnote(Paragraph para){
        pageFootnotes.get(pageFootnotes.size() - 1).addFootnote(para);
        writeDoc.close();

        pageNumber = 0;
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
        if (page < 0){
            return pageMargin;
        }
        if (page < pageFootnotes.size()){
            return pageFootnotes.get(page).getMargin();
        }
        return pageMargin;
    }

    public Div footnoteOnPage(int page){
        if (page < 0){
            return new Div();
        }
        if (page < pageFootnotes.size()){
            return pageFootnotes.get(page).getFootnoteDiv();
        }
        return new Div();
    }

    public ArrayList<IBlockElement> getContent(){
        return addParagraphs;
    }
}