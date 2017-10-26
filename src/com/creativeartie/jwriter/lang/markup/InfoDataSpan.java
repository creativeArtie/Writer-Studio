package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.main.Checker;

public abstract class InfoDataSpan<T> extends SpanBranch{
    
    public abstract InfoDataSpan<T> cast();
    
    public abstract T getData();
    
    private final InfoDataType dataType;
    
    protected InfoDataSpan(List<Span> children, InfoDataType type){
        super(children);
        dataType = Checker.checkNotNull(type, "type");
    }
    
    public InfoDataType getDataType(){
        return dataType;
    }
    
    @Override
    public List<DetailStyle> getBranchStyles(){
        return ImmutableList.of(dataType);
    }
    
    @Override
    public String toString(){
        return "{" + getData() + "}";
    }
}
