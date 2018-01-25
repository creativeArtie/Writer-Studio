package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.google.common.collect.*;

/**
 * Escaped character with the {@link AuxiliaryData#CHAR_ESCAPE}. Represented in
 * design/ebnf.txt as {@code Escape}.
 */
public final class BasicTextEscape extends SpanBranch{

    private Optional<String> cacheEscape;
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.ESCAPE);

    BasicTextEscape(List<Span> children){
        super(children);
        cacheEscape = Optional.empty();
    }

    public String getEscape(){
        cacheEscape = getCache(cacheEscape,
            () -> size() == 2? get(1).getRaw(): "");
        return cacheEscape.get();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void clearLocalCache(){
        cacheEscape = Optional.empty();
    }

    @Override
    protected void clearDocCache(){}
}
