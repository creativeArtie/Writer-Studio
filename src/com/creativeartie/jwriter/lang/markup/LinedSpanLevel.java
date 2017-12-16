package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that points to a list. This is the base for {@link LinedSpanSection}.
 */
public class LinedSpanLevel extends LinedSpan {
    private LinedParseLevel spanReparser;
    private Optional<Optional<FormatSpanMain>> cacheFormatted;
    private Optional<Integer> cacheLevel;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanLevel(List<Span> children, LinedParseLevel reparser){
        super(children);
        spanReparser = checkNotNull(reparser, "reparser");
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        cacheFormatted = getCache(cacheFormatted, () -> spanFromLast(FormatSpanMain
            .class));
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
    public int getPublishTotal(){
        cachePublish = getCache(cachePublish, () -> getFormattedSpan()
            .map(span -> span.getPublishTotal()).orElse(0));
        return cachePublish.get();
    }

    @Override
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> getFormattedSpan()
            .map(span -> span.getNoteTotal()).orElse(0)
        );
        return cacheNote.get();
    }


    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(getLevelToken(spanReparser, getLevel())) &&
            BasicParseText.checkLineEnd(isLast(), text)?
            spanReparser: null;

    }

    @Override
    protected void childEdited(){
        cacheFormatted = Optional.empty();
        cacheLevel = Optional.empty();
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
