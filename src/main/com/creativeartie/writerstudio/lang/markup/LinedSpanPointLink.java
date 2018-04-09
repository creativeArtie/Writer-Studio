package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a hyperlink to be use later. Represented in design/ebnf.txt
 * as {@code LinedLink}.
 */
public class LinedSpanPointLink extends LinedSpanPoint {

    private Optional<String> cachePath;

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
        cachePath = getCache(cachePath, () -> {
            Optional<ContentSpan> span = getPathSpan();
            return span.isPresent()? span.get().getTrimmed() : "";
        });
        return cachePath.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINED_LINK) &&
            AuxiliaryChecker.checkLineEnd(text, isLast())?
            LinedParsePointer.HYPERLINK: null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cachePath = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    protected String getLookupStart(){
        return LINK_REF;
    }

    protected String getLookupEnd(){
        return LINK_END;
    }
}
