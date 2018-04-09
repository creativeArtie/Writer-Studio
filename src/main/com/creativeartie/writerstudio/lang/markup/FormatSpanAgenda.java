package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpan} for to do text. Represented in design/ebnf.txt as
 * {@code FormatAgenda}.
 */
public final class FormatSpanAgenda extends SpanBranch implements Catalogued{
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.AGENDA);
    private Optional<String> cacheAgenda;
    private Optional<Optional<CatalogueIdentity>> cacheId;

    FormatSpanAgenda(List<Span> children){
        super(children);
        cacheId = Optional.empty();
    }

    public Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getAgenda(){
        cacheAgenda = getCache(cacheAgenda, () -> {
            Optional<ContentSpan> text = getAgendaSpan();
            if (text.isPresent()){
                return text.get().getTrimmed();
            }
            return "";
        });
        return cacheAgenda.get();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        cacheId = getCache(cacheId, () -> Optional.of(new CatalogueIdentity(
            TYPE_AGENDA_INLINE, this)));
        return cacheId.get();
    }

    @Override
    public boolean isId(){
        return true;
    }

    @Override
    protected SetupParser getParser(String text){
        return (text.startsWith(CURLY_AGENDA) &&
            AuxiliaryChecker.willEndWith(text, CURLY_END)
        )? FormatParseAgenda.PARSER: null;
    }

    @Override
    protected void childEdited(){
        cacheAgenda = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }

    @Override
    public String toString(){
        return "agenda(" + SpanLeaf.escapeText(getAgenda()) + ")";
    }
}
