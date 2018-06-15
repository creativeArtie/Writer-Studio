package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** A single data about the day. */
public abstract class SpecSpanField<T> extends SpanBranch{

    private CacheKeyMain<String> cacheData;

    private CacheKeyMain<SpecTypeData> cacheType;

    SpecSpanField(ArrayList<Span> children){
        super(children);
        cacheData = CacheKeyMain.stringKey();
        cacheType = new CacheKeyMain<>(SpecTypeData.class);
    }

    protected static final <T extends SpecTypeKey> T getKey(T[] values){
        String raw = leafFromFirst(SpanLeafStyle.FIELD).map(s -> s.getRaw()).
            orElse("");
        for (T value: values){
            if (raw.endsWith(value.getKeyName())){
                return value;
            }
        }
        return values[values.length - 1];
    }

    public final SpecTypeKey getKey();

    public final String getData(){
        return getLocalCache(cacheData, () -> leafFromLast(SpanLeafStyle.DATA));
    }

    public final SpecTypeData getDataType(){
        return getLocalCache(cacheData, () -> getKey().getDataType());
    }
}
