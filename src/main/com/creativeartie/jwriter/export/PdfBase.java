package com.creativeartie.jwriter.export;

import java.io.*;
import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.*;
import com.itextpdf.layout.renderer.*;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.List;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.pdf.action.*;
import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.events.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.*;

abstract class PdfBase implements Exporter{

    private final Document pdfDocument;

    public PdfBase(ManuscriptFile input, File output) throws
            FileNotFoundException{
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.addNewPage();
        pdfDocument = new Document(pdf);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evt ->{
        });
    }

    @Override
    public void parse(){
        for(SpanBranch child: fileInput.getDocument()){
            newSection((SectionSpan)child);
        }
        if (! endnoteAdded.isEmpty()){
            pdfDocument.add(new AreaBreak());
            addEndnote();
        }
    }

    @Override
    public void close(){
        pdfDocument.close();
    }

    private void addEndnote(){
        int ptr = 1;
        for (SpanBranch span: endnoteAdded){

            Text text = new Text(RomanNumbering.toRomanLower(ptr) + "  ");
            pdfDocument.add(addNote(text, ((LinedSpanPointNote)span)
                .getFormattedSpan()));
        }
    }

    private Paragraph addNote(Text start, Optional<FormatSpanMain> format){
        start.setTextRise(NOTE_RISE);
        start.setFontSize(NOTE_SIZE);
        Paragraph line = new Paragraph();
        line.add(start);
        addLine(format, line);
        return line;
    }

    private void newSection(SectionSpan section){
        for (Span child: section){
            boolean listEnds = true;
            if (child instanceof LinedSpanBreak){
                addLineBreak((LinedSpanBreak) child)
                    .ifPresent(para -> pdfDocument.add(para));
            } else if (child instanceof LinedSpanLevelSection){
                addHeading((LinedSpanLevelSection) child)
                    .ifPresent(para -> pdfDocument.add(para));
            } else if (child instanceof LinedSpanLevelList){
                // TODO
            } else if (child instanceof LinedSpanParagraph){
                addParagraph((LinedSpanParagraph)child)
                    .ifPresent(para -> pdfDocument.add(para));
            } else if (child instanceof SectionSpan){
                newSection((SectionSpan) child);
            } else if (child instanceof LinedSpanQuote){
                addQuote((LinedSpanQuote)child)
                    .ifPresent(para -> pdfDocument.add(para));
            } 
        }
    }

    protected abstract Optional<Paragraph> addLineBreak(LinedSpanBreak line);
    protected abstract Optional<Paragraph> addHeading(LinedSpanLevelSection line);
    protected abstract Optional<Paragraph> addParagraph(LinedSpanParagraph line);

    private Paragraph addLine(Optional<FormatSpanMain> format){
        return addLine(format, new Paragraph());
    }

    private float getFootnotesHeight(){
        if (footnoteDiv.isPresent()){
            DivRenderer renderer = (DivRenderer) footnoteDiv.get()
                .createRendererSubTree();
            renderer.setParent(new Document(new PdfDocument(new PdfWriter(
                new ByteArrayOutputStream()))).getRenderer());
            return renderer.layout(new LayoutContext(
                new LayoutArea(0, PageSize.A4))).getOccupiedArea().getBBox()
                .getHeight();
        }
        return 0f;
    }

    protected Paragraph addLine(Optional<FormatSpanMain> format, Paragraph para){
        if (! format.isPresent()){
            return para;
        }
        for (Span child: format.get()){
            if (child instanceof FormatSpanAgenda){
                para.addAll(addAgenda((FormatSpanAgenda)child));
            } else if (child instanceof FormatSpanContent){
                para.addAll(addContent((FormatSpanContent) span)
            } else if (child instanceof FormatSpanDirectory){
                FormatSpanDirectory ref = (FormatSpanDirectory) child;
                switch (ref.getDirectoryType()){
                    case ENDNOTE:
                        para.addAll(addEndnote(ref));
                    case FOOTNOTE:
                        para.addAll(addFootnote(ref));
                    case NOTE:
                        para.addAll(addNote(ref));
                }
            } else if (child instanceof FormatSpanLink){
                para.addAll(addLink((FormatSpanLink) child));
            }
        }
        return para;
    }

    protected abstract ArrayList<ILeafElement> addAgenda(FormatSpanAgenda span);
    protected abstract ArrayList<ILeafElement> addContent(FormatSpanContent span);
    protected abstract ArrayList<ILeafElement> addEndnote(FormatSpanDirectory span);
    protected abstract ArrayList<ILeafElement> addFootnote(FormatSpanDirectory span);
    protected abstract ArrayList<ILeafElement> addNote(FormatSpanDirectory span);

    private List buildList(LinedType type){
        if (type == LinedType.NUMBERED){
            return new List(ListNumberingType.DECIMAL);
        }
        List ans = new List();
        ans.setListSymbol(BULLET_SYMBOL);
        return ans;
    }

    private ListHandler start(Document doc, LinedType type){
        return new ListHandler(doc, type);
    }
}