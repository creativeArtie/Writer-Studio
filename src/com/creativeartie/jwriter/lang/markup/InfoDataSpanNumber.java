package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.lang.*;

@Deprecated
public class InfoDataSpanNumber extends InfoDataSpan<Integer>{

    @Override
    public InfoDataSpanNumber cast(){
        return this;
    }

    @Override
    public Integer getData(){
        String raw = CharMatcher.whitespace().removeFrom(get(0).getRaw());
        try{
            return Integer.parseInt(raw);
        } catch (NumberFormatException ex){
            throw new IllegalStateException("No integer found.", ex);
        }
    }

    InfoDataSpanNumber(List<Span> children){
        super(children, InfoDataType.NUMBER);
    }
}
