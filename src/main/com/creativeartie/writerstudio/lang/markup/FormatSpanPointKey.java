package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.writerstudio.lang.*;

/**
 * A {@linkplain FormatSpan} meta data and stats.
 * Represented in design/ebnf.txt as {@code FormatKey}.
 */
public final class FormatSpanPointKey extends FormatSpan{
    private final FormatParsePointKey spanReparser;
    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<String> cacheField;
    private final CacheKeyMain<String> cacheValue;

    FormatSpanPointKey(List<Span> children, FormatParsePointKey reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;

        cacheField = CacheKeyMain.stringKey();
        cacheValue = CacheKeyMain.stringKey();
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
    }

    public String getField(){
        return getLocalCache(cacheField, () -> spanFromFirst(ContentSpan.class)
            .map(s -> s.getTrimmed()).orElse(""));
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(AuxiliaryType.REF_KEY)
                .addAll(super.getBranchStyles()).build();
        });
    }

    @Override
    public String getOutput(){
        return "";
    }

    @Override
    protected SetupParser getParser(String text){
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected String toChildString(){
        return getField();
    }
}