package com.creativeartie.jwriter.lang.markup;

import com.google.common.collect.*;

import java.util.*;
import java.util.Optional;
import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * {@link FormatSpanLink} with path indicated right in the text. Represented in
 * design/ebnf.txt as {@code FormatDirectLink}.
 */
public final class FormatSpanLinkDirect extends FormatSpanLink {

    private final FormatParseLinkDirect spanReparser;
    private Optional<String> cachePath;
    private Optional<String> cacheText;
    private Optional<List<StyleInfo>> cacheStyles;

    FormatSpanLinkDirect(List<Span> children, FormatParseLinkDirect reparser){
        super(children, reparser.getFormats());
        spanReparser = reparser;
    }

    @Override
    public String getPath(){
        cachePath = getCache(cachePath, () -> {
            Optional<ContentSpan> path = spanFromFirst(ContentSpan.class);
            return path.isPresent()? path.get().getTrimmed(): "";
        });
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
        cachePath = Optional.empty();
        cacheText = Optional.empty();
        cacheStyles = Optional.empty();
    }

    @Override
    protected void docEdited(){}

}
