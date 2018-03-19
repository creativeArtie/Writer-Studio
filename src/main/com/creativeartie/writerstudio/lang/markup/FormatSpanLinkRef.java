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
    private Optional<Optional<SpanBranch>> cachePath;
    private Optional<String> cacheText;
    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<Boolean> cacheExternal;

    FormatSpanLinkRef(List<Span> children, FormatParseLinkRef reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;
    }

    @Override
    public Optional<SpanBranch> getPathSpan(){
        cachePath = getCache(cachePath, () ->{
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
        return cachePath.get();
    }

    @Override
    public String getText(){
        cacheText = getCache(cacheText, () ->{
            Optional<ContentSpan> text = spanFromFirst(ContentSpan.class);
            if (text.isPresent()){
                return text.get().getTrimmed();
            }
            return getPathSpan()
                .filter(span -> span instanceof LinedSpanPointLink)
                .map(span -> ((LinedSpanPointLink)span).getPath())
                .orElse("");
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

    public boolean isExternal(){
        cacheExternal = getCache(cacheExternal, () ->
            getSpanIdentity().map(id -> getDocument().getCatalogue()
                    .get(id).getTarget() instanceof LinedSpanPointLink)
                .orElseThrow(() -> new IllegalStateException("Link not found."))
        );
        return cacheExternal.get();
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
        cacheExternal = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    protected String toChildString(){
        Optional<DirectorySpan> id = spanFromFirst(DirectorySpan.class);
        String data = spanFromFirst(DirectorySpan.class)
            .map(span -> span.toString())
            .orElse("null");
        return data + "->" + getPathSpan().toString();
    }
}
