package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Base class for all {@link InfoDataSpan*} classes.
 */
public abstract class InfoDataSpan extends SpanBranch{

    private Optional<List<StyleInfo>> cacheList;

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
        cacheList = getCache(cacheList, () -> ImmutableList.of(dataType));
        return cacheList.get();
    }

    @Override
    public String toString(){
        return "{" + getData() + "}";
    }

    @Override
    protected void childEdited(){
        cacheList = Optional.empty();
    }
}
