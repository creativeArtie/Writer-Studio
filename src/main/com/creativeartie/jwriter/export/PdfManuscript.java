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
import com.itextpdf.kernel.pdf.canvas.draw.*;

final class PdfManuscript extends PdfBase{
    private static final int HEADING_SIZE = 18;
    private static final int TEXT_SIZE = 12;
    private static final int QUOTE_PADDING = 40;
    private static final float NOTE_RISE = 8f;
    private static final int NOTE_SIZE = 8;
    private ArrayList<FormatSpanMain> footnoteList;
    private Consumer<FormatSpanMain> footnoteSpan;

    public PdfManuscript(ManuscriptFile input, File output) throws
            FileNotFoundException{
        super(input, output);
        footnoteList = new ArrayList<>();
    }

    protected void endPage(){
        footnoteList.clear();
    }

    protected void addEndnoteLines(ArrayList<FormatSpanMain> endnotes){
        newPage();
        int ptr = 1;
        for (FormatSpanMain span: endnotes){
            Text start = addSuperscript(RomanNumbering.toRomanLower(ptr));
            Paragraph para = new Paragraph();
            para.add(start);
            addLine(span, para);
            add(para);
        }
    }

    protected void addBreakLine(LinedSpanBreak line){
        Paragraph ans = new Paragraph("*");
        ans.setTextAlignment(TextAlignment.CENTER);
        add(ans);
    }

    protected Optional<PdfListHandler> addList(LinedSpanLevelList line,
            Optional<PdfListHandler> list){
        sameList();
        if (! list.isPresent()){
            list = startList(line.getLinedType());
        }
        return list.get().add(line, span -> addLine(span.getFormattedSpan()));
    }

    protected void addHeadingLine(LinedSpanLevelSection line){
        if (line.getLinedType() == LinedType.OUTLINE){
            sameList();
            return;
        }
        newPage();
        Paragraph ans = addLine(line.getFormattedSpan());
        ans.setFontSize(HEADING_SIZE);
        add(ans);
    }

    protected void addParagraphLine(LinedSpanParagraph line){
        Paragraph ans = addLine(line.getFormattedSpan());
        ans.setFontSize(TEXT_SIZE);
        add(ans);
    }

    protected void addQuoteLine(LinedSpanQuote line){
        Paragraph ans = addLine(line.getFormattedSpan());
        ans.setPaddingLeft(QUOTE_PADDING);
        ans.setPaddingRight(QUOTE_PADDING);
        add(ans);
    }

    protected ArrayList<ILeafElement> addAgendaSpan(FormatSpanAgenda span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        return ans;
    }

    protected ArrayList<ILeafElement> addContentSpan(FormatSpanContent span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        String text = span.getTrimmed();
        if (span.isSpaceBegin()){
            text = " " + text;
        }
        if (span.isSpaceEnd()){
            text = text + " ";
        }
        ans.add(setFormat(text, span));
        return ans;
    }

    protected ArrayList<ILeafElement> addEndnoteSpan(FormatSpanDirectory span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        searchPointNote(span, add -> {
            int ptr = addEndnoteSpan(add);
            ans.add(addSuperscript(RomanNumbering.toRomanLower(ptr + 1), span));
        });
        return ans;
    }

    protected ArrayList<ILeafElement> addFootnoteSpan(FormatSpanDirectory span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        searchPointNote(span, found -> {
            int ptr = addFootnote(found);
            ans.add(addSuperscript(ptr + 1 + "", span));
        });
        return ans;
    }

    private int addFootnote(FormatSpanMain span){
        int i = 0;
        for (FormatSpanMain item: footnoteList){
            if (item == span){
                return i;
            }
            i++;
        }
        footnoteList.add(span);
        if (! hasFootnoteDiv()){
            addFootnoteLine(new LineSeparator(new SolidLine()));
        }
        Text text = addSuperscript(footnoteList.size() + "");
        Paragraph para = new Paragraph();
        para.add(text);
        addLine(span, para);
        addFootnoteLine(para);
        return i;
    }

    private void searchPointNote(FormatSpanDirectory span,
            Consumer<FormatSpanMain> consumer){
        span.getTarget().map(found -> (LinedSpanPointNote) found)
            .flatMap(found -> found.getFormattedSpan())
            .ifPresent(consumer);
    }

    protected ArrayList<ILeafElement> addNoteSpan(FormatSpanDirectory span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        Optional<NoteCardSpan> line = span.getTarget()
            .map(found -> (NoteCardSpan) found);
        Optional<LinedSpanCite> text = line
            .flatMap(found -> found.getInTextLine());
        Optional<FormatSpanMain> source = line
            .flatMap(found -> found.getSource());
        if (! text.isPresent() || ! source.isPresent()){
            return ans;
        }
        LinedSpanCite cite = text.get();
        if(cite.getFieldType() == InfoFieldType.IN_TEXT){
            ans.add(setFormat(cite.getData().map(found -> found.getData())
                .map(found -> (ContentSpan) found)
                .get().getTrimmed(), span));
        } else {
            assert cite.getFieldType() == InfoFieldType.FOOTNOTE;
            return ans;
        }

        return ans;
    }

    protected ArrayList<ILeafElement> addLinkSpan(FormatSpanLink span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        String text = span.getText();
        Optional<SpanBranch> target = span.getPathSpan();
        if (! target.isPresent()){
            ans.add(setFormat(text, span));
            return ans;
        }
        SpanBranch found = target.get();
        Link link;
        if (found instanceof LinedSpanLevelSection){
            /// TODO
            return ans;
        } else {
            String path = found instanceof LinedSpanPointLink?
                ((LinedSpanPointLink)found).getPath() :
                ((ContentSpan)found).getTrimmed();
            PdfAction action = PdfAction.createURI(path);
            link = new Link(text, action);
        }
        link.setFontColor(ColorConstants.BLUE);
        ans.add(setFormat(link, span));
        return ans;
    }
}