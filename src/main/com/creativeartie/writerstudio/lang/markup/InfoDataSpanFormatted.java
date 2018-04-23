package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/**
 * Data that stores a {@link FormattedSpan}. Represented in design/ebnf.txt as
 * {@code InfoDataFormatted}.
 */
public final class InfoDataSpanFormatted extends InfoDataSpan{

    private final CacheKeyMain<FormattedSpan> cacheData;

    @Override
    public FormattedSpan getData(){
        return getLocalCache(cacheData, () -> (FormattedSpan)get(0));
    }

    protected InfoDataSpanFormatted(List<Span> children){
        super(children, InfoDataType.FORMATTED);
        cacheData = new CacheKeyMain<>(FormattedSpan.class);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
