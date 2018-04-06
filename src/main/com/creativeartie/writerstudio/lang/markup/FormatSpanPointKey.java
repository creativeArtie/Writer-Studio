package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.writerstudio.lang.*;

/**
 * A {@linkplain FormatSpan} meta data and stats.
 * Represented in design/ebnf.txt as {@code FormatKey}.
 */
public final class FormatSpanPointKey extends FormatSpan{
    private final FormatParsePointId spanReparser;
    private Optional<String> cacheField;
    private Optional<String> cacheValue;

    FormatSpanPointKey(List<Span> children, FormatParsePointId reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;
    }

    public String getField(){
        cacheField = getCache(cacheField, () -> spanFromFirst(ContentSpan.class)
            .map(s -> s.getTrimmed()).orElse(""));
        return cacheField.get();
    }

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
    }

    @Override
    protected void docEdited(){}

    @Override
    protected String toChildString(){
        return getField();
    }
}