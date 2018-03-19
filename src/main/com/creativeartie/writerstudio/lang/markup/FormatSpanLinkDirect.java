package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import java.util.Optional;
import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpanLink} with path indicated right in the text. Represented in
 * design/ebnf.txt as {@code FormatDirectLink}.
 */
public final class FormatSpanLinkDirect extends FormatSpanLink {

    private final FormatParseLinkDirect spanReparser;
    private Optional<Optional<SpanBranch>> cacheTarget;
    private Optional<String> cachePath;
    private Optional<String> cacheText;
    private Optional<List<StyleInfo>> cacheStyles;

    FormatSpanLinkDirect(List<Span> children, FormatParseLinkDirect reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;
    }

    @Override
    public Optional<SpanBranch> getPathSpan(){
        cacheTarget = getCache(cacheTarget, () -> spanFromFirst(ContentSpan
            .class).map(span -> (SpanBranch) span));
        return cacheTarget.get();
    }

    public String getPath(){
        cachePath = getCache(cachePath, () -> getPathSpan()
            .map(span -> (ContentSpan) span)
            .map(path -> path.getTrimmed())
            .orElse(""));
        return cachePath.get();
    }

    @Override
    public String getText(){
        cacheText = getCache(cacheText, () -> {
            Optional<ContentSpan> text = spanFromLast(ContentSpan.class);
            return text.isPresent()? text.get().getTrimmed(): "";
        });
        return cacheText.get();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(AuxiliaryType.DIRECT_LINK)
                .addAll(super.getBranchStyles()).build();
        });
        return cacheStyles.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(LINK_BEGIN) && AuxiliaryChecker.willEndWith(text,
            LINK_END)? spanReparser : null;
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cacheTarget = Optional.empty();
        cachePath = Optional.empty();
        cacheText = Optional.empty();
        cacheStyles = Optional.empty();
    }


    public boolean isExternal(){
        return false;
    }

    @Override
    protected void docEdited(){}

    @Override
    protected String toChildString(){
        return SpanLeaf.escapeText(getText()) + "->" +
            SpanLeaf.escapeText(getPath());
    }

}
