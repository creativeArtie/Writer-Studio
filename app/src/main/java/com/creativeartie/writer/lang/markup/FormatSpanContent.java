package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** A formatted {@link BasicText}. */
public final class FormatSpanContent extends FormatSpan implements BasicText{

    /// Stuff for reparsing
    private final FormatParseContent spanReparser;

    private final CacheKeyMain<String> cacheText;
    private final CacheKeyMain<String> cacheTrimmed;
    private final CacheKeyMain<Boolean> cacheSpaceBegin;
    private final CacheKeyMain<Boolean> cacheSpaceEnd;

    /** Creates a {@linkplain FormatSpanContent}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     * @param reparser
     *      span reparser
     * @see FormatParseContent#buildSpan(SetupPointer)
     */
    FormatSpanContent(List<Span> spanChildren, boolean[] formats,
            FormatParseContent reparser){
        super(spanChildren, formats);
        spanReparser = argumentNotNull(reparser, "reparser");

        cacheText = CacheKeyMain.stringKey();
        cacheTrimmed = CacheKeyMain.stringKey();
        cacheSpaceBegin = CacheKeyMain.booleanKey();
        cacheSpaceEnd = CacheKeyMain.booleanKey();
    }

    @Override
    public String getRendered(){
        return getLocalCache(cacheText, () -> BasicText.super.getRendered());
    }

    @Override
    public String getTrimmed(){
        return getLocalCache(cacheTrimmed, () -> BasicText.super.getTrimmed());
    }

    @Override
    public boolean isSpaceBegin(){
        return getLocalCache(cacheSpaceBegin, () -> BasicText.super
            .isSpaceBegin());
    }

    @Override
    public boolean isSpaceEnd(){
        return getLocalCache(cacheSpaceEnd, () -> BasicText.super
            .isSpaceEnd());
    }

    @Override
    protected String toChildName(){
        return "format";
    }

    @Override
    protected String toChildString(){
        String ans = "";
        for(Span span: this){
            if (! ans.isEmpty()) {
                ans += ", ";
            }
            if (span instanceof SpanLeaf){
                ans += SpanLeaf.escapeText(span.getRaw());
            } else /** if (span == BasicTextEscape, but whatever) */{
                ans += span.toString();
            }
        }
        return ans;
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }
}
