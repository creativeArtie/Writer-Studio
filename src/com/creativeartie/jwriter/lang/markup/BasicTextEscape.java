package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.google.common.collect.*;

/**
 * Escaped character with the {@link AuxiliaryData#CHAR_ESCAPE}.
 */
public class BasicTextEscape extends SpanBranch{

    private Optional<String> escape;

    BasicTextEscape(List<Span> children){
        super(children);
        escape = Optional.empty();
    }

    public String getEscape(){
        if (! escape.isPresent()){
            escape = Optional.of(size() == 2? get(1).getRaw(): "");
        }
        return escape.get();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(AuxiliaryType.ESCAPE);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){
        escape = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
