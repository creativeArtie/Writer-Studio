package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;

/**
 * A {@link span} for formatted text.
 */
public final class FormatSpanMain extends SpanBranch {

    FormatSpanMain(List<Span> spanChildren){
        super(spanChildren);
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        return ImmutableList.of();
    }

    public int getPublishTotal(){
        return getCount(true, false);
    }

    public int getNoteTotal(){
        return getCount(false, true);
    }

    public int getTotalCount(){
        return getCount(true, true);
    }

    private int getCount(boolean isPublish, boolean isNote){
        StringBuilder text = new StringBuilder();
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
        return Splitter.on(CharMatcher.whitespace())
            .omitEmptyStrings().splitToList(text).size();
    }

    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }

}
