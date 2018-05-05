package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Line with levels like list and headings.*/
public abstract class LinedSpanLevel extends LinedSpan {
    private final CacheKeyOptional<FormattedSpan> cacheFormatted;
    private final CacheKeyMain<Integer> cacheLevel;

    /** Creates a {@linkplain LinedSpanLevel}.
     *
     * @param children
     *      span children
     */
    LinedSpanLevel(List<Span> children){
        super(children);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
        cacheLevel = CacheKeyMain.integerKey();
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    public int getLevel(){
        return getLocalCache(cacheLevel, () -> {
            int extras = getLinedType() == LinedType.OUTLINE? 1: 0;
            return get(0).getRaw().length() - extras;
        });
    }
}
