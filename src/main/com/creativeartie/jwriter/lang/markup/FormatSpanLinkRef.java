package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpanLink} with path located somewhere in the document.
 * Represented in design/ebnf.txt as {@code FormatRefLink}.
 */
public final class FormatSpanLinkRef extends FormatSpanLink
        implements Catalogued{

    private final FormatParseLinkRef spanReparser;
    private Optional<String> cachePath;
    private Optional<String> cacheText;
    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<Optional<CatalogueIdentity>> cacheId;

    FormatSpanLinkRef(List<Span> children, FormatParseLinkRef reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;
    }

    @Override
    public String getPath(){
        cachePath = getCache(cachePath, () ->{
            Optional<CatalogueIdentity> id = getSpanIdentity();
            if (id.isPresent()){
                CatalogueData data = getDocument().getCatalogue().get(id.get());
                if (data.isReady()){
                    Span span = data.getTarget();
                    return ((LinedSpanPointLink)span).getPath();
                }
            }
            return "";
        });
        return cachePath.get();
    }

    @Override
    public String getText(){
        cacheText = getCache(cacheText, () ->{
            Optional<ContentSpan> text = spanFromFirst(ContentSpan.class);
            if (text.isPresent()){
                return text.get().getTrimmed();
            }
            return getPath();
        });
        return cacheText.get();
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        cacheId = getCache(cacheId, () ->{
            return spanFromFirst(DirectorySpan.class).map(
                span -> span.buildId());
        });
        return cacheId.get();
    }

    @Override
    public boolean isId(){
        return false;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () ->{
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(AuxiliaryType.REF_LINK).add(getIdStatus())
                .addAll(super.getBranchStyles()).build();
        });
        return cacheStyles.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINK_REF) &&
            AuxiliaryChecker.willEndWith(text, LINK_END)? spanReparser: null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cachePath = Optional.empty();
        cacheText = Optional.empty();
        cacheStyles = Optional.empty();
        cacheId = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    protected String toChildString(){
        Optional<DirectorySpan> id = spanFromFirst(DirectorySpan.class);
        String data = spanFromFirst(DirectorySpan.class)
            .map(span -> span.toString())
            .orElse("null");
        return data + "->" + SpanLeaf.escapeText(getPath());
    }
}
