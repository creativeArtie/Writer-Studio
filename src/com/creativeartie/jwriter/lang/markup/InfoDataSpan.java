package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Base class for all {@link InfoDataSpan*} classes.
 */
public abstract class InfoDataSpan extends SpanBranch{

    public abstract SpanBranch getData();

    private final InfoDataType dataType;

    protected InfoDataSpan(List<Span> children, InfoDataType type){
        super(children);
        dataType = checkNotNull(type, "type");
    }

    public final InfoDataType getDataType(){
        return dataType;
    }

    @Override
    public final List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(dataType);
    }

    @Override
    public String toString(){
        return "{" + getData() + "}";
    }
}
