package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/** Data storing a {@link DirectorySpan}. */
public final class InfoDataSpanRef extends InfoDataSpan{

    private final CacheKeyMain<DirectorySpan> cacheData;

    /** Creates a {@linkplain InfoDataSpanRef}.
     * @param children
     *      span children
     * @param type
     *      data type
     * @see InfoDataParser#NOTE_REF
     */
    protected InfoDataSpanRef(List<Span> children, InfoDataType type){
        super(children, type);
        cacheData = new CacheKeyMain<>(DirectorySpan.class);
    }

    @Override
    public DirectorySpan getData(){
        return getLocalCache(cacheData, () -> (DirectorySpan)get(0));
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
