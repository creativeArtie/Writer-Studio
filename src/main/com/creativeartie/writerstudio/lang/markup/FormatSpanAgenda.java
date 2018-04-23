package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, List, Optional

import com.google.common.collect.*; // ImmutableList

import com.creativeartie.writerstudio.lang.*; // (mabny)

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpan} for to do text. Represented in design/ebnf.txt as
 * {@code FormatAgenda}.
 */
public final class FormatSpanAgenda extends SpanBranch implements Catalogued{
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.AGENDA);
    private CacheKeyMain<String> cacheAgenda;
    private CacheKeyOptional<CatalogueIdentity> cacheId;

    FormatSpanAgenda(List<Span> children){
        super(children);
        cacheAgenda = CacheKey.stringKey();
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
    }

    public Optional<ContentSpan> getAgendaSpan(){
        return spanFromLast(ContentSpan.class);
    }

    public String getAgenda(){
        return getLocalCache(cacheAgenda, () -> {
            Optional<ContentSpan> text = getAgendaSpan();
            if (text.isPresent()){
                return text.get().getTrimmed();
            }
            return "";
        });
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getDocCache(cacheId, () -> Optional.of(new CatalogueIdentity(
            TYPE_AGENDA_INLINE, this)));
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
    public String toString(){
        return "agenda(" + SpanLeaf.escapeText(getAgenda()) + ")";
    }
}
