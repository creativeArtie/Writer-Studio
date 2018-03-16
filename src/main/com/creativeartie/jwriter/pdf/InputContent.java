package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.creativeartie.jwriter.pdf.value.*;

import com.google.common.collect.*;

public final class InputContent implements Input{
    private InputWriting baseData;

    public InputContent(InputWriting data){
        baseData = data;
    }

    @Override
    public InputWriting getBaseData(){
        return baseData;
    }

    public List<InputContentLine> getContentLines(StreamData data)
            throws IOException{
        ImmutableList.Builder<InputContentLine> builder = ImmutableList.builder();
        float width = data.getWidth();
        for (LinedSpan child: listLines()){
            InputContentLine line = new InputContentLine(baseData, child, data);
            line.getContentItem().ifPresent(item -> builder.add(line));
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

    public List<PdfItem> getHeader(StreamData data) throws IOException{
        ImmutableList.Builder<PdfItem> builder = ImmutableList.builder();
        builder.add(new PdfItem(data.getRenderWidth(getMargin()),
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