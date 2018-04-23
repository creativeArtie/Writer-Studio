package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/**
 * Data that stores a {@link DirectorySpan} for notes. Represented in
 * design/ebnf.txt as {@code InfoDataRef}.
 */
public final class InfoDataSpanRef extends InfoDataSpan{

    private final CacheKeyMain<DirectorySpan> cacheData;

    @Override
    public DirectorySpan getData(){
        return getLocalCache(cacheData, () -> (DirectorySpan)get(0));
    }

    protected InfoDataSpanRef(List<Span> children){
        super(children, InfoDataType.NOTE_REF);
        cacheData = new CacheKeyMain<>(DirectorySpan.class);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
