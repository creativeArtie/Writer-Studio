package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List
import java.util.Optional;

import com.google.common.collect.*; // ImmutableList
import com.google.common.base.*; // CharMatcher, Spliter

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.main.Checker.*;


/**
 * Text implementing {@link BasicText} for non-formatted text. Represented in
 * design/ebnf.txt as {@code Content}.
 */
public final class ContentSpan extends SpanBranch implements BasicText{

    private final CacheKeyMain<String> cacheText;
    private final CacheKeyMain<String> cacheTrimmed;
    private final CacheKeyMain<Boolean> cacheSpaceBegin;
    private final CacheKeyMain<Boolean> cacheSpaceEnd;
    private final CacheKeyMain<Integer> wordCount;

    ContentSpan (List<Span> spanChildren){
        super(spanChildren);
        cacheText = CacheKey.stringKey();
        cacheTrimmed = CacheKey.stringKey();
        cacheSpaceBegin = CacheKey.booleanKey();
        cacheSpaceEnd = CacheKey.booleanKey();
        wordCount = CacheKey.integerKey();
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
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    /** Counts the number of words by spliting word count*/
    public int wordCount(){
        return getLocalCache(wordCount,
            () -> Splitter.on(CharMatcher.whitespace())
                .omitEmptyStrings()
                .splitToList(getTrimmed())
                .size());
    }

    @Override
    public String toString(){
        return SpanLeaf.escapeText(getText());
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

}
