package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line representing a basic paragraph.
 */
public class LinedSpanParagraph extends LinedSpan {

    private Optional<Optional<FormatSpanMain>> cacheFormatted;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanParagraph(List<Span> children){
        super(children);
    }

    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanAtFirst(FormatSpanMain.class);
    }

    @Override
    public int getPublishTotal(){
        cachePublish = getCache(cachePublish, () ->
            getFormattedSpan().map(span -> span.getPublishTotal()).orElse(0));
        return cachePublish.get();
    }

    @Override
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () ->
            getFormattedSpan().map(span -> span.getNoteTotal()).orElse(0));
        return cacheNote.get();
    }

    @Override
    protected SetupParser getParser(String text){
        for (String token: getLinedTokens()){
            if (text.startsWith(token)){
                return null;
            }
        }
        return BasicParseText.checkLineEnd(isLast(), text)?
            LinedParseRest.PARAGRAPH: null;
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
