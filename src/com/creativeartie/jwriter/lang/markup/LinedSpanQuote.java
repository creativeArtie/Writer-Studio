package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Line that stores a block quote.
 */
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

    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}