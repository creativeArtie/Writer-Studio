package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/** Data storing a {@link ContentSpan} */
public final class InfoDataSpanText extends InfoDataSpan{

    private final CacheKeyMain<ContentSpan> cacheData;

    /** Creates a {@linkplain InfoDataSpanText}.
     *
     * @param children
     *      span children
     * @see InfoDataParser#TEXT
     */
    protected InfoDataSpanText(List<Span> children){
        super(children, InfoDataType.TEXT);
        cacheData = new CacheKeyMain<>(ContentSpan.class);
    }

    @Override
    public ContentSpan getData(){
        return getLocalCache(cacheData, () -> (ContentSpan)get(0));
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
