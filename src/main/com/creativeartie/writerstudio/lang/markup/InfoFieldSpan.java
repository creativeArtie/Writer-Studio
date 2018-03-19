package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

/**
 * Name for fields that store {@link InfoDataSpan data}. Represented in
 * design/ebnf.txt as {@code InfoFieldSource}, {@code InfoFieldFootnote},
 * {@code InfoFieldInText}, {@code InfoFieldError}.
 */
public final class InfoFieldSpan extends SpanBranch{


    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<InfoFieldType> cacheField;

    InfoFieldSpan(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () -> ImmutableList
            .of(getFieldType()));
        return cacheStyles.get();
    }

    public InfoFieldType getFieldType(){
        cacheField = getCache(cacheField, () -> {
            Optional<SpanLeaf> found = leafFromFrist(StyleInfoLeaf.FIELD);
            if (found.isPresent()){
                return InfoFieldType.parseText(found.get().getRaw());
            }
            return InfoFieldType.ERROR;
        });
        return cacheField.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){
        cacheStyles = Optional.empty();
        cacheField = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    public String toString(){
        return getFieldType().toString() + ": ";
    }
}
