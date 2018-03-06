package com.creativeartie.jwriter.output;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.Optional;

import com.google.common.base.*;

import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.events.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.*;
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
        pageNumber = 0;
        PdfDocument doc = file.getPdfDocument();
        doc.addEventHandler(PdfDocumentEvent.END_PAGE, evt -> {
            /*if (pageNumber == -1){
                pageNumber++;
                return;
            }*/
            documentOutput.setBottomMargin(docHolder.marginOnPage(pageNumber));
            Div footnote = docHolder.footnoteOnPage(pageNumber - 2);

            /// Setup
            PdfDocumentEvent use = (PdfDocumentEvent) evt;
            PdfPage page = use.getPage();
            Rectangle size = page.getPageSize();
            PdfCanvas canvas = new PdfCanvas(page);

            /// Work with footnotes
            float footnotes = getElementHeight(footnote, margin);
            System.out.println(pageNumber + "\t" + docHolder.marginOnPage(pageNumber) +
                "\t" + footnotes);
            new Canvas(canvas, doc, new Rectangle(
                size.getX() + margin,
                size.getY() + margin,
                size.getWidth() - (margin * 2),
                footnotes
            )).add(footnote);
            pageNumber++;
        });
        docHolder = new PdfDocumentRun(margin);
    }

    void render(LinedSpan span){
        if (span instanceof LinedSpanParagraph){
            ((LinedSpanParagraph)span).getFormattedSpan()
                .ifPresent(found -> docHolder.addParagraph(renderLine(found)));
        } else if (span instanceof LinedSpanLevelSection){
            ((LinedSpanLevelSection)span).getFormattedSpan()
                .ifPresent(found -> docHolder.addParagraph(renderLine(found)));
        }
    }

    void close(){
        docHolder.close();
        for (IBlockElement element: docHolder.getContent()){
            documentOutput.add(element);
        }
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
        return renderLine(content, new Paragraph());
    }

    private Paragraph renderLine(FormatSpanMain content, Paragraph para){
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
                        newFootnoteText(ref).ifPresent(found -> para.add(found));
                        break;
                    case NOTE:
                        // para.addAll(addNoteSpan(ref));
                        break;
                }
            }
        }
        return para;
    }

    private Optional<Text> newFootnoteText(FormatSpanDirectory ref){
        return searchPointNote(ref, found -> {
            addFootnoteBottom(found);
            return addSuperscript("*", ref);
        });
    }

    private Optional<Text> searchPointNote(FormatSpanDirectory span,
            Function<FormatSpanMain, Text> consumer){
        return span.getTarget().map(found -> (LinedSpanPointNote) found)
            .flatMap(found -> found.getFormattedSpan())
            .map(consumer);
    }

    private void addFootnoteBottom(FormatSpanMain span){
        Paragraph line = new Paragraph();
        line.add(new Text("+"));
        docHolder.addFootnote(renderLine(span, line));
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