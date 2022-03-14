package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** A formatted pointer for footnote, endnote and card notes. */
public final class FormatSpanPointId extends FormatSpan implements Catalogued{
    private final FormatParsePointId spanReparser;
    private final DirectoryType spanType;

    private final CacheKeyOptional<SpanBranch> cacheTarget;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
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
        cacheOutput = CacheKeyMain.stringKey();
    }

    /** Gets the type of note it is pointing to. */
    public DirectoryType getIdType(){
        return spanType;
    }

    public Optional<SpanBranch> getTarget(){
        return getLocalCache(cacheTarget, () -> getSpanIdentity()
            /// id == CatalogueIdentity
            .map(id -> getDocument().getCatalogue().get(id))
            /// id == CatalogueData
            .filter(id -> id.isReady())
            .map(id -> id.getTarget())
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
