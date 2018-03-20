package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public final class DataContent implements Data{
    private DataWriting baseData;
    private boolean paraFirst;
    private LinkedList<Integer> listNumbering;

    public DataContent(DataWriting data){
        baseData = data;
        paraFirst = true;
        listNumbering = new LinkedList<>();
    }

    @Override
    public DataWriting getBaseData(){
        return baseData;
    }

    public List<DataContentLine> getContentLines(StreamData data)
            throws IOException{
        ImmutableList.Builder<DataContentLine> builder = ImmutableList.builder();
        for (LinedSpan child: listLines()){
            FormatterItem item = new FormatterItem(data.getRenderWidth(
                getMargin()));
            DataContentLine line = null;
            switch(child.getLinedType()){
            case BREAK:
                item.setTextAlignment(TextAlignment.CENTER);
                item.appendText("#", getBaseFont());
                line = new DataContentLine(getBaseData(), item);
                paraFirst = true;
                break;
            case BULLET:
                line = parseBullet((LinedSpanLevelList) child, item);
                break;
            case NUMBERED:
                line = parseNumber((LinedSpanLevelList) child, item);
                break;
            case HEADING:
                line = parse((LinedSpanLevelSection) child, item);
                break;
            case PARAGRAPH:
                line = parse((LinedSpanParagraph) child, item);
                break;
            case QUOTE:
                line = parse((LinedSpanQuote) child, item);
            }
            if (line != null && line.getFormatter().isPresent()){
                builder.add(line);
            }
        }
        builder.add(new DataContentLine(getBaseData(),
            new FormatterItem(
                data.getRenderWidth(getMargin()), TextAlignment.CENTER
            ).appendText("THE END", getBaseFont())
        ));
        return builder.build();
    }

    private DataContentLine parseBullet(LinedSpanLevelList line,
            FormatterItem item) throws IOException{
        return parse(line, item, "•");
    }

    private DataContentLine parseNumber(LinedSpanLevelList line,
            FormatterItem item) throws IOException{
        int level = line.getLevel();
        while (listNumbering.size() > level){
            listNumbering.pop();
        }
        while (listNumbering.size() < level){
            listNumbering.push(1);
        }
        int count = listNumbering.pop();
        DataContentLine ans = parse(line, item, count + ".");
        listNumbering.push(++count);
        return ans;
    }

    private DataContentLine parse(LinedSpanLevelList line, FormatterItem item,
            String prefix) throws IOException{
        float indent = Data.cmToPoint(.5f) + Data.cmToPoint(.5f) *
            line.getLevel();
        item.setFirstIndent(indent);
        item.setIndent(indent * line.getLevel());
        item.setIndent(indent * line.getLevel());
        item.setPrefix(prefix, indent - Data.cmToPoint(.75f));
        Optional<FormatSpanMain> span = line.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        paraFirst = false;
        DataContentLine ans = new DataContentLine(getBaseData(), item, span
            .get());
        ans.getFormatter().ifPresent(f -> paraFirst = false);
        return ans;
    }


    private DataContentLine parse(LinedSpanLevelSection line,
            FormatterItem item) throws IOException{
        Optional<FormatSpanMain> span = line.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        item.setTextAlignment(TextAlignment.CENTER);
        DataContentLine content = new DataContentLine(getBaseData(), item, span
            .get());
        if (line.getLevel() == 1){
            item.setBottomSpacing(Data.cmToPoint(1f));
            content.setPageBreak(PageBreak.THIRD_WAY);
        }
        return content;
    }

    private DataContentLine parse(LinedSpanParagraph line, FormatterItem item)
            throws IOException{
        Optional<FormatSpanMain> span = line.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        if (! paraFirst) {
            item.setFirstIndent(Data.cmToPoint(1.25f));
        }
        DataContentLine ans = new DataContentLine(getBaseData(), item, span
            .get());
        ans.getFormatter().ifPresent(f -> paraFirst = false);
        return ans;
    }

    private DataContentLine parse(LinedSpanQuote quote, FormatterItem item)
            throws IOException{
        item = new FormatterItem(item.getWidth() - Data.cmToPoint(2f));
        item.setFirstIndent(Data.cmToPoint(2f));
        item.setIndent(Data.cmToPoint(2f));

        Optional<FormatSpanMain> span = quote.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        DataContentLine ans = new DataContentLine(getBaseData(), item, span
            .get());
        ans.getFormatter().ifPresent(f -> paraFirst = false);
        return ans;
    }

    private List<LinedSpan> listLines(){
        ImmutableList.Builder<LinedSpan> builder = ImmutableList.builder();
        for (Span span: getWritingText()){
            builder.addAll(listLines((SectionSpan) span));
        }
        return builder.build();
    }

    private List<LinedSpan> listLines(SectionSpan section){
        ImmutableList.Builder<LinedSpan> builder = ImmutableList.builder();
        for (Span child: section){
            if (child instanceof LinedSpan){
                builder.add((LinedSpan) child);
            } else if (child instanceof SectionSpan){
                builder.addAll(listLines((SectionSpan) child));
            }
        }
        return builder.build();
    }

    public List<FormatterItem> getHeader(StreamData data) throws IOException{
        ImmutableList.Builder<FormatterItem> builder = ImmutableList.builder();
        builder.add(new FormatterItem(data.getRenderWidth(getMargin()),
            TextAlignment.RIGHT)
            .setLeading(1)
            .appendText(getOutputDoc().getText(MetaData.LAST_NAME) + "/" +
                getOutputDoc().getText(MetaData.TITLE) + "/" +
                data.getPageNumber(), getBaseFont()
            )
        );
        return builder.build();
    }
}