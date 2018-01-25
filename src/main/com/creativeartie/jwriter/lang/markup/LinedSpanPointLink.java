package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

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
            AuxiliaryChecker.checkLineEnd(isLast(), text)?
            LinedParsePointer.HYPERLINK: null;
    }

    @Override
    protected void clearLocalCache(){
        super.clearLocalCache();
        cachePath = Optional.empty();
    }

    @Override
    protected void clearDocCache(){}
}
