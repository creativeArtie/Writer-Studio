package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

public class LinedSpanAgenda extends LinedSpan implements Catalogued{

    LinedSpanAgenda(List<Span> children){
        super(children);
    }

    public Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getAgenda(){
        Optional<ContentSpan> ans = getAgendaSpan();
        return ans.isPresent()? ans.get().getParsed() : "";
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
    public int getNoteCount(){
        return getAgendaSpan().map(span -> span.wordCount())
            .orElse(new Integer(0)).intValue();
    }
}
