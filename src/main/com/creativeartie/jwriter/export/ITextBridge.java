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
import com.itextpdf.kernel.geom.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.List;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.pdf.action.*;
import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.events.*;

final class ITextBridge implements Exporter{
    private static final int HEADING_SIZE = 18;
    private static final int TEXT_SIZE = 12;
    private static final int QUOTE_PADDING = 40;
    private static final String BULLET_SYMBOL = "•  ";
    private static final String LINE_SEP = "━━━━━━━━━━━━━━━━━━━━";
    private static final float NOTE_RISE = 8f;
    private static final int NOTE_SIZE = 8;

    private final Document pdfDocument;
    private final ManuscriptFile fileInput;
    private ArrayList<SpanBranch> endnoteAdded;
    private ArrayList<SpanBranch> footnoteAdded;
    private Optional<Div> footnoteDiv;

    public ITextBridge(ManuscriptFile input, File output) throws
            FileNotFoundException{
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.addNewPage();
        pdfDocument = new Document(pdf);
        fileInput = input;
        endnoteAdded = new ArrayList<>();
        footnoteAdded = new ArrayList<>();
        footnoteDiv = Optional.empty();
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evt ->{
            if (! footnoteDiv.isPresent()){
                return;
            }
            Div adding = footnoteDiv.get();
            PdfDocumentEvent event = (PdfDocumentEvent) evt;
            PdfPage page = event.getPage();
            Rectangle size = page.getPageSize();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(),
                page.getResources(), pdf);
            System.out.println(adding.getHeight());
            new Canvas(canvas, pdf,  new Rectangle(
                    pdf.getDefaultPageSize().getX() + pdfDocument.getLeftMargin(),
                    pdf.getDefaultPageSize().getY() + pdfDocument.getBottomMargin(),
                    100,
                    40
                )).add(footnoteDiv.get());
            footnoteDiv = Optional.empty();
            footnoteAdded.clear();
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
        Optional<ListHandler> list = Optional.empty();
        for (Span child: section){
            boolean listEnds = true;
            if (child instanceof LinedSpanBreak){
                Paragraph paragaraph = new Paragraph("*");
                paragaraph.setTextAlignment(TextAlignment.CENTER);
                pdfDocument.add(paragaraph);
            } else if (child instanceof LinedSpanLevelSection){
                pdfDocument.add(new AreaBreak());
                LinedSpanLevelSection line = (LinedSpanLevelSection) child;
                Paragraph para = addLine(line.getFormattedSpan());
                para.setFontSize(HEADING_SIZE);
                pdfDocument.add(para);
            } else if (child instanceof LinedSpanLevelList){
                listEnds = false;
                LinedSpanLevelList line = (LinedSpanLevelList) child;
                if (! list.isPresent()){
                    list = Optional.of(start(pdfDocument, line.getLinedType()));
                }
                list = list.get().add(line);
            } else if (child instanceof LinedSpanParagraph){
                LinedSpanParagraph line = (LinedSpanParagraph) child;
                Paragraph para = addLine(line.getFormattedSpan());
                para.setFontSize(TEXT_SIZE);
                pdfDocument.add(para);
            } else if (child instanceof SectionSpan){
                newSection((SectionSpan) child);
            } else if (child instanceof LinedSpanQuote){
                LinedSpanQuote line = (LinedSpanQuote) child;
                Paragraph para = addLine(line.getFormattedSpan());
                para.setPaddingLeft(QUOTE_PADDING);
                para.setPaddingRight(QUOTE_PADDING);
                pdfDocument.add(para);
            } else {
                listEnds = false;
            }
            if (listEnds){
                list.ifPresent(completed -> completed.completed());
                list = Optional.empty();
            }
        }
        list.ifPresent(completed -> completed.completed());
    }

    private Paragraph addLine(Optional<FormatSpanMain> format){
        return addLine(format, new Paragraph());
    }

    private int getFootnotesHeight(){
        if (footnoteDiv.isPresent()){

        }
        return 0;
    }

    private Paragraph addLine(Optional<FormatSpanMain> format, Paragraph para){
        if (! format.isPresent()){
            return para;
        }
        for (Span child: format.get()){
            if (child instanceof FormatSpan){
                para.addAll(addSpan((FormatSpan)child));
            }
        }
        return para;
    }

