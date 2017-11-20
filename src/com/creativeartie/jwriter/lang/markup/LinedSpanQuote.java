package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public class LinedSpanQuote extends LinedSpan {

    LinedSpanQuote(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanFromLast(FormatSpanMain.class);
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