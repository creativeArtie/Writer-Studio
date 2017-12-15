package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.creativeartie.jwriter.property.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Data that stores a {@link ContentSpan}.
 */
public final class InfoDataSpanText extends InfoDataSpan{

    @Override
    public ContentSpan getData(){
        return (ContentSpan)get(0);
    }

    InfoDataSpanText(List<Span> children){
        super(children, InfoDataType.TEXT);
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
