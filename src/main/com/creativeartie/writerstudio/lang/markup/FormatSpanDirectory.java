package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.writerstudio.lang.*;

/**
 * A {@linkplain FormatSpan} for footnote, endnote, and research notes.
 * Represented in design/ebnf.txt as {@code FormatNote}, {@code FormatEndnote}
 * and {@code FormatFootnote}.
 */
public final class FormatSpanDirectory extends FormatSpan implements Catalogued{
    private final FormatParseDirectory spanReparser;
    private final DirectoryType spanType;
    private Optional<Optional<SpanBranch>> cacheTarget;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<String> cacheOutput;

    FormatSpanDirectory(List<Span> children, FormatParseDirectory reparser){
        super(children, reparser.getFormats());
        spanType = reparser.getDirectoryType();
        spanReparser = reparser;
    }

    /** Gets the type of note it is pointing to. */
    public DirectoryType getIdType(){
        return spanType;
    }

    public Optional<SpanBranch> getTarget(){
        cacheTarget = getCache(cacheTarget, () -> getSpanIdentity()
            .map(id -> getDocument().getCatalogue().get(id))
            .filter(data -> data.isReady())
            .map(data -> data.getTarget())
        );
        return  cacheTarget.get();
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        cacheId = getCache(cacheId, () -> {
            Optional<DirectorySpan> found = spanFromFirst(DirectorySpan.class);
            return found.map(span -> span.buildId());
        });
        return cacheId.get();
    }

    @Override
    public boolean isId(){
        return false;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(spanType).add(getIdStatus())
                .addAll(super.getBranchStyles()).build();
        });
        return cacheStyles.get();
    }

    @Override
    public String getOutput(){
        cacheOutput = getCache(cacheOutput, () -> {
            Optional<DirectorySpan> id = spanFromFirst(DirectorySpan.class);
            if (id.isPresent()){
                return id.get().getLookupText();
            }
            return "";
        });
        return cacheOutput.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cacheTarget = Optional.empty();
        cacheId = Optional.empty();
        cacheStyles = Optional.empty();
        cacheOutput = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    protected String toChildString(){
        Optional<CatalogueIdentity> id = getSpanIdentity();
        String data = id.isPresent()? SpanLeaf.escapeText(id.get().toString()):
            "null";
        return getIdType().toString() + data;
    }
}
