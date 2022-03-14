package com.creativeartie.writer.lang.markup;

import java.util.*;
import java.time.*;

import com.creativeartie.writer.lang.*;

/** A span to store {@linkplain Duration} data. */
public class StatSpanDataTime extends StatSpanData<Duration>{
    private final CacheKeyMain<Duration> cacheData;

    /** Creates an instance of {@linkplain StatSpanDataTime}.
     *
     * @param children
     *      span children
     */
    public StatSpanDataTime(List<Span> children){
        super(children);
        cacheData = new CacheKeyMain<>(Duration.class);
    }

    @Override
    public Duration getData(){
        return getLocalCache(cacheData, () -> leafFromLast(SpanLeafStyle.DATA)
            .map(s -> Duration.parse(s.getRaw())).orElse(Duration.ofSeconds(0))
        );
    }
}
