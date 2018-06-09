package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** A single data about the day. */
public abstract class SpecSpanData<T> extends SpanBranch{
    private CacheKeyList<StyleInfo> cacheInfoStyle;
    private CacheKeyMain<StatTypeData> cacheType;

    /** Creates an instance of {@linkplain SpecSpanData}.
     *
     * @param children
     *      span children
     */
    SpecSpanData(List<Span> children){
        super(children);
        cacheInfoStyle = new CacheKeyList<>(StyleInfo.class);
        cacheType = new CacheKeyMain<>(StatTypeData.class);
    }

    public final StatTypeData getDataType(){
        return getLocalCache(cacheType, () -> leafFromFirst(StyleInfoLeaf.FIELD)
            .map( s -> StatTypeData.parse(s.getRaw().trim()) )
            .orElse(StatTypeData.UNKNOWN)
        );
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheInfoStyle,
            () -> ImmutableList.of(getDataType())
        );
    }

    public abstract T getData();

    void setData(T data){
        runCommand(() -> ((StatParseData)getParser("")).getSymbol() +
            STAT_DATA_SEP + data + STAT_SEPARATOR);
    }

    @Override
    protected final SetupParser getParser(String text){
        return StatParseData.values()[getDataType().ordinal()];
    }
}
