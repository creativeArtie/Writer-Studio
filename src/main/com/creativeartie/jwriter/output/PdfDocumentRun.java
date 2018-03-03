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

    private int count;
    public PdfDocumentRun(float margin){
        addParagraphs = new ArrayList<>();
        pageMargins = new ArrayList<>();
        pageFootnotes = new ArrayList<>();
        count = 0;
        writeDoc = new PdfDocument(new PdfWriter("Test" + (count++) + ".pdf"));
        currentMargin = margin;
        currentDiv = new Div();
        new Canvas(canvas, doc, new Rectangle(
            0, 0,
            size.getWidth() - margin - margin, 0
        )).add(currentDiv);
        useDoc = new Document(doc);
    }

    public void close(){
        writeDoc.close();
    }
}