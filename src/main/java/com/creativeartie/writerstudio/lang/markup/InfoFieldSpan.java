package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

/** Name for fields that store {@link InfoDataSpan data}. */
public final class InfoFieldSpan extends SpanBranch{

    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<InfoFieldType> cacheField;

    /** Creates a {@linkplain InfoFieldSpan}.
     *
     * @param children
     *      span children
     * @see InfoFieldParser#parse(List)
     */
    InfoFieldSpan(List<Span> children){
        super(children);
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheField = new CacheKeyMain<>(InfoFieldType.class);
    }

    /** Gets the field type.
     *
     * @return answer
     */
    public InfoFieldType getInfoFieldType(){
        return getLocalCache(cacheField, () -> {
            Optional<SpanLeaf> found = leafFromFirst(StyleInfoLeaf.FIELD);
            if (found.isPresent()){
                return InfoFieldType.getType(found.get().getRaw());
            }
            return InfoFieldType.ERROR;
        });
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> ImmutableList
            .of(getInfoFieldType()));
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    public String toString(){
        return getInfoFieldType().toString() + ": ";
    }
}
