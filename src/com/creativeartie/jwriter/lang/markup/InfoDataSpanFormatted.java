package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Data that stores a {@link FormatSpanMain}.
 */
public class InfoDataSpanFormatted extends InfoDataSpan{

    @Override
    public FormatSpanMain getData(){
        return spanAtFirst(FormatSpanMain.class).get();
    }

    protected InfoDataSpanFormatted(List<Span> children){
        super(children, InfoDataType.FORMATTED);
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
