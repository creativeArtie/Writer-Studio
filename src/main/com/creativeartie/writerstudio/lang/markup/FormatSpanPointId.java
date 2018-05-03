package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A formatted pointer for footnote, endnote and card notes. */
public final class FormatSpanPointId extends FormatSpan implements Catalogued{
    private final FormatParsePointId spanReparser;
    private final DirectoryType spanType;

    private final CacheKeyOptional<SpanBranch> cacheTarget;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<String> cacheOutput;

    /** Creates a {@linkplain FormatSpanPointId}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     * @param reparser
     *      span reparser
     * @see FormatSpanPointId#parseSpan(SetupPointer, List)
     */
    FormatSpanPointId(List<Span> spanChildren, boolean[] formats,
            FormatParsePointId reparser){
        super(spanChildren, formats);
        spanReparser = argumentNotNull(reparser, "reparser");
        spanType = reparser.getDirectoryType();

        cacheTarget = new CacheKeyOptional<>(SpanBranch.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheOutput = CacheKeyMain.stringKey();
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
        );
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getLocalCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class)
            .map(span -> span.buildId())
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
    protected SetupParser getParser(String text){
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected String toChildName(){
        return "id";
    }

    @Override
    protected String toChildString(){
        Optional<CatalogueIdentity> id = getSpanIdentity();
        String data = id.isPresent()? SpanLeaf.escapeText(id.get().toString()):
            "null";
        return getIdType().toString() + " " + data;
    }
}
