package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that points to a list. Represented in design/ebnf.txt as
 * {@code LinedNumbered}, and {@link LinedBullet}.
 */
public class LinedSpanLevelList extends LinedSpanLevel {
    private LinedParseLevel spanReparser;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;

    LinedSpanLevelList(List<Span> children){
        super(children);
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
        if (AuxiliaryChecker.checkLineEnd(isLast(), text)){
            if (getParser(text, LinedParseLevel.BULLET)){
                return LinedParseLevel.BULLET;
            } else if (getParser(text, LinedParseLevel.NUMBERED)){
                return LinedParseLevel.NUMBERED;
            }
        }
        return null;
    }

    /**
     * Check if text is parseable by a parser. Helper method of
     * {@link #getParser(String)}
     */
    private boolean getParser(String text, LinedParseLevel parser){
        for (String token: getLevelToken(parser)){
            if (text.startsWith(token)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
