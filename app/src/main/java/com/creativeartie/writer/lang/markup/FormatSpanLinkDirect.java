package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** A formatted link with a link path. */
public final class FormatSpanLinkDirect extends FormatSpanLink {

    private final FormatParseLinkDirect spanReparser;
    private final CacheKeyMain<String> cachePath;
    private final CacheKeyMain<String> cacheText;

    /** Creates a {@linkplain FormatSpanLinkDirect}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     * @param reparser
     *      span reparser
     * @see FormatParseLinkDirect#parseSpan(SetupPointer, List)
     */
    FormatSpanLinkDirect(List<Span> spanChildren, boolean[] formats,
            FormatParseLinkDirect reparser){
        super(spanChildren, formats);
        spanReparser = argumentNotNull(reparser, "reparser");

        new CacheKeyOptional<>(SpanBranch.class);
        cachePath = CacheKeyMain.stringKey();
        cacheText = CacheKeyMain.stringKey();
    }

    /** Gets the link path
     *
     * @return answer
     */
    public String getPath(){
        return getLocalCache(cachePath, () -> spanFromFirst(ContentSpan.class)
            .map(p -> p.getTrimmed())
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

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return (text.startsWith(LINK_BEGIN) &&
            AuxiliaryChecker.willEndWith(text, LINK_END)
        )? spanReparser : null;
    }

}
