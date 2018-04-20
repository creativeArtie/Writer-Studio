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
    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<String> cacheField;
    private Optional<String> cacheValue;

    FormatSpanPointKey(List<Span> children, FormatParsePointKey reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;
    }

    public String getField(){
        cacheField = getCache(cacheField, () -> spanFromFirst(ContentSpan.class)
            .map(s -> s.getTrimmed()).orElse(""));
        return cacheField.get();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(AuxiliaryType.REF_KEY)
                .addAll(super.getBranchStyles()).build();
        });
        return cacheStyles.get();
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
    protected void childEdited(){
        super.childEdited();
        cacheField = Optional.empty();
        cacheStyles = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    protected String toChildString(){
        return getField();
    }
}