package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.collect.*;
import com.google.common.base.*;

/**
 * Text implementing {@link BasicText} for non-formatted text. Represented in
 * design/ebnf.txt as {@code Content}.
 */
public final class ContentSpan extends SpanBranch implements BasicText{

    private Optional<String> cacheText;
    private Optional<String> cacheTrimmed;
    private Optional<Boolean> cacheSpaceBegin;
    private Optional<Boolean> cacheSpaceEnd;
    private Optional<Integer> wordCount;

    ContentSpan (List<Span> spanChildren){
        super(spanChildren);
    }

    @Override
    public String getText(){
        cacheText = getCache(cacheText, () -> BasicText.super.getText());
        return cacheText.get();
    }

    @Override
    public String getTrimmed(){
        cacheTrimmed = getCache(cacheTrimmed, () -> BasicText.super
            .getTrimmed());
        return cacheTrimmed.get();
    }

    @Override
    public boolean isSpaceBegin(){
        cacheSpaceBegin = getCache(cacheSpaceBegin,
            () -> BasicText.super.isSpaceBegin());
        return cacheSpaceBegin.get();
    }

    @Override
    public boolean isSpaceEnd(){
        cacheSpaceEnd = getCache(cacheSpaceEnd,
            () -> BasicText.super.isSpaceEnd());
        return cacheSpaceEnd.get();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    /** Counts the number of words by spliting word count*/
    public int wordCount(){
        wordCount = getCache(wordCount,
            () -> Splitter.on(CharMatcher.whitespace())
                .omitEmptyStrings()
                .splitToList(getTrimmed())
                .size());
        return wordCount.get();
    }

    @Override
    public String toString(){
        return SpanLeaf.escapeText(getText());
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){
        cacheText = Optional.empty();
        cacheTrimmed = Optional.empty();
        cacheSpaceBegin = Optional.empty();
        cacheSpaceEnd = Optional.empty();
        wordCount = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
