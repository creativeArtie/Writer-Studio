package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that have level. This is the base for {@link LinedSpanLevelSection}, and
 * {@link LinedSpanLevelList}.
 */
public abstract class LinedSpanLevel extends LinedSpan {
    private final CacheKeyOptional<FormattedSpan> cacheFormatted;
    private final CacheKeyMain<Integer> cacheLevel;

    LinedSpanLevel(List<Span> children){
        super(children);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
        cacheLevel = CacheKey.integerKey();
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class).orElse(null));
    }

    public int getLevel(){
        return getLocalCache(cacheLevel, () -> {
            int extras = getLinedType() == LinedType.OUTLINE? 1: 0;
            return get(0).getRaw().length() - extras;
        });
    }
}
