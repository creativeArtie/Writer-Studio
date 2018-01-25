package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Data that stores a {@link ContentSpan}. Represented in design/ebnf.txt as
 * {@code InfoDataText}.
 */
public final class InfoDataSpanText extends InfoDataSpan{

    private Optional<ContentSpan> cacheData;

    @Override
    public ContentSpan getData(){
        cacheData = getCache(cacheData, () -> (ContentSpan)get(0));
        return cacheData.get();
    }

    InfoDataSpanText(List<Span> children){
        super(children, InfoDataType.TEXT);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void clearLocalCache(){
        super.clearLocalCache();
        cacheData = Optional.empty();
    }

    @Override
    protected void clearDocCache(){}
}
