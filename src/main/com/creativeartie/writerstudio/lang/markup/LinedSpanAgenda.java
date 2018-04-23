package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Line that store information that needed to be done before published.
 * Represented in design/ebnf.txt as {@code LinedAgenda}.
 */
public class LinedSpanAgenda extends LinedSpan implements Catalogued{

    private final CacheKeyMain<String> cacheAgenda;
    private final CacheKeyMain<Integer> cacheNote;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;

    LinedSpanAgenda(List<Span> children){
        super(children);
        cacheAgenda = CacheKey.stringKey();
        cacheNote = CacheKey.integerKey();
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
    }

    public Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getAgenda(){
        return getLocalCache(cacheAgenda, () -> {
            Optional<ContentSpan> ans = getAgendaSpan();
            return ans.isPresent()? ans.get().getTrimmed() : "";
        });
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getDocCache(cacheId, () ->
            Optional.of(new CatalogueIdentity(TYPE_AGENDA_LINED, this))
        );
    }

    @Override
    public boolean isId(){
        return true;
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            return getAgendaSpan().map(span -> span.wordCount())
                .orElse(0);
        });
    }


    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINED_AGENDA) &&
            AuxiliaryChecker.checkLineEnd(text, isLast())?
            LinedParseRest.AGENDA: null;
    }
}
