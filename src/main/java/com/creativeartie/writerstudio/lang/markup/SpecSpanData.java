package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** A single data about the day. */
public abstract class SpecSpanData<T> extends SpanBranch{
    private CacheKeyMain<StatTypeData> cacheType;

    /** Creates an instance of {@linkplain SpecSpanData}.
     *
     * @param children
     *      span children
     */
    SpecSpanData(List<Span> children){
        super(children);
        cacheType = new CacheKeyMain<>(StatTypeData.class);
    }

    public final StatTypeData getDataType(){
        return getLocalCache(cacheType, () -> leafFromFirst(SpanLeafStyle.FIELD)
            .map( s -> StatTypeData.parse(s.getRaw().trim()) )
            .orElse(StatTypeData.UNKNOWN)
        );
    }

    public abstract T getData();

    void setData(T data){
        runCommand(() -> ((StatParseData)getParser("")).getSymbol() +
            SPEC_KEY_DATA + data + SPEC_SEPARATOR);
    }

    @Override
    protected final SetupParser getParser(String text){
        return StatParseData.values()[getDataType().ordinal()];
    }
}
