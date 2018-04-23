package com.creativeartie.writerstudio.lang;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public final class CacheKeyList<T> extends CacheKey {

    private Class<T> valueCaster;

    public CacheKeyList(Class<T> caster){
        valueCaster = caster;
    }


    List<T> cast(List<?> value){
        ArrayList<T> ans = new ArrayList<>();
        int i = 0;
        for (Object item : value){
            ans.add(argumentIsInstance(item, "value.get(" + i + ")",
                valueCaster));
            i++;
        }
        return ans;
    }
}