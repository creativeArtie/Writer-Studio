package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/**
 * Data that stores a {@link FormatSpanMain}. Represented in design/ebnf.txt as
 * {@code InfoDataFormatted}.
 */
public final class InfoDataSpanFormatted extends InfoDataSpan{

    private Optional<FormatSpanMain> cacheData;

    @Override
    public FormatSpanMain getData(){
        cacheData = getCache(cacheData, () -> (FormatSpanMain)get(0));
        return cacheData.get();
    }

    protected InfoDataSpanFormatted(List<Span> children){
        super(children, InfoDataType.FORMATTED);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cacheData = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}