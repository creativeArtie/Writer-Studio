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
import com.itextpdf.kernel.pdf.navigation.*;

abstract class PdfBase implements Exporter{

    public  static float getElementHeight(IBlockElement element){
        IRenderer renderer = element.createRendererSubTree();
        renderer.setParent(new Document(new PdfDocument(new PdfWriter(
            new ByteArrayOutputStream()))).getRenderer());
        return renderer.layout(new LayoutContext(
            new LayoutArea(0, PageSize.A4))).getOccupiedArea().getBBox()
            .getHeight();
    }

    private static final float NOTE_RISE = 8f;
    private static final int NOTE_SIZE = 8;

    private final Document pdfDocument;
    private final PdfFrontPageHandler frontPage;
    private final ManuscriptFile fileInput;
    private ArrayList<FormatSpanMain> endnoteList;
    private ArrayList<FormatSpanMain> noteCited;
    private PdfFooterHeader pageEnder;
    private boolean listEnds;
    public static final float PAGE_THIRD = PageSize.A4.getHeight() / 3;

    public PdfBase(ManuscriptFile input, File output) throws
            FileNotFoundException{
        fileInput = input;
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        endnoteList = new ArrayList<>();
        pdfDocument = new Document(pdf);
        pageEnder = new PdfFooterHeader(pdfDocument, pdf);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evt -> {
            pageEnder.handleEvent(evt);
            endPage();
        });
        frontPage = new PdfFrontPageHandler(pdf.addNewPage(), pdf, pdfDocument);
    }

    protected abstract void startDoc(ManuscriptFile input,
        PdfFrontPageHandler output);
    protected abstract void endPage();
    protected abstract void endDoc();

    @Override
    public void parse(){
        startDoc(fileInput, frontPage);
        for(SpanBranch child: fileInput.getDocument()){
            newSection((SectionSpan)child);
        }
        if (! endnoteList.isEmpty()){
            addEndnoteLines(endnoteList);
        }
    }

    @Override
    public void close(){
        endDoc();
        pdfDocument.close();
    }

    protected abstract void addEndnoteLines(ArrayList<FormatSpanMain> list);

    private void newSection(SectionSpan section){
        Optional<PdfListHandler> list = Optional.empty();
        for (Span child: section){
            listEnds = true;
            if (child instanceof LinedSpanBreak){
                addBreakLine((LinedSpanBreak) child);
            } else if (child instanceof LinedSpanLevelSection){
                addHeadingLine((LinedSpanLevelSection) child);
            } else if (child instanceof LinedSpanLevelList){
                list = addList((LinedSpanLevelList)child, list);
            } else if (child instanceof LinedSpanParagraph){
                addParagraphLine((LinedSpanParagraph)child);
            } else if (child instanceof SectionSpan){
                newSection((SectionSpan) child);
            } else if (child instanceof LinedSpanQuote){
                addQuoteLine((LinedSpanQuote)child);
            }
            if (listEnds){
                list.ifPresent(completed -> completed.completed());
                list = Optional.empty();
            }
        }
        list.ifPresent(completed -> completed.completed());
    }

    protected abstract void addBreakLine(LinedSpanBreak line);
    protected abstract Optional<PdfListHandler> addList(LinedSpanLevelList line,
        Optional<PdfListHandler> list);
    protected abstract void addHeadingLine(LinedSpanLevelSection line);
    protected abstract void addParagraphLine(LinedSpanParagraph line);
    protected abstract void addQuoteLine(LinedSpanQuote line);

    protected void sameList(){
        listEnds = false;
    }

    protected Optional<PdfListHandler> startList(LinedType type){
        return Optional.of(PdfListHandler.start(pdfDocument, type));
    }

    protected Paragraph addLine(Optional<FormatSpanMain> format){
        return addLine(format, new Paragraph());
    }

    protected Paragraph addLine(Optional<FormatSpanMain> format, Paragraph para){
        if (! format.isPresent()){
            return para;
        }
        return addLine(format.get(), para);
    }

    protected Paragraph addLine(FormatSpanMain format){
        return addLine(format, new Paragraph());
    }

    protected Paragraph addLine(FormatSpanMain format, Paragraph para){
        for (Span child: format){
            if (child instanceof FormatSpanAgenda){
                para.addAll(addAgendaSpan((FormatSpanAgenda)child));
            } else if (child instanceof FormatSpanContent){
                para.addAll(addContentSpan((FormatSpanContent) child));
            } else if (child instanceof FormatSpanDirectory){
                FormatSpanDirectory ref = (FormatSpanDirectory) child;
                switch (ref.getIdType()){
                    case ENDNOTE:
                        para.addAll(addEndnoteSpan(ref));
                        break;
                    case FOOTNOTE:
                        para.addAll(addFootnoteSpan(ref));
                        break;
                    case NOTE:
                        para.addAll(addNoteSpan(ref));
                        break;
                }
            } else if (child instanceof FormatSpanLink){
                para.addAll(addLinkSpan((FormatSpanLink) child));
            }
        }
        return clearMargin(para).setMultipliedLeading(2.0f);
    }

    protected Paragraph clearMargin(Paragraph para){
        return para.setMargin(0).setPadding(0);
    }

    protected abstract ArrayList<ILeafElement> addAgendaSpan(FormatSpanAgenda span);
    protected abstract ArrayList<ILeafElement> addContentSpan(FormatSpanContent span);
    protected abstract ArrayList<ILeafElement> addEndnoteSpan(FormatSpanDirectory span);
    protected abstract ArrayList<ILeafElement> addFootnoteSpan(FormatSpanDirectory span);
    protected abstract ArrayList<ILeafElement> addNoteSpan(FormatSpanDirectory span);
    protected abstract ArrayList<ILeafElement> addLinkSpan(FormatSpanLink span);

    protected void newPage(){
        pdfDocument.add(new AreaBreak());
    }

    protected boolean hasFootnoteDiv(){
        return pageEnder.isFootnotePresent();
    }

    protected void addFootnoteLine(IBlockElement element){
        pageEnder.addFootnoteLine(element);
    }

    protected int addEndnoteSpan(FormatSpanMain span){
        int i = 0;
        for (FormatSpanMain endnote: endnoteList){
            if (span == endnote){
                return i;
            }
            i++;
        }
        endnoteList.add(span);
        return i;
    }

    protected void add(IBlockElement element){
        pdfDocument.add(element);
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

    protected Text setFormat(String string, FormatSpan format){
        return setFormat(new Text(string), format);
    }

    protected Text setFormat(Text text, FormatSpan format){
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