package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Base class for all {@link InfoDataSpan*} classes.
 */
public abstract class InfoDataSpan extends SpanBranch{

    private final CacheKeyList<StyleInfo> cacheList;

    public abstract SpanBranch getData();

    private final InfoDataType dataType;

    protected InfoDataSpan(List<Span> children, InfoDataType type){
        super(children);
        dataType = checkNotNull(type, "type");
        cacheList = new CacheKeyList<>(StyleInfo.class);
    }

    public final InfoDataType getDataType(){
        return dataType;
    }

    @Override
    public final List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheList, () -> ImmutableList.of(dataType));
    }

    @Override
    public String toString(){
        return "Data%%" + getDataType() + " " + getData() + "%%";
    }
}
