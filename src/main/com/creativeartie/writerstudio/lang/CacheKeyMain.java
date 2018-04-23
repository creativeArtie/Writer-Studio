package com.creativeartie.writerstudio.lang;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public final class CacheKeyMain<T> extends CacheKey {

    private Class<T> valueCaster;

    public CacheKeyMain(Class<T> caster){
        valueCaster = argumentNotNull(caster, "caster");
    }

    public T cast(Object value){
        return valueCaster.cast(value);
    }
}