package com.creativeartie.writer.lang.markup;

import java.util.*;
import com.google.common.base.*;

import com.creativeartie.writer.lang.*;

/** Text that is not formatted */
public final class ContentSpan extends SpanBranch implements BasicText{

    private final CacheKeyMain<String> cacheText;
    private final CacheKeyMain<String> cacheTrimmed;
    private final CacheKeyMain<Boolean> cacheSpaceBegin;
    private final CacheKeyMain<Boolean> cacheSpaceEnd;
    private final CacheKeyMain<Integer> wordCount;

    /** Creates a {@linkplain ContentSpan}.
     *
     * @param children
     *      span children
     * @see ContentParser#buildSpan(List)
     */
    ContentSpan (List<Span> children){
        super(children);
        cacheText = CacheKeyMain.stringKey();
        cacheTrimmed = CacheKeyMain.stringKey();
        cacheSpaceBegin = CacheKeyMain.booleanKey();
        cacheSpaceEnd = CacheKeyMain.booleanKey();
        wordCount = CacheKeyMain.integerKey();
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

    /** Counts the number of words by spliting word count.
     *
     * @return answer
     */
    public int getWordCount(){
        return getLocalCache(wordCount,
            () -> Splitter.on(CharMatcher.whitespace())
                .omitEmptyStrings()
                .splitToList(getTrimmed())
                .size());
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    public String toString(){
        String ans = "";
        for(Span span: this){
            if (! ans.isEmpty()) {
                ans += ", ";
            }
            if (span instanceof SpanLeaf){
                ans += SpanLeaf.escapeText(span.getRaw());
            } else /* if (span 99.999% certainly == BasicTextEscape) */{
                ans += span.toString();
            }
        }
        return "text -> " + ans;
    }

}
