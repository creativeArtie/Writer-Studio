package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that points to a list. This is the base for {@link LinedSpanLevelSection}.
 */
public abstract class LinedSpanLevel extends LinedSpan {
    private Optional<Optional<FormatSpanMain>> cacheFormatted;
    private Optional<Integer> cacheLevel;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanLevel(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(
            FormatSpanMain.class));
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
