package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A span to store integer data. */
public final class SpecStatDataInt extends SpecStatData<Integer>{

    private final CacheKeyMain<Integer> cacheData;

    /** Creates an instance of {@linkplain SpecStatDataInt}.
     *
     * @param children
     *      span children
     */
    public SpecStatDataInt(List<Span> children){
        super(children);
        cacheData = new CacheKeyMain<>(Integer.class);
    }

    @Override
    public Integer getData(){
        return getLocalCache(cacheData, () -> leafFromLast(StyleInfoLeaf.DATA)
            .map( s -> Integer.parseInt(s.getRaw()) ).orElse(0)
        );
    }
}
