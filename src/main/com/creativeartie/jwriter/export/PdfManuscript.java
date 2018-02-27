package com.creativeartie.jwriter.export;

import java.io.*;
import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.resource.*;

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

/// Format follow: https://www.scribophile.com/academy/how-to-format-a-novel-manuscript
final class PdfManuscript extends PdfBase{
    private static final int HEADING_SIZE = 18;
    private static final int TEXT_SIZE = 12;
    private static final int QUOTE_PADDING = 40;
    private static final float NOTE_RISE = 8f;
    private static final int NOTE_SIZE = 8;
    private ArrayList<FormatSpanMain> footnoteList;
    private TreeSet<FormatSpanMain> citationsList;
    private boolean willIndent;

    public PdfManuscript(ManuscriptFile input, File output) throws
            FileNotFoundException{
        super(input, output);
        footnoteList = new ArrayList<>();
        citationsList = new TreeSet<>(Comparator.comparing(span ->
            span.getParsedText()));
    }

    @Override
    protected void endPage(){
        footnoteList.clear();
    }

    @Override
    protected void startDoc(ManuscriptFile input, PdfFrontPageHandler output){
        Div div = new Div();
        div.add(getFrontParagraph(input, MetaData.AGENT_NAME));
        div.add(getFrontParagraph(input, MetaData.AGENT_ADDRESS));
        div.add(getFrontParagraph(input, MetaData.AGENT_EMAIL));
        div.add(getFrontParagraph(input, MetaData.AGENT_PHONE));
        div.setVerticalAlignment(VerticalAlignment.TOP);
        output.addTop(div);

        div = new Div();
        div.add(getFrontParagraph(input, MetaData.TITLE));
        div.add(clearMargin(new Paragraph()));
        div.add(getFrontParagraph(input, MetaData.BY));
        div.add(clearMargin(new Paragraph()));
        div.add(getFrontParagraph(input, MetaData.PEN_NAME, MetaData.AUTHOR));
        div.setHorizontalAlignment(HorizontalAlignment.CENTER);
        div.setVerticalAlignment(VerticalAlignment.MIDDLE);
        div.setTextAlignment(TextAlignment.CENTER);
        output.addCentre(div);
    }

    private Paragraph getFrontParagraph(ManuscriptFile input, MetaData... data){
        return clearMargin(new Paragraph(input.getText(data)));
    }

    @Override
    protected void endDoc(){
        if (! citationsList.isEmpty()){
            newPage();
            Paragraph title = new Paragraph(WindowText.WORK_CITED.getText());
            title.setTextAlignment(TextAlignment.CENTER);
            add(title);
            for (FormatSpanMain cite: citationsList){
                add(addLine(cite));
            }
        }
    }

    @Override
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

    @Override
    protected void addBreakLine(LinedSpanBreak line){
        Paragraph ans = new Paragraph("#");
        ans.setTextAlignment(TextAlignment.CENTER);
        add(new Paragraph());
        add(ans);
        willIndent = false;
    }

    @Override
    protected Optional<PdfListHandler> addList(LinedSpanLevelList line,
            Optional<PdfListHandler> list){
        sameList();
        if (! list.isPresent()){
            list = startList(line.getLinedType());
        }
        return list.get().add(line, span -> addLine(span.getFormattedSpan()));
    }

    @Override
    protected void addHeadingLine(LinedSpanLevelSection line){
        if (line.getLinedType() == LinedType.OUTLINE){
            sameList();
            return;
        }
        Paragraph ans = addLine(line.getFormattedSpan());
        if (line.getLevel() == 1){
            newPage();
            ans.setMarginTop(PAGE_THIRD);
            ans.setTextAlignment(TextAlignment.CENTER);
        }
        ans.setFontSize(HEADING_SIZE);
        add(ans);
        if (line.getLevel() == 1){
            for (int i = 0; i < 3; i++){
                add(new Paragraph());
            }
        }
        willIndent = false;
    }

    @Override
    protected void addParagraphLine(LinedSpanParagraph line){
        Paragraph ans = addLine(line.getFormattedSpan());
        if (willIndent){
            ans.setFirstLineIndent(20f);
        } else {
            willIndent = true;
        }
        ans.setFontSize(TEXT_SIZE);
        add(ans);
    }

    @Override
    protected void addQuoteLine(LinedSpanQuote line){
        Paragraph ans = addLine(line.getFormattedSpan());
        ans.setPaddingLeft(QUOTE_PADDING);
        ans.setPaddingRight(QUOTE_PADDING);
        add(ans);
    }

    @Override
    protected ArrayList<ILeafElement> addAgendaSpan(FormatSpanAgenda span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        return ans;
    }

    @Override
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

    @Override
    protected ArrayList<ILeafElement> addEndnoteSpan(FormatSpanDirectory span){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        searchPointNote(span, add -> {
            int ptr = addEndnoteSpan(add);
            ans.add(addSuperscript(RomanNumbering.toRomanLower(ptr + 1), span));
        });
        return ans;
    }

    @Override
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

    @Override
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
            FormatSpanMain footnote = (FormatSpanMain) cite.getData().get()
                .getData();
            int ptr = addFootnote(footnote);
            ans.add(addSuperscript(ptr + 1 + "", span));
        }
        citationsList.add(source.get());
        return ans;
    }

    @Override
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