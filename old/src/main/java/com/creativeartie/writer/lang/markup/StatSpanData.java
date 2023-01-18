package com.creativeartie.writer.lang.markup;

import java.util.*;
import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;

/** A single data about the day. */
public abstract class StatSpanData<T> extends SpanBranch{
    private CacheKeyMain<StatTypeData> cacheType;

    /** Creates an instance of {@linkplain StatSpanData}.
     *
     * @param children
     *      span children
     */
    StatSpanData(List<Span> children){
        super(children);
        cacheType = new CacheKeyMain<>(StatTypeData.class);
    }

    public final StatTypeData getDataType(){
        return getLocalCache(cacheType, () -> leafFromFirst(SpanLeafStyle.FIELD)
            .map( s -> StatTypeData.parse(s.getRaw().trim()) )
            .orElse(StatTypeData.UNKNOWN)
        );
    }

    public abstract T getData();

    void setData(T data){
        runCommand(() -> ((StatParseData)getParser("")).getSymbol() +
            STAT_KEY_DATA + data + STAT_SEPARATOR);
    }

    @Override
    protected final SetupParser getParser(String text){
        return StatParseData.values()[getDataType().ordinal()];
    }
}
