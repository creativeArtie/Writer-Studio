package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

/**
 * Prints the agent data
 */
class PdfSectionTitleHeader extends PdfSection{
    private float baseMargins;
    private PDPage outputPage;
    private ArrayList<String> headerLines;
    private float startX;
    private float startY;
    private float contentWidth;

    public PdfSectionTitleHeader(float margins){
        super();
        headerLines = new ArrayList<>();
        baseMargins = margins;
    }

    void loadContent(DocumentData file, PdfDocument doc){
        headerLines = file.getTitleTopText();
        startY = doc.getPage().getMediaBox().getHeight() - baseMargins;
        startX = baseMargins;
        contentWidth = doc.getPage().getMediaBox().getWidth() - (baseMargins * 2);
    }

    void render(PDPageContentStream output) throws IOException{
        PDFont font = PDType1Font.HELVETICA_BOLD;

        output.beginText();
        output.setFont(font, 12);
        output.newLineAtOffset(startX, startY);
        for (String text: headerLines){
            for (PdfLine line: new PdfBlock(contentWidth).setLeading(0)
                    .appendText(text, font, 12)){
                //TODO render line
            }
        }
        output.endText();
    }

    public float getX(){
        return baseMargins;
    }
    public float getY(){
        return baseMargins;
    }
}