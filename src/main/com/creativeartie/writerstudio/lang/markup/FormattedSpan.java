package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * {@link Span} to store all other {@link FormatSpan*} classes. Represented in
 * design/ebnf.txt as {@code Format1} or {@code Foramt2}.
 *
 * Dec 29,2017: it was decided that this class will <b>not</b> do any local
 * reparsing, because it is deem to be too much work.
 */
public final class FormattedSpan extends SpanBranch {

    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;
    private final CacheKeyMain<Integer> cacheTotal;
    private final CacheKeyMain<String> cacheText;
    private boolean allowNotes;

    FormattedSpan(List<Span> spanChildren, boolean notes){
        super(spanChildren);
        allowNotes = notes;

        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
        cacheTotal = CacheKeyMain.integerKey();
        cacheText = CacheKeyMain.stringKey();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    /** Get the word count of publishing text.*/
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () -> getCount(true, false));
    }

    /** Get the word count of research notes.*/
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> getCount(false, true));
    }

    /** Get the total word count. */
    public int getTotalCount(){
        return getLocalCache(cacheTotal, () -> getCount(true, true));
    }

    private int getCount(boolean isPublish, boolean isNote){
        StringBuilder text = new StringBuilder();
        /// Add of the text together
        for(Span span: this){
            if (isNote && span instanceof FormatSpanAgenda){
                text.append(" " + ((FormatSpanAgenda)span).getAgenda() + " ");
            } else if (isPublish && span instanceof FormatSpanContent){
                text.append(((FormatSpanContent) span).getRendered());
            // } else if (span instanceof FormatSpanPointId){
            } else if (isPublish && span instanceof FormatSpanLink){
                text.append(((FormatSpanLink)span).getText());
            }
        }
        /// creative use of Splitter to count words.
        return Splitter.on(CharMatcher.whitespace())
            .omitEmptyStrings().splitToList(text).size();
    }

    public String getParsedText(){
        return getLocalCache(cacheText, () -> {
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
    }

    public boolean allowNotes(){
        return allowNotes;
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        for (Span span: this){
            if (output.length() != 0){
                output.append(", ");
            }
            output.append(span);
        }
        return "Format%%" + output.toString() + "%%";
    }

}
