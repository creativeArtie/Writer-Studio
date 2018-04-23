package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/**
 * Data that stores a {@link ContentSpan}. Represented in design/ebnf.txt as
 * {@code InfoDataText}.
 */
public final class InfoDataSpanText extends InfoDataSpan{

    private final CacheKeyMain<ContentSpan> cacheData;

    @Override
    public ContentSpan getData(){
        return getLocalCache(cacheData, () -> (ContentSpan)get(0));
    }

    protected InfoDataSpanText(List<Span> children){
        super(children, InfoDataType.TEXT);
        cacheData = new CacheKeyMain<>(ContentSpan.class);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