    private ArrayList<ILeafElement> addSpan(FormatSpan format){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        if (format instanceof FormatSpanContent){
            FormatSpanContent content = (FormatSpanContent) format;
            String text = content.getTrimmed();
            if (content.isSpaceBegin()){
                text = " " + text;
            }
            if (content.isSpaceEnd()){
                text = text + " ";
            }
            ans.add(newText(text, format));
        } else if (format instanceof FormatSpanLink){
            FormatSpanLink content = (FormatSpanLink) format;
            ans.add(newText(content.getText(), format));
            Link path;
            Optional<SpanBranch> pointer = content.getPathSpan();
            if (! pointer.isPresent()){
                return ans;
            }
            SpanBranch found = pointer.get();

            if (found instanceof ContentSpan){
                String target = ((ContentSpan)found).getTrimmed();
                PdfAction action = PdfAction.createURI(target);
                path = new Link("<" + target + ">", action);
            } else if (found instanceof LinedSpanPointLink){
                String target = ((LinedSpanPointLink)found).getPath();
                if (target.isEmpty()){
                    return ans;
                }
                PdfAction action = PdfAction.createURI(target);
                path = new Link("<" + target + ">", action);
            } else {
                assert found instanceof LinedSpanLevelSection;
                return ans;
            }
            path.setFontColor(ColorConstants.BLUE);
            ans.add(setFormat(path, format));
        } else if (format instanceof FormatSpanDirectory){
            FormatSpanDirectory ref = (FormatSpanDirectory) format;
            ref.getSpanIdentity()
                .map(id -> id.findData(ref.getDocument().getCatalogue()))
                .filter(data -> data.isReady())
                .map(data -> data.getTarget())
                .ifPresent(span ->
                    addNote(span, ref).ifPresent(text -> ans.add(text))
                );
        }
        return ans;
    }

    private Optional<Text> addNote(SpanBranch span, FormatSpanDirectory ref){
        if (ref.getIdType() == DirectoryType.ENDNOTE){
            return addNote((LinedSpanPointNote) span, note ->{
                if (! endnoteAdded.contains(note)){
                    endnoteAdded.add(note);
                }
                return newText(RomanNumbering.toRomanLower(
                    endnoteAdded.indexOf(note) + 1), ref);
            });
        } else if (ref.getIdType() == DirectoryType.FOOTNOTE){
            return addNote((LinedSpanPointNote) span, note ->{
                if (! footnoteAdded.contains(note)){
                    footnoteAdded.add(note);
                    if (! footnoteDiv.isPresent()){
                        footnoteDiv = Optional.of(new Div());
                    }
                    Text text = new Text(footnoteAdded.size() + "  ");
                    footnoteDiv.get().add(addNote(text, note
                        .getFormattedSpan()));
                }
                return newText(footnoteAdded.indexOf(note) + 1 + "", ref);
            });
        }

        return Optional.of(newText(ref.getIdType().name(), ref));
    }

    private Optional<Text> addNote(LinedSpanPointNote span,
            Function<LinedSpanPointNote, Text> special){
        Optional<FormatSpanMain> content = span.getFormattedSpan();
        if (! content.isPresent()){
            return Optional.empty();
        }
        Text text = special.apply(span);
        text.setTextRise(NOTE_RISE);
        text.setFontSize(NOTE_SIZE);
        return Optional.of(text);
    }

    private Text newText(String data, FormatSpan format){
        Text text = new Text(data);
        setFormat(text, format);
        return text;
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
            // text.setFont(StandardFonts.COURIER);
        }
        return text;
    }

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

    private class ListHandler{
        private final Document baseDoc;
        private final LinedType listType;
        private final Optional<ListHandler> parentList;
        private final List filledList;
        private final int listLevel;
        private ListItem lastItem;

        private ListHandler(Document doc, LinedType type){
            baseDoc = doc;
            listLevel = 1;
            listType = type;
            filledList = buildList(type);
            parentList = Optional.empty();
        }

        private ListHandler(Document doc, ListHandler parent){
            baseDoc = doc;
            listLevel = parent.listLevel + 1;
            listType = parent.listType;
            filledList = buildList(listType);
            parentList = Optional.of(parent);
        }

        Optional<ListHandler> add(LinedSpanLevelList line){
            if (line.getLinedType() == listType){
                if (line.getLevel() == listLevel){
                    lastItem = new ListItem();
                    lastItem.add(addLine(line.getFormattedSpan()));
                    filledList.add(lastItem);
                    return Optional.of(this);
                }
                if (line.getLevel() > listLevel){
                    ListHandler child = new ListHandler(baseDoc, this);
                    return child.add(line);
                }
                if (line.getLevel() < listLevel){
                    assert listLevel > 1: line;
                    parentList.get().getLast().add(filledList);
                    return parentList.get().add(line);
                }
                assert false;
            }
            completed();
            ListHandler ans = new ListHandler(baseDoc, line.getLinedType());
            return ans.add(line);
        }

        private ListItem getLast(){
            if (lastItem == null){
                lastItem = new ListItem();
                filledList.add(lastItem);
            }
            return lastItem;
        }

        void completed(){
            if(parentList.isPresent()){
                parentList.get().getLast().add(filledList);
                parentList.get().completed();
            } else {
                baseDoc.add(filledList);
            }
        }
    }
}