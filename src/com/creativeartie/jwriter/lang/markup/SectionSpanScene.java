package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public final class SectionSpanScene extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.SECTION_SCENE);
    private Optional<List<LinedSpan>> cacheSectionLines;
    private Optional<List<SectionSpanScene>> cacheChildren;

    SectionSpanScene(List<Span> children){
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
        super.childEdited();
    }

    @Override
    protected void docEdited(){
        //TODO docEdited
        super.docEdited();
    }
}