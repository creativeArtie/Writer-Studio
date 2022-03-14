package com.creativeartie.writer.lang.markup;

import java.util.*;
import com.creativeartie.writer.lang.*;

/** A span to store integer data. */
public final class StatSpanDataInt extends StatSpanData<Integer>{

    private final CacheKeyMain<Integer> cacheData;

    /** Creates an instance of {@linkplain StatSpanDataInt}.
     *
     * @param children
     *      span children
     */
    public StatSpanDataInt(List<Span> children){
        super(children);
        cacheData = new CacheKeyMain<>(Integer.class);
    }

    @Override
    public Integer getData(){
        return getLocalCache(cacheData, () -> leafFromLast(SpanLeafStyle.DATA)
            .map( s -> Integer.parseInt(s.getRaw()) ).orElse(0)
        );
    }
}
