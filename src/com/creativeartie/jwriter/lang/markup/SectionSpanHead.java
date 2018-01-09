package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public final class SectionSpanHead extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
            AuxiliaryType.SECTION_HEAD);
    private Optional<List<LinedSpan>> cacheSectionLines;
    private Optional<List<SectionSpanHead>> cacheChildren;
    private Optional<List<SectionSpanScene>> cacheScenes;

    SectionSpanHead(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    public List<LinedSpan> getSectionLines(){
        cacheSectionLines = getCache(cacheSectionLines, () -> {
            ImmutableList.Builder<LinedSpan> lines = ImmutableList.builder();
            for (Span span: this){
                if (span instanceof LinedSpan){
                    lines.add((LinedSpan)span);
                } else if (span instanceof SectionSpanHead){
                    lines.addAll(((SectionSpanScene)span).getSectionLines());
                } else if (span instanceof SectionSpanHead){
                    return lines.build();
                }
            }
            return lines.build();
        });
        return cacheSectionLines.get();
    }

    @Override
    protected SetupParser getParser(String text){
        ///TODO getParser
        return null;
    }

    @Override
    protected void childEdited(){
        //TODO childEdited
        cacheSectionLines = Optional.empty();
        super.childEdited();
    }

    @Override
    protected void docEdited(){
        //TODO docEdited
        super.docEdited();
    }
}