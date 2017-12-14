package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Line that store note heading or details.
 */
public class LinedSpanNote extends LinedSpan{

    public LinedSpanNote(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanFromLast(FormatSpanMain.class);
    }

    CatalogueIdentity buildId(){
        return spanFromFirst(DirectorySpan.class).map(span -> span.buildId())
            .orElse(null);
    }

    @Override
    public int getNoteTotal(){
        return getFormattedSpan().map(span -> span.getTotalCount()).orElse(0);
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
