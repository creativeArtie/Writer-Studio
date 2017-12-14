package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpan} for to do text.
 */
public final class FormatSpanAgenda extends SpanBranch implements Catalogued{
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.AGENDA);
    private Optional<String> cacheAgenda;
    private Optional<Optional<CatalogueIdentity>> cacheId;

    FormatSpanAgenda(List<Span> children){
        super(children);
        cacheId = Optional.empty();
        clearCache();
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
        return text.startsWith(CURLY_AGENDA)? FormatParseAgenda.PARSER: null;
    }

    @Override
    protected void childEdited(){
        clearCache();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }
    /**
     * Set all cache to empty. Helper method of
     * {@link #EditionSpan(List, ContentParser)} and {@link #childEdited()}.
     */
    private void clearCache(){
        cacheAgenda = Optional.empty();
    }
}
