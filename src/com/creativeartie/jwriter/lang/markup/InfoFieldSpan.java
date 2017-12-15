package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.Optional;
import com.google.common.collect.*;
import com.google.common.base.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Name for fields that store {@link InfoDataSpan data}.
 */
public final class InfoFieldSpan extends SpanBranch{

    InfoFieldSpan(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(getFieldType());
    }

    public InfoFieldType getFieldType(){
        Optional<SpanLeaf> found = leafFromFrist(StyleInfoLeaf.FIELD);
        if (found.isPresent()){
            String name = CaseFormat.LOWER_HYPHEN
                .to(CaseFormat.UPPER_UNDERSCORE,
                found.get().getRaw().trim());
            try {
                return InfoFieldType.valueOf(name);
            } catch (IllegalArgumentException ex){
                /// return InfoFieldType.ERROR;
            }
        }
        return InfoFieldType.ERROR;
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
