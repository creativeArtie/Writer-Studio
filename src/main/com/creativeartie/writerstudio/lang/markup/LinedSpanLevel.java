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
    private Optional<Optional<FormattedSpan>> cacheFormatted;
    private Optional<Integer> cacheLevel;

    LinedSpanLevel(List<Span> children){
        super(children);
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
        return cacheFormatted.get();
    }

    public int getLevel(){
        cacheLevel = getCache(cacheLevel, () -> {
            int extras = getLinedType() == LinedType.OUTLINE? 1: 0;
            return get(0).getRaw().length() - extras;
        });
        return cacheLevel.get();
    }

    @Override
    protected void childEdited(){
        cacheFormatted = Optional.empty();
        cacheLevel = Optional.empty();
    }
}
