package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public class LinedSpanPointLink extends LinedSpanPoint {

    LinedSpanPointLink(List<Span> children){
        super(children);
    }

    @Override
    public DirectoryType getDirectoryType(){
        return DirectoryType.LINK;
    }

    public Optional<ContentSpan> getPathSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getPath(){
        Optional<ContentSpan> span = getPathSpan();
        return span.isPresent()? span.get().getParsed() : "";
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
