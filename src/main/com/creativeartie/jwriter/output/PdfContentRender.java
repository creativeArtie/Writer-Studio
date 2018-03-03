package com.creativeartie.jwriter.output;

import java.io.*;
import java.util.*;
import java.util.Optional;

import com.google.common.base.*;

import com.itextpdf.layout.element.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.events.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

class PdfContentRender extends PdfPageRender{

    private Document documentOutput;
    private Paragraph documentLine;
    private Optional<Div> footnoteDiv;
    private int pageNumber;
    private PdfMarginCalculator pdfCalculator;

    PdfContentRender(OutputInfo info, PdfFileOutput file){
        super(info, file);
        documentOutput = new Document(file.getPdfDocument());
        float margin = OutputInfo.inchToPoint(1f);
        documentOutput.setMargins(margin, margin, margin, margin);
        documentOutput.add(new AreaBreak());
        //Document.setFixedPosition(int pageNumber, float left, float bottom, float width)
        pageNumber = 1;
        footnoteDiv = Optional.empty();
        file.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, evt -> {
            System.out.println(pageNumber++);
            documentOutput.setBottomMargin(margin);
        });
        pdfCalculator = new PdfMarginCalculator(margin);
    }

    void render(LinedSpan span){
        if (span instanceof LinedSpanParagraph){
            ((LinedSpanParagraph)span).getFormattedSpan()
                .ifPresent(found -> renderLine(found));
        }
    }

    public void completed(){
        pdfCalculator.close();
    }

    private int bottom = 0;
    private void renderLine(FormatSpanMain content){
        Paragraph para = new Paragraph();
        for(Span child: content){
            if (child instanceof FormatSpanContent){
                FormatSpanContent format = (FormatSpanContent) child;
                String text = format.getTrimmed();
                if (format.isSpaceBegin()){
                    text = " " + text;
                }
                if (format.isSpaceEnd()){
                    text = text + " ";
                }
                para.add(setFormat(text, format));
            } else if (child instanceof FormatSpanDirectory){
                bottom += 20;
                para.add(setFormat("[[Hello]]", (FormatSpan) child));
                documentOutput.setBottomMargin(OutputInfo.inchToPoint(1) + bottom);
                pdfCalculator.addFootnote(OutputInfo.inchToPoint(1) + bottom);
            }
        }
        documentOutput.add(para);
        pdfCalculator.addParagraph(para);
    }

    private Text setFormat(String string, FormatSpan format){
        Text text = new Text(string);
        if (format.isItalics()){
            text.setItalic();
        }
        if (format.isBold()){
            text.setBold();
        }
        if (format.isUnderline()){
            text.setUnderline();
        }
        if (format.isCoded()){
            try {
                PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
                text.setFont(font);
            } catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
        return text;
    }
}