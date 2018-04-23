package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpanLink} with path located somewhere in the document.
 * Represented in design/ebnf.txt as {@code FormatRefLink}.
 */
public final class FormatSpanLinkRef extends FormatSpanLink
        implements Catalogued{

    private final FormatParseLinkRef spanReparser;
    private final CacheKeyOptional<SpanBranch> cachePath;
    private final CacheKeyMain<String> cacheText;
    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyMain<Boolean> cacheExternal;

    FormatSpanLinkRef(List<Span> children, FormatParseLinkRef reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;

        cachePath = new CacheKeyOptional<>(SpanBranch.class);
        cacheText = CacheKey.stringKey();
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheExternal = CacheKey.booleanKey();
    }

    @Override
    public Optional<SpanBranch> getPathSpan(){
        return getLocalCache(cachePath, () ->{
            Optional<CatalogueIdentity> id = getSpanIdentity();
            if (id.isPresent()){
                CatalogueData data = getDocument().getCatalogue().get(id.get());
                if (data.isReady()){
                    Span span = data.getTarget();
                    assert span instanceof LinedSpanPointLink ||
                        span instanceof LinedSpanLevelSection;
                    return Optional.of((SpanBranch) span);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public String getText(){
        return getLocalCache(cacheText, () ->{
            Optional<ContentSpan> text = spanFromFirst(ContentSpan.class);
            if (text.isPresent()){
                return text.get().getTrimmed();
            }
            return getPathSpan()
                .filter(span -> span instanceof LinedSpanPointLink)
                .map(span -> ((LinedSpanPointLink)span).getPath())
                .orElse("");
        });
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getLocalCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class).map(span -> span.buildId())
        );
    }

    @Override
    public boolean isId(){
        return false;
    }

    public boolean isExternal(){
        return getLocalCache(cacheExternal, () ->
            getSpanIdentity().map(id -> getDocument().getCatalogue()
                    .get(id).getTarget() instanceof LinedSpanPointLink)
                .orElseThrow(() -> new IllegalStateException("Link not found."))
        );
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () ->{
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(AuxiliaryType.REF_LINK).add(getIdStatus())
                .addAll(super.getBranchStyles()).build();
        });
    }

    @Override
    protected SetupParser getParser(String text){
        return (text.startsWith(LINK_REF) &&
            AuxiliaryChecker.willEndWith(text, LINK_END)
        )? spanReparser: null;
    }

    @Override
    protected String toChildString(){
        Optional<DirectorySpan> id = spanFromFirst(DirectorySpan.class);
        String data = spanFromFirst(DirectorySpan.class)
            .map(span -> span.toString())
            .orElse("null");
        return data + "->" + getPathSpan().toString();
    }
}
