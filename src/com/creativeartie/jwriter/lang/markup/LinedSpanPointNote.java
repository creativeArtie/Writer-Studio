package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Line that stores text of a footnote or a endnote.
 */
public class LinedSpanPointNote extends LinedSpanPoint {

    LinedSpanPointNote(List<Span> children){
        super(children);
    }

    @Override
    public DirectoryType getDirectoryType(){
        return getLinedType() == LinedType.FOOTNOTE? DirectoryType.FOOTNOTE:
            DirectoryType.ENDNOTE;
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanFromLast(FormatSpanMain.class);
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
