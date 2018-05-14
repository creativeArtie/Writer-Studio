package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Block quote. */
public class LinedSpanQuote extends LinedSpan {
    private final CacheKeyOptional<FormattedSpan> cacheFormatted;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain LinedSpanQuote}.
     *
     * @param children
     *      span children
     * @see LinedParseRest#QUOTE
     */
    LinedSpanQuote(List<Span> children){
        super(children);
        cacheFormatted = new CacheKeyOptional<>(FormattedSpan.class);
        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
    }

    /** Gets the formatted content.
     *
     * @return answer
     */
    public Optional<FormattedSpan> getFormattedSpan(){
        return getLocalCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
    }

    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () ->
            getFormattedSpan().map(s -> s.getPublishTotal()).orElse(0)
        );
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () ->
            getFormattedSpan().map(s -> s.getNoteTotal()).orElse(0)
        );
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return text.startsWith(LINED_QUOTE) &&
            AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            LinedParseRest.QUOTE: null;
    }

}