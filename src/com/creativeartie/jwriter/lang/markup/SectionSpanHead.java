package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public class SectionSpanHead extends SpanBranch {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.AGENDA);

    SectionSpanHead(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    protected SetupParser getParser(String text){
        ///TODO getParser
        return null;
    }

    @Override
    protected void childEdited(){
        //TODO childEdited
    }

    @Override
    protected void docEdited(){
        //TODO docEdited
    }
}