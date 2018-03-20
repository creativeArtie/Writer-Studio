package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public class DataContentLine implements Data{
    private final DataWriting baseData;
    private LinedSpan mainNote;
    private ArrayList<ArrayList<DataContentNote>> pointerNotes;
    private Optional<FormatterItem> itemFormatter;
    private PageBreak pageBreak;

    public DataContentLine(DataWriting input, FormatterItem item) {
        baseData = input;
        pointerNotes = new ArrayList<>();
        pageBreak = PageBreak.NONE;

        itemFormatter = Optional.of(item);
    }

    public DataContentLine(DataWriting input, FormatterItem item,
            FormatSpanMain span) throws IOException{
        baseData = input;
        pointerNotes = new ArrayList<>();
        pageBreak = PageBreak.NONE;

        itemFormatter = parseItem(span, item, input.getBaseFont());
    }

    private Optional<FormatterItem> parseItem(FormatSpanMain span,
            FormatterItem item, SizedFont font) throws IOException{
        for (Span child: span){
            if (child instanceof FormatSpan){
                FormatSpan format = (FormatSpan) child;
                String text = format.getRaw();
                boolean bold = format.isBold();
                boolean italics = format.isItalics();
                boolean coded = format.isCoded();
                SizedFont add = font;
                if (coded){
                    add = add.changeToCourier();
                }
                add = bold?
                    (add.changeStyle(
                        italics? SizedFont.Style.BOTH: SizedFont.Style.BOLD
                    )): (italics? add.changeStyle(SizedFont.Style.ITALICS): add);
                item.appendText(text, add);
            }
        }
        if (item.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(item);
        }
    }

    public Optional<FormatterItem> getFormatter(){
        return itemFormatter;
    }

    public ArrayList<DataContentNote> listNotes(int line){
        return pointerNotes.get(line);
    }

    @Override
    public DataWriting getBaseData(){
        return baseData;
    }

    public PageBreak getPageBreak(){
        return pageBreak;
    }

    public DataContentLine setPageBreak(PageBreak style){
        pageBreak = style;
        return this;
    }
}