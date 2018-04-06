package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

/**
 * Data that stores a {@link DirectorySpan} for notes. Represented in
 * design/ebnf.txt as {@code InfoDataRef}.
 */
public final class InfoDataSpanRef extends InfoDataSpan{

    private Optional<DirectorySpan> cacheData;

    protected InfoDataSpanRef(List<Span> children){
        super(children, InfoDataType.NOTE_REF);
    }

    @Override
    public DirectorySpan getData(){
        cacheData = getCache(cacheData, () -> (DirectorySpan)get(0));
        return cacheData.get();
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
