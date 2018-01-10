package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public final class SectionSpanHead extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
            AuxiliaryType.SECTION_HEAD);
    private Optional<List<LinedSpan>> cacheSectionLines;
    private Optional<List<SectionSpanHead>> cacheSections;
    private Optional<List<SectionSpanScene>> cacheScenes;

    SectionSpanHead(List<Span> children, SectionParser reparser){
        super(children, reparser);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    public List<LinedSpan> getLines(){
        cacheSectionLines = getCache(cacheSectionLines, () -> {
            ImmutableList.Builder<LinedSpan> lines = ImmutableList.builder();
            for (Span span: this){
                if (span instanceof LinedSpan){
                    lines.add((LinedSpan)span);
                } else if (span instanceof SectionSpanScene){
                    lines.addAll(((SectionSpanScene)span).getLines());
                } else if (span instanceof SectionSpanHead){
                    return lines.build();
                }
            }
            return lines.build();
        });
        return cacheSectionLines.get();
    }

    public List<SectionSpanHead> getSections(){
        cacheSections = getCache(cacheSections, () ->
            getChildren(SectionSpanHead.class));
        return cacheSections.get();
    }

    public List<SectionSpanScene> getScenes(){
        cacheScenes = getCache(cacheScenes, () ->
            getChildren(SectionSpanScene.class));
        return cacheScenes.get();
    }

    @Override
    protected SetupParser getParser(String text){
        checkNotNull(text, "text");
        return  ! AuxiliaryChecker.checkSectionEnd(this, text) &&
            text.startsWith(getParser().getStarter()) &&
            canParse(text, SectionParseSection.values())? getParser(): null;
    }

    @Override
    protected void childEdited(){
        cacheSectionLines = Optional.empty();
        cacheSections = Optional.empty();
        cacheScenes = Optional.empty();
        super.childEdited();
    }
}