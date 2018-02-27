package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.Optional;

import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * {@link Span} to store all other {@link FormatSpan*} classes. Represented in
 * design/ebnf.txt as {@code Format}.
 *
 * Dec 29,2017: it was decided that this class will <b>not</b> do any local
 * reparsing, because it is deem to be too much work.
 */
public final class FormatSpanMain extends SpanBranch {

    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;
    private Optional<Integer> cacheTotal;
    private Optional<String> cacheText;

    FormatSpanMain(List<Span> spanChildren){
        super(spanChildren);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    /** Get the word count of publishing text.*/
    public int getPublishTotal(){
        cachePublish = getCache(cachePublish, () -> getCount(true, false));
        return cachePublish.get();
    }

    /** Get the word count of research notes.*/
    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> getCount(false, true));
        return cacheNote.get();
    }

    /** Get the total word count. */
    public int getTotalCount(){
        cacheTotal = getCache(cacheTotal, () -> getCount(true, true));
        return cacheTotal.get();
    }

    private int getCount(boolean isPublish, boolean isNote){
        StringBuilder text = new StringBuilder();
        /// Add of the text together
        for(Span span: this){
            if (isNote && span instanceof FormatSpanAgenda){
                text.append(" " + ((FormatSpanAgenda)span).getAgenda() + " ");
            } else if (isPublish && span instanceof FormatSpanContent){
                text.append(((FormatSpanContent) span).getText());
            // } else if (span instanceof FormatSpanDirectory){
            } else if (isPublish && span instanceof FormatSpanLink){
                text.append(((FormatSpanLink)span).getText());
            }
        }
        /// creative use of Splitter to count words.
        return Splitter.on(CharMatcher.whitespace())
            .omitEmptyStrings().splitToList(text).size();
    }

    public String getParsedText(){
        cacheText = getCache(cacheText, () -> {
            StringBuilder text = new StringBuilder();
            for (Span span: this){
                if (span instanceof FormatSpanContent){
                    FormatSpanContent content = (FormatSpanContent) span;
                    if (content.isSpaceBegin()){
                        text.append(" ");
                    }
                    text.append(content.getTrimmed());
                    if (content.isSpaceEnd()){
                        text.append(" ");
                    }
                } else if (span instanceof FormatSpanLink){
                    text.append(((FormatSpanLink)span).getText());
                }
            }
            return text.toString();
        });
        return cacheText.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
        cacheTotal = Optional.empty();
        cacheText = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        for (Span span: this){
            if (output.length() != 0){
                output.append(", ");
            }
            output.append(span);
        }
        return output.toString();
    }

}
