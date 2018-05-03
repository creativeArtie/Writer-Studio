package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/** Data storing a {@link FormattedSpan}. */
public final class InfoDataSpanFormatted extends InfoDataSpan{

    private final CacheKeyMain<FormattedSpan> cacheData;

    /** Creates a {@linkplain InfoDataSpanFormatted}.
     * @param children
     *      span children
     * @see InfoDataParser#FORMATTED
     */
    protected InfoDataSpanFormatted(List<Span> children){
        super(children, InfoDataType.FORMATTED);
        cacheData = new CacheKeyMain<>(FormattedSpan.class);
    }

    @Override
    public FormattedSpan getData(){
        return getLocalCache(cacheData, () -> (FormattedSpan)get(0));
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
