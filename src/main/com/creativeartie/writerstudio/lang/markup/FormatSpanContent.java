package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import com.creativeartie.writerstudio.main.Checker;

/**
 * {@link BasicText} with {@link FormatSpan format} for {@link FormattedSpan}.
 * Represented in design/ebnf.txt as {@code FormatContent}.
 */
public final class FormatSpanContent extends FormatSpan implements BasicText{

    /// Stuff for reparsing
    private final FormatParseContent spanReparser;

    private final CacheKeyMain<String> cacheText;
    private final CacheKeyMain<String> cacheTrimmed;
    private final CacheKeyMain<Boolean> cacheSpaceBegin;
    private final CacheKeyMain<Boolean> cacheSpaceEnd;

    FormatSpanContent(List<Span> spanChildren, boolean[] formats,
            FormatParseContent reparser){
        super(spanChildren, formats);
        spanReparser = reparser;

        cacheText = CacheKeyMain.stringKey();
        cacheTrimmed = CacheKeyMain.stringKey();
        cacheSpaceBegin = CacheKeyMain.booleanKey();
        cacheSpaceEnd = CacheKeyMain.booleanKey();
    }

    @Override
    public String getOutput(){
        return getText();
    }

    @Override
    public String getText(){
        return getLocalCache(cacheText, () -> BasicText.super.getText());
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
    protected SetupParser getParser(String text){
        return null;
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
            } else {
                ans += span.toString();
            }
        }
        return ans;
    }
}
