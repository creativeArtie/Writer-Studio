package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * A {@linkplain FormatSpanCurly} for to do text. It will warn user when
 * exporting while there are these {@link Span spans} still exists.
 */
public final class FormatSpanAgenda extends SpanBranch implements Catalogued{

    FormatSpanAgenda(List<Span> children){
        super(children);
    }

    public Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getAgenda(){
        Optional<ContentSpan> text = getAgendaSpan();
        if (text.isPresent()){
            return text.get().getParsed();
        }
        return "";
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        return ImmutableList.of(AuxiliaryType.AGENDA);
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return Optional.of(new CatalogueIdentity(TYPE_AGENDA_INLINE, this));
    }

    @Override
    public boolean isId(){
        return true;
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
