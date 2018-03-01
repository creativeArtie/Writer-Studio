package com.creativeartie.jwriter.output;

import java.io.*;
import java.util.*;
import java.util.Optional;

import com.google.common.base.*;

import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

class PdfContentRender extends PdfPageRender{

    private Paragraph checkingLine;
    private Optional<Paragraph> addingLine;
    private Div checkingContent;
    private Div addingContent;

    PdfContentRender(OutputInfo info, PdfFileOutput file){
        super(info, file);
        checkingContent = new Div();
        addingContent = new Div();
    }

    void completedPage(){
        addingLine.ifPresent(found -> addingContent.add(found));
        addContent(addingContent);
    }

    void render(LinedSpan span){
        if (span instanceof LinedSpanParagraph){
            ((LinedSpanParagraph)span).getFormattedSpan()
                .ifPresent(found -> renderLine(found));
        }
    }

    private void renderLine(FormatSpanMain span){
    }

    private void newLine(){
        checkingLine = new Paragraph();
        checkingContent.add(checkingLine);
        addingLine = Optional.of(new Paragraph());
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