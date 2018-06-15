package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** A single data about the day. */
public final class StatSpanField extends SpecSpanField<StatTypeKey> {

    private CacheKeyMain<StatTypeKey> cacheKey;

    StatSpanField(ArrayList<Span> children){
        super(children);
        cacheKey = new CacheKeyMain<>(StatTypeKey.class);
    }

    public synchronized void setData(Object data){
        runCommand(() ->  SPEC_SEPARATOR + getKey().getKeyName() +
            SPEC_KEY_DATA + data)
    }

    public SpecTypeKey getKey(){
        return getLocalCache(cacheKey, () -> getKey(StatTypeKey.values()));
    }

}
