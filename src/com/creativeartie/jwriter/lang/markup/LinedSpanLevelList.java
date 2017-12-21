package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that points to a list. This is the base for {@link LinedSpanLevelSection}.
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
        // TODO editRaw
        return null;
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
