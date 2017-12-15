package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Data that stores a {@link FormatSpanMain}.
 */
public final class InfoDataSpanFormatted extends InfoDataSpan{

    @Override
    public FormatSpanMain getData(){
        return (FormatSpanMain)get(0);
    }

    protected InfoDataSpanFormatted(List<Span> children){
        super(children, InfoDataType.FORMATTED);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){}

    @Override
    protected void docEdited(){}
}
