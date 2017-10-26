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
    public int getPublishCount(){
        return getFormattedSpan().map(span -> span.getPublishCount()).orElse(0);
    }

    @Override
    public int getNoteCount(){
        return getFormattedSpan().map(span -> span.getNoteCount()).orElse(0);
    }
}
