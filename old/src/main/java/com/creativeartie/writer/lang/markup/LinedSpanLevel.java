package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;

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

    /** Gets the formatted content.
     *
     * @return answer
     */
    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    /** Get the line level.
     *
     * This is from {@code 1} to {@link AuxiliaryData#LEVEL_MAX}.
     *
     * @return answer
     */
    public int getLevel(){
        return getLocalCache(cacheLevel, () -> {
            /// Where only outline line has an extra "!"
            int extras = getRaw().startsWith(LINED_BEGIN)? 1: 0;
            /// for everything else is just the length of text
            return get(0).getRaw().length() - extras;
        });
    }
}
