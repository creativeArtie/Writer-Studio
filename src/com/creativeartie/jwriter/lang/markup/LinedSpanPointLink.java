package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a hyperlink to be use later.
 */
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
        return span.isPresent()? span.get().getTrimmed() : "";
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINED_LINK) &&
            BasicParseText.checkLineEnd(isLast(), text)?
            LinedParsePointer.HYPERLINK: null;
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
