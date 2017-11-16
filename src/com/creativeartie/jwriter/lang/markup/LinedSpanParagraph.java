package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public class LinedSpanParagraph extends LinedSpan {

    LinedSpanParagraph(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanAtFirst(FormatSpanMain.class);
    }

    @Override
    public int getPublishTotal(){
        return getFormattedSpan().map(span -> span.getPublishTotal()).orElse(0);
    }

    @Override
    public int getNoteTotal(){
        return getFormattedSpan().map(span -> span.getNoteTotal()).orElse(0);
    }
}
