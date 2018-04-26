package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List, Optional

import com.creativeartie.writerstudio.lang.*; // Span, SpanBranch, StyleInfo

import com.google.common.collect.*; // ImmutableList

/**
 * Escaped character with the {@link AuxiliaryData#TOKEN_ESCAPE}. Represented in
 * design/ebnf.txt as {@code Escape}.
 */
public final class BasicTextEscape extends SpanBranch{

    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.ESCAPE);

    private final CacheKeyMain<String> cacheEscape;

    BasicTextEscape(List<Span> children){
        super(children);
        cacheEscape = CacheKeyMain.stringKey();
    }

    public String getEscape(){
        return getLocalCache(cacheEscape, () -> size() == 2? get(1).getRaw():
            "");
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
