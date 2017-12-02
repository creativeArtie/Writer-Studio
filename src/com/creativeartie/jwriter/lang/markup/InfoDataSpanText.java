package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.creativeartie.jwriter.property.*;

import com.creativeartie.jwriter.lang.*;

public class InfoDataSpanText extends InfoDataSpan{

    @Override
    public ContentSpan getData(){
        return (ContentSpan)get(0);
    }

    InfoDataSpanText(List<Span> children){
        super(children, InfoDataType.TEXT);
    }

    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}
