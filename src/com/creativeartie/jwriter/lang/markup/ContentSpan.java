package com.creativeartie.jwriter.lang.markup;

import java.util.*; /// For initialization (children)

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

import com.google.common.base.*;
import com.google.common.collect.*;
import static com.google.common.base.Preconditions.*;

/**
 * Created from {@link ContentParser}, super class of
 * {@link ContentSpanContent}. Used for whenever text is needed.
 */
public class ContentSpan extends SpanBranch implements BasicText{

    /// Stuff for getUpdater(int index, String)
    private final ContentParser spanReparser;

    ContentSpan (List<Span> spanChildren, ContentParser parser){
        super(spanChildren);
        spanReparser = checkNotNull(parser, "parser");
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    public int wordCount(){
        return Splitter.on(CharMatcher.whitespace()).omitEmptyStrings()
            .splitToList(getTrimmed()).size();
    }

    @Override
    public String toString(){
        String ans = "";
        for (Span span: this){
            ans += span.toString() + "-";
        }
        return ans;
    }

    @Override
    protected SetupParser getParser(String text){
        return spanReparser.canParse(text)? spanReparser: null;
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
