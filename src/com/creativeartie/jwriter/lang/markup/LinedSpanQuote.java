package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a block quote.
 */
public class LinedSpanQuote extends LinedSpan {
    private Optional<Optional<FormatSpanMain>> cacheFormatted;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanQuote(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanAtFirst(
            FormatSpanMain.class));
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
            BasicParseText.checkLineEnd(isLast(), text)?
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