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
    private ArrayList<Paragraph> addParagraphs;
    private ArrayList<Div> pageFootnotes;
    private ArrayList<Float> pageMargins;
    private float currentMargin;
    private Div currentDiv;
    private PdfDocument writeDoc;
    private Document useDoc;
    private float pageMargin;

    private int count;
    public PdfDocumentRun(float margin){
        addParagraphs = new ArrayList<>();
        pageMargins = new ArrayList<>();
        pageFootnotes = new ArrayList<>();
        count = 0;
        try {
        writeDoc = new PdfDocument(new PdfWriter("Test" + (count++) + ".pdf"));
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
        PdfPage page = writeDoc.addNewPage();
        currentMargin = margin;
        currentDiv = new Div();
        pageMargin = margin;
        useDoc = new Document(writeDoc);
    }

    public void close(){
        writeDoc.close();
    }
    
    public void addParagraph(Paragraph para){
        addParagraphs.add(para);
        useDoc.add(para);
        
    }
    
    public void addFootnote(Paragraph para){
        currentDiv.add(para);
        System.out.println(PdfPageRender.getElementHeight(currentDiv, pageMargin));
        
    }
}