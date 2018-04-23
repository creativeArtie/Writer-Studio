package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.writerstudio.lang.*;

/**
 * A {@linkplain FormatSpan} for footnote, endnote, and research notes.
 * Represented in design/ebnf.txt as {@code FormatNote}, {@code FormatEndnote}
 * and {@code FormatFootnote}.
 */
public final class FormatSpanPointId extends FormatSpan implements Catalogued{
    private final FormatParsePointId spanReparser;
    private final DirectoryType spanType;
    private final CacheKeyOptional<SpanBranch> cacheTarget;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<String> cacheOutput;

    FormatSpanPointId(List<Span> children, FormatParsePointId reparser){
        super(children, reparser.getFormats());
        spanType = reparser.getDirectoryType();
        spanReparser = reparser;

        cacheTarget = new CacheKeyOptional<>(SpanBranch.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheOutput = CacheKey.stringKey();
    }

    /** Gets the type of note it is pointing to. */
    public DirectoryType getIdType(){
        return spanType;
    }

    public Optional<SpanBranch> getTarget(){
        return getLocalCache(cacheTarget, () -> getSpanIdentity()
            .map(id -> getDocument().getCatalogue().get(id))
            .filter(data -> data.isReady())
            .map(data -> data.getTarget())
            .orElse(null)
        );
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getLocalCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class)
            .map(span -> span.buildId())
            .orElse(null)
        );
    }

    @Override
    public boolean isId(){
        return false;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(spanType).add(getIdStatus())
                .addAll(super.getBranchStyles()).build();
        });
    }

    @Override
    public String getOutput(){
        return getLocalCache(cacheOutput, () -> {
            Optional<DirectorySpan> id = spanFromFirst(DirectorySpan.class);
            if (id.isPresent()){
                return id.get().getLookupText();
            }
            return "";
        });
    }

    @Override
    protected SetupParser getParser(String text){
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected String toChildString(){
        Optional<CatalogueIdentity> id = getSpanIdentity();
        String data = id.isPresent()? SpanLeaf.escapeText(id.get().toString()):
            "null";
        return getIdType().toString() + data;
    }
}
