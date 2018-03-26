package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.awt.*;
import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public class DataContentLine extends Data{
    private ArrayList<ArrayList<DataContentNote>> pointerNotes;
    private Optional<FormatterItem> itemFormatter;
    private PageBreak pageBreak;

    public DataContentLine(Data input, FormatterItem item) {
        super(input);
        pointerNotes = new ArrayList<>();
        pageBreak = PageBreak.NONE;

        itemFormatter = Optional.of(item);
    }

    public DataContentLine(Data input, FormatterItem item,
            FormatSpanMain span, SizedFont font) throws IOException{
        super(input);
        pointerNotes = new ArrayList<>();
        pageBreak = PageBreak.NONE;

        itemFormatter = parseItem(span, item, font);
    }

    private Optional<FormatterItem> parseItem(FormatSpanMain span,
            FormatterItem item, SizedFont font) throws IOException{
        for (Span child: span){
            if (child instanceof FormatSpan){
                FormatSpan format = (FormatSpan) child;
                String text = getText(format);
                SizedFont add = font;
                if (format.isCoded()){
                    add = add.changeToMono();
                }
                add = add.changeBold(format.isBold());
                add = add.changeItalics(format.isItalics());
                add = add.changeUnderline(format.isUnderline());
                if (child instanceof FormatSpanLink){
                    add = add.changeFontColor(Color.BLUE);
                }
                if (child instanceof FormatSpanDirectory){
                    add = add.changeToSuperscript();
                }
                setAddition(item.appendText(text, add), format);
            }
        }
        if (item.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(item);
        }
    }

    private String getText(FormatSpan span){
        if (span instanceof FormatSpanContent){
            return ((FormatSpanContent) span).getText();
        } else if (span instanceof FormatSpanLink){
            return ((FormatSpanLink)span).getText();
        } else if (span instanceof FormatSpanDirectory){
            FormatSpanDirectory ref = (FormatSpanDirectory) span;
            Optional<SpanBranch> base = ref.getTarget();
            Optional<LinedSpanPointNote> note = base
                .filter(t -> t instanceof LinedSpanPointNote)
                .map(t -> (LinedSpanPointNote) t);
            Optional<String> text = note
                .filter(t -> t.getDirectoryType() == DirectoryType.ENDNOTE)
                .map(t -> getContentData().addEndnote(t));
            return text.orElse(span.getRaw());
        }
        return span.getRaw();
    }

    private void setAddition(ArrayList<FormatterData> formatter,
            FormatSpan span){
        if (span instanceof FormatSpanLinkDirect){
            String path = ((FormatSpanLinkDirect)span).getPath();
            for (FormatterData data: formatter){
                data.setLinkPath(path);
            }
        } else if (span instanceof FormatSpanLinkRef){
            Optional<SpanBranch> target = ((FormatSpanLinkRef) span)
                .getPathSpan();
            target.filter(f -> f instanceof LinedSpanPointLink)
                .map(s -> (LinedSpanPointLink) s)
                .ifPresent(s ->
                    formatter.forEach(c -> c.setLinkPath(s.getPath()))
                );
        }
    }

    public Optional<FormatterItem> getFormatter(){
        return itemFormatter;
    }

    public ArrayList<DataContentNote> listNotes(int line){
        return pointerNotes.get(line);
    }

    public PageBreak getPageBreak(){
        return pageBreak;
    }

    public DataContentLine setPageBreak(PageBreak style){
        pageBreak = style;
        return this;
    }
}