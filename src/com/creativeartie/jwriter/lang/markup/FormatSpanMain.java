package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.Optional;

import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * {@link Span} to store all other {@link FormatSpan*} classes.
 */
public final class FormatSpanMain extends SpanBranch {

    private FormatParser spanReparser;
    private Optional<Integer> cachePublish;
    private Optional<Integer> cacheNote;
    private Optional<Integer> cacheTotal;

    FormatSpanMain(List<Span> spanChildren, FormatParser reparser){
        super(spanChildren);
        spanReparser = checkNotNull(reparser, "reparser");
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

    @Override
    protected SetupParser getParser(String text){
        return BasicParseText.canParse(text, spanReparser.getReparseEnders())?
            spanReparser:null;
    }

    @Override
    protected void childEdited(){
        cachePublish = Optional.empty();
        cacheNote = Optional.empty();
        cacheTotal = Optional.empty();
    }

    @Override
    protected void docEdited(){}

}
