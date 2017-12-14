package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that store information that needed to be done before published.
 */
public class LinedSpanAgenda extends LinedSpan implements Catalogued{

    LinedSpanAgenda(List<Span> children){
        super(children);
    }

    public Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getAgenda(){
        Optional<ContentSpan> ans = getAgendaSpan();
        return ans.isPresent()? ans.get().getTrimmed() : "";
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return Optional.of(new CatalogueIdentity(TYPE_AGENDA_LINED, this));
    }

    @Override
    public boolean isId(){
        return true;
    }

    @Override
    public int getNoteTotal(){
        return getAgendaSpan().map(span -> span.wordCount())
            .orElse(0);
    }


    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}
