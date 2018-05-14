package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** An inline to-do text. */
public final class FormatSpanAgenda extends SpanBranch implements Catalogued{
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.AGENDA);
    private CacheKeyMain<String> cacheAgenda;
    private CacheKeyOptional<CatalogueIdentity> cacheId;

    /** Creates a {@linkplain ContentSpan}.
     *
     * @param children
     *      span children
     * @see FormatParseAgenda#parse(SetupPointer)
     */
    FormatSpanAgenda(List<Span> children){
        super(children);
        cacheAgenda = CacheKeyMain.stringKey();
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
    }

    /** Gets to do details.
     *
     * @return answer
     */
    public String getAgenda(){
        return getLocalCache(cacheAgenda, () -> {
            Optional<ContentSpan> text = spanFromLast(ContentSpan.class);
            if (text.isPresent()){
                return text.get().getTrimmed();
            }
            return "";
        });
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
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return (text.startsWith(CURLY_AGENDA) &&
            AuxiliaryChecker.willEndWith(text, CURLY_END)
        )? FormatParseAgenda.PARSER: null;
    }

    @Override
    public String toString(){
        return "agenda(" + SpanLeaf.escapeText(getAgenda()) + ")";
    }
}
