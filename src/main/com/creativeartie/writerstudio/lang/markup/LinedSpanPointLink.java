package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a hyperlink to be use later. Represented in design/ebnf.txt
 * as {@code LinedLink}.
 */
public class LinedSpanPointLink extends LinedSpanPoint {

    private final CacheKeyMain<String> cachePath;

    LinedSpanPointLink(List<Span> children){
        super(children);

        cachePath = CacheKeyMain.stringKey();
    }

    @Override
    public DirectoryType getDirectoryType(){
        return DirectoryType.LINK;
    }

    public Optional<ContentSpan> getPathSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getPath(){
        return getLocalCache(cachePath, () -> {
            Optional<ContentSpan> span = getPathSpan();
            return span.isPresent()? span.get().getTrimmed() : "";
        });
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINED_LINK) &&
            AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            LinedParsePointer.HYPERLINK: null;
    }

    protected String getLookupStart(){
        return LINK_REF;
    }

    protected String getLookupEnd(){
        return LINK_END;
    }
}
