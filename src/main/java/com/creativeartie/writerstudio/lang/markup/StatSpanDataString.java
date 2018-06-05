package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.time.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A span to store data. */
public class StatSpanDataString extends StatSpanData<String>{
    private final CacheKeyMain<String> cacheData;

    public StatSpanDataString(List<Span> children){
        super(children);
        cacheData = new CacheKeyMain<>(String.class);
    }

    @Override
    public String getData(){
        return getLocalCache(cacheData, () -> leafFromLast(StyleInfoLeaf.DATA)
            .map(s -> s.getRaw()).orElse("")
        );
    }
}
