package com.creativeartie.writerstudio.lang.markup;

import com.google.common.collect.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A formatted reference text, mainly to docuement statistics. */
public final class FormatSpanPointKey extends FormatSpan{
    private final FormatParsePointKey spanReparser;
    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyMain<String> cacheField;
    private final CacheKeyMain<String> cacheValue;

    /** Creates a {@linkplain FormatSpanLinkRef}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     * @param reparser
     *      span reparser
     * @see FormatParseLinkRef#parseSpan(SetupPointer, List)
     */
    FormatSpanPointKey(List<Span> spanChildren, boolean[] formats,
            FormatParsePointKey reparser){
        super(spanChildren, formats);
        spanReparser = argumentNotNull(reparser, "reparser");

        cacheField = CacheKeyMain.stringKey();
        cacheValue = CacheKeyMain.stringKey();
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
    }

    public String getField(){
        return getLocalCache(cacheField, () -> spanFromFirst(ContentSpan.class)
            .map(s -> s.getTrimmed()).orElse(""));
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.add(AuxiliaryType.REF_KEY)
                .addAll(super.getBranchStyles()).build();
        });
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected String toChildName(){
        return "ref-key";
    }

    @Override
    protected String toChildString(){
        return getField();
    }
}