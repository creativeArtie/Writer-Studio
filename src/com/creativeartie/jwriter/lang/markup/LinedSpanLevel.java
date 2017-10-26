package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public class LinedSpanLevel extends LinedSpan {

    LinedSpanLevel(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanFromLast(FormatSpanMain.class);
    }

    public int getLevel(){
        int extras = getLinedType() == LinedType.OUTLINE? 1: 0;
        return get(0).getRaw().length() - extras;
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
