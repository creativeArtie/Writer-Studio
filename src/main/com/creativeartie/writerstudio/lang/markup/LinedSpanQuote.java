package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public class LinedSpanQuote extends LinedSpan {
    private final CacheKeyOptional<FormattedSpan> cacheFormatted;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    LinedSpanQuote(List<Span> children){
        super(children);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
        cachePublish = CacheKey.integerKey();
        cacheNote = CacheKey.integerKey();
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () ->
            getFormattedSpan().map(span -> span.getPublishTotal()).orElse(0)
        );
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () ->
            getFormattedSpan().map(span -> span.getNoteTotal()).orElse(0)
        );
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINED_QUOTE) &&
            AuxiliaryChecker.checkLineEnd(text, isLast())?
            LinedParseRest.QUOTE: null;
    }

}