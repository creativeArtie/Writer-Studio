package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that store information that needed to be done before published.
 * Represented in design/ebnf.txt as {@code LinedAgenda}.
 */
public class LinedSpanAgenda extends LinedSpan implements Catalogued{

    Optional<String> cacheAgenda;
    Optional<Integer> cacheNote;
    Optional<Optional<CatalogueIdentity>> cacheId;

    LinedSpanAgenda(List<Span> children){
        super(children);
    }

    public Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getAgenda(){
        cacheAgenda = getCache(cacheAgenda, () -> {
            Optional<ContentSpan> ans = getAgendaSpan();
            return ans.isPresent()? ans.get().getTrimmed() : "";
        });
        return cacheAgenda.get();
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        cacheId = getCache(cacheId, () -> {
            return Optional.of(new CatalogueIdentity(TYPE_AGENDA_LINED, this));
        });
        return cacheId.get();
    }

    @Override
    public boolean isId(){
        return true;
    }

    @Override
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> {
            return getAgendaSpan().map(span -> span.wordCount())
                .orElse(0);
        });
        return cacheNote.get();
    }


    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINED_AGENDA) &&
            BasicParseText.checkLineEnd(isLast(), text)?
            LinedParseRest.AGENDA: null;
    }

    @Override
    protected void childEdited(){
        cacheAgenda = Optional.empty();
        cacheNote = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }
}
