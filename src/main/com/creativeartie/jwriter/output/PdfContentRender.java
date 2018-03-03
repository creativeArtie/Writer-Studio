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
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.constants.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

class PdfContentRender extends PdfPageRender{

    private static final float NOTE_RISE = 8f;
    private static final int NOTE_SIZE = 8;

    private Document documentOutput;
    private int pageNumber;
    private PdfDocumentRun docHolder;

    PdfContentRender(OutputInfo info, PdfFileOutput file){
        super(info, file);
        documentOutput = new Document(file.getPdfDocument());
        float margin = OutputInfo.inchToPoint(1f);
        documentOutput.setMargins(margin, margin, margin, margin);
        documentOutput.add(new AreaBreak());
        //Document.setFixedPosition(int pageNumber, float left, float bottom, float width)
        pageNumber = 1;
        file.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, evt -> {
            documentOutput.setBottomMargin(margin + (50 * pageNumber));
            pageNumber++;
        });
        docHolder = new PdfDocumentRun(margin);
    }

    void render(LinedSpan span){
        if (span instanceof LinedSpanParagraph){
            ((LinedSpanParagraph)span).getFormattedSpan()
                .ifPresent(found -> docHolder.addParagraph(renderLine(found)));
        }
    }

    void close(){
        docHolder.close();
    }

    protected Text addSuperscript(String string){
        Text text = new Text(string);
        text.setTextRise(NOTE_RISE);
        text.setFontSize(NOTE_SIZE);
        return text;

    }

    protected Text addSuperscript(String string, FormatSpan format){
        return setFormat(addSuperscript(string), format);
    }

    private Paragraph renderLine(FormatSpanMain content){
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
                FormatSpanDirectory ref = (FormatSpanDirectory) child;
                switch (ref.getIdType()){
                    case ENDNOTE:
                        // para.addAll(addEndnoteSpan(ref));
                        break;
                    case FOOTNOTE:
                        para.add(newFootnoteText(ref));
                        break;
                    case NOTE:
                        // para.addAll(addNoteSpan(ref));
                        break;
                }
            }
        }
        return para;
    }

    private Text newFootnoteText(FormatSpanDirectory ref){
        return searchPointNote(ref, found -> {
            return addSuperscript(1 + "", ref);
        });
    }

    private Text searchPointNote(FormatSpanDirectory span,
            Function<FormatSpanMain, Text> consumer){
        return span.getTarget().map(found -> (LinedSpanPointNote) found)
            .flatMap(found -> found.getFormattedSpan())
            .map(consumer).get();
    }

    private void addFootnoteBottom(FormatSpanMain span){
        Paragraph line = renderLine(span);
        docHolder.addFootnote(line);
    }

    private Text setFormat(String string, FormatSpan format){
        return setFormat(new Text(string), format);
    }

    private Text setFormat(Text text, FormatSpan format){
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