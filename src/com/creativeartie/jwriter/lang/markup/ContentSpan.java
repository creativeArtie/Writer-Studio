package com.creativeartie.jwriter.lang.markup;

import java.util.*; /// For initialization (children)

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Created from {@link ContentParser}, super class of
 * {@link ContentSpanContent}. Used for whenever text is needed.
 */
public class ContentSpan extends SpanBranch implements BasicText{

    /// Stuff for getUpdater(int index, String)
    private final List<String> reparseEnders;
    private final SetupLeafStyle leafStyle;

    ContentSpan (List<Span> spanChildren, List<String> enders,
        SetupLeafStyle style
    ){
        super(spanChildren);
        reparseEnders = Checker.checkNotNull(enders, "enders");
        leafStyle = Checker.checkNotNull(style, "style");
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        return ImmutableList.of();
    }

    public int wordCount(){
        return Splitter.on(CharMatcher.whitespace()).omitEmptyStrings()
            .splitToList(getParsed()).size();
    }

    @Override
    public String toString(){
        String ans = "";
        for (Span span: this){
            ans += span.toString() + "-";
        }
        return ans;
    }
}
