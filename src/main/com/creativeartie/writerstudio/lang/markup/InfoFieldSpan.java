package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

/**
 * Name for fields that store {@link InfoDataSpan data}. Represented in
 * design/ebnf.txt as {@code InfoFieldSource}, {@code InfoFieldFootnote},
 * {@code InfoFieldInText}, {@code InfoFieldError}.
 */
public final class InfoFieldSpan extends SpanBranch{

    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<InfoFieldType> cacheField;

    InfoFieldSpan(List<Span> children){
        super(children);
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheField = new CacheKeyMain<>(InfoFieldType.class);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> ImmutableList
            .of(getFieldType()));
    }

    public InfoFieldType getFieldType(){
        return getLocalCache(cacheField, () -> {
            Optional<SpanLeaf> found = leafFromFirst(StyleInfoLeaf.FIELD);
            if (found.isPresent()){
                return InfoFieldType.parseText(found.get().getRaw());
            }
            return InfoFieldType.ERROR;
        });
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    public String toString(){
        return getFieldType().toString() + ": ";
    }
}
