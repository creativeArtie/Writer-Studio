package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public class LinedSpanQuote extends LinedSpan {
    private Optional<Optional<FormattedSpan>> cacheFormatted;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanQuote(List<Span> children){
        super(children);
    }

    public Optional<FormattedSpan> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(
            FormattedSpan.class));
        return cacheFormatted.get();
    }

    @Override
    public int getPublishTotal(){
        cachePublish = getCache(cachePublish, () ->
            getFormattedSpan().map(span -> span.getPublishTotal()).orElse(0)
        );
        return cachePublish.get();
    }

    @Override
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () ->
            getFormattedSpan().map(span -> span.getNoteTotal()).orElse(0)
        );
        return cacheNote.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINED_QUOTE) &&
            AuxiliaryChecker.checkLineEnd(isLast(), text)?
            LinedParseRest.QUOTE: null;
    }

    @Override
    protected void childEdited(){
        cacheFormatted = Optional.empty();
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}