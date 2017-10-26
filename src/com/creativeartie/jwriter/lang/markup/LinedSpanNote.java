package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

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
    public int getNoteCount(){
        return getFormattedSpan().map(span -> span.getTotalCount()).orElse(0);
    }

    /* // TODO Speed up preformance by edit only some of the text
    @Override
    protected DetailUpdater getUpdater(int indexedSpan, String newText){
        return DetailUpdater.unable();
    }
    */
}
