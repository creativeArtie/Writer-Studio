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

    public DataContent(DataWriting data){
        baseData = data;
    }

    @Override
    public DataWriting getBaseData(){
        return baseData;
    }

    public List<DataContentLine> getContentLines(StreamData data)
            throws IOException{
        ImmutableList.Builder<DataContentLine> builder = ImmutableList.builder();
        for (LinedSpan child: listLines()){
            DataContentLine line = new DataContentLine(baseData, child, data);
            line.getFormatter().ifPresent(item -> builder.add(line));
}
        return builder.build();
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