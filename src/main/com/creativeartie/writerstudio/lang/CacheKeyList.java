package com.creativeartie.writerstudio.lang;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public final class CacheKeyList<T> extends CacheKey {

    private Class<T> valueCaster;

    public CacheKeyList(Class<T> caster){
        valueCaster = caster;
    }


    List<T> cast(Object value){
        List<?> list = argumentIsInstance(value, "value", List.class);
        ArrayList<T> ans = new ArrayList<>();
        for (Object item : list){
            ans.add(argumentIsInstance(item, "value's item", valueCaster));
        }
        return ans;
    }
}