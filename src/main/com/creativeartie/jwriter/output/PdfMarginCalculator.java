package com.creativeartie.jwriter.output;

import java.io.*;
import java.util.*;

import com.creativeartie.jwriter.file.*;
import com.itextpdf.kernel.events.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.itextpdf.layout.renderer.*;
import com.itextpdf.layout.property.*;

public class PdfMarginCalculator{

    private class CustomDocumentRender extends DocumentRenderer{

        private CustomDocumentRender(Document document){
            super(document);
        }

        @Override
        public void addChild(IRenderer renderer) {
            super.addChild(renderer);
            System.out.println(getPropertyAsBoolean(Property.FULL));
        }
    }
    private PdfDocument lastDoc;
    private Document testDoc;
    private float baseMargin;
    private ArrayList<Paragraph> lastParagraphs;
    private ArrayList<Paragraph> currentParagraphs;
    private ArrayList<Float> bottomMargins;

    public PdfMarginCalculator(float margin){
        baseMargin = margin;
        lastParagraphs = new ArrayList<>();
        currentParagraphs = new ArrayList<>();
        bottomMargins = new ArrayList<>();
        bottomMargins.add(baseMargin);

        lastDoc = createTestDocument();
        newPageHandler(lastDoc);
        testDoc = new Document(lastDoc);
        testDoc.setRenderer(new CustomDocumentRender(testDoc));
    }

    private int count = 0;
    private PdfDocument createTestDocument(){
        if (lastDoc != null) lastDoc.close();
        try {
            PdfDocument doc = new PdfDocument(new PdfWriter(
                "Test" + (count++) + ".pdf"));
            doc.addNewPage();
            // new ByteArrayOutputStream()));
            return doc;
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void newPageHandler(PdfDocument doc){
        doc.addEventHandler(PdfDocumentEvent.END_PAGE, evt -> {
            lastParagraphs.addAll(currentParagraphs);
            bottomMargins.add(bottomMargins.size(), baseMargin);
            System.out.println(bottomMargins);
        });
    }
    public void close(){
        lastDoc.close();
    }

    private int level = 0;

    public void addFootnote(float size) {
        /*lastDoc.close();
        isAdding = false;
        bottomMargins.remove(bottomMargins.size() - 1);
        bottomMargins.add(bottomMargins.size(), size);

        PdfDocument doc = createTestDocument();
        testDoc = new Document(doc);
        Iterator<Float> it = bottomMargins.iterator();
        testDoc.setMargins(baseMargin, baseMargin, baseMargin, it
            .next());
        doc.addEventHandler(PdfDocumentEvent.END_PAGE, evt -> {
            if (it.hasNext()){
                testDoc.setBottomMargin(it.next());
            } else {
                testDoc.setBottomMargin(baseMargin);
            }
        });

        for (Paragraph para: lastParagraphs){
            testDoc.add(para);
        }
        for (Paragraph para: currentParagraphs){
            testDoc.add(para);
        }
        isAdding = true;*/
    }

    public void addParagraph(Paragraph para){
        testDoc.add(para);
        currentParagraphs.add(para);
    }
}