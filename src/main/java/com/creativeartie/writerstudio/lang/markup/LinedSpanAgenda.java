package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A to do reminder line.*/
public class LinedSpanAgenda extends LinedSpan implements Catalogued{

    private final CacheKeyMain<String> cacheAgenda;
    private final CacheKeyMain<Integer> cacheNote;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;

    /** Creates a {@linkplain LinedSpanAgenda}.
     *
     * @param children
     *      span children
     * @see LinedParseRest#AGENDA
     */
    LinedSpanAgenda(List<Span> children){
        super(children);
        cacheAgenda = CacheKeyMain.stringKey();
        cacheNote = CacheKeyMain.integerKey();
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
    }

    /** Get the agenda text.
     *
     * @return answer.
     */
    public String getAgenda(){
        return getLocalCache(cacheAgenda, () -> spanFromLast(ContentSpan.class)
            .map(s -> s.getTrimmed()).orElse("")
        );
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            return getAgendaSpan().map(s -> s.getWordCount())
                .orElse(0);
        });
    }

    /** Get the agenda span.
     *
     * @return answer.
     */
    private Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
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
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return text.startsWith(LINED_AGENDA) &&
            AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            LinedParseRest.AGENDA: null;
    }
}
