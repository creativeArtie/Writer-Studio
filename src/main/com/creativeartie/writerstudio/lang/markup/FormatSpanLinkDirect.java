package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpanLink} with path indicated right in the text. Represented in
 * design/ebnf.txt as {@code FormatDirectLink}.
 */
public final class FormatSpanLinkDirect extends FormatSpanLink {

    private final FormatParseLinkDirect spanReparser;
    private final CacheKeyOptional<SpanBranch> cacheTarget;
    private final CacheKeyMain<String> cachePath;
    private final CacheKeyMain<String> cacheText;
    private final CacheKeyList<StyleInfo> cacheStyles;

    FormatSpanLinkDirect(List<Span> children, FormatParseLinkDirect reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;

        cacheTarget = new CacheKeyOptional<>(SpanBranch.class);
        cachePath = CacheKeyMain.stringKey();
        cacheText = CacheKeyMain.stringKey();
        cacheStyles  = new CacheKeyList<>(StyleInfo.class);
    }

    @Override
    public final Optional<SpanBranch> getPathSpan(){
        return getLocalCache(cacheTarget, () -> spanFromFirst(ContentSpan
            .class).map(span -> (SpanBranch) span));
    }

    public String getPath(){
        return getLocalCache(cachePath, () -> getPathSpan()
            .map(span -> (ContentSpan) span)
            .map(path -> path.getTrimmed())
            .orElse(""));
    }

    @Override
    public String getText(){
        return getLocalCache(cacheText, () -> {
            Optional<ContentSpan> text = spanFromLast(ContentSpan.class);
            return text.isPresent()? text.get().getTrimmed(): "";
        });
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(AuxiliaryType.DIRECT_LINK)
                .addAll(super.getBranchStyles()).build();
        });
    }

    @Override
    protected SetupParser getParser(String text){
        return (text.startsWith(LINK_BEGIN) &&
            AuxiliaryChecker.willEndWith(text, LINK_END)
        )? spanReparser : null;
    }

    public boolean isExternal(){
        return false;
    }

    @Override
    protected String toChildName(){
        return "direct";
    }

    @Override
    protected String toChildString(){
        return SpanLeaf.escapeText(getText()) + "->" +
            SpanLeaf.escapeText(getPath());
    }

}
