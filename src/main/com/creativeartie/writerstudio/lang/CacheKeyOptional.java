package com.creativeartie.writerstudio.lang;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public final class CacheKeyOptional<T> extends CacheKey {

    private Class<T> valueCaster;

    public CacheKeyOptional(Class<T> caster){
        valueCaster = argumentNotNull(caster, "caster");
    }

    public Optional<T> cast(Object value){
        if (value instanceof Optional){
            value = ((Optional<?>)value).orElse(null);
        }
        return Optional.ofNullable(valueCaster.cast(value));
    }
}