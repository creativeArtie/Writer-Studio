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
public final class SectionSpanScene extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.SECTION_SCENE);
    private Optional<SectionSpanHead> cacheHead;
    private Optional<List<SectionSpanScene>> cacheScenes;
    private Optional<List<LinedSpan>> cacheLines;

    SectionSpanScene(List<Span> children, SectionParser reparser){
        super(children, reparser);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    public SectionSpanHead getSection(){
        cacheHead = getCache(cacheHead, () ->{
            SpanNode<?> span = getParent();
            while (! (span instanceof SectionSpanHead)){
                span = span.getParent();
                assert ! (span instanceof Document);
            }
            return (SectionSpanHead) span;
        });
        return cacheHead.get();
    }

    @Override
    public final List<LinedSpan> getLines(){
        cacheLines = getCache(cacheLines, () -> {
            ImmutableList.Builder<LinedSpan> lines = ImmutableList.builder();
            for (Span child: this){
                if (child instanceof LinedSpan){
                    lines.add((LinedSpan) child);
                }
            }
            return lines.build();
        });
        return cacheLines.get();
    }

    public List<SectionSpanScene> getSubscenes(){
        cacheScenes = getCache(cacheScenes, () ->
            getChildren(SectionSpanScene.class));
        return cacheScenes.get();
    }

    @Override
    protected SetupParser getParser(String text){
        checkNotNull(text, "text");
        return  ! AuxiliaryChecker.checkSectionEnd(this, text) &&
            text.startsWith(getParser().getStarter()) &&
            canParse(text, SectionParseScene.values())? getParser(): null;
    }

    @Override
    protected void childEdited(){
        cacheHead = Optional.empty();
        cacheScenes = Optional.empty();
        cacheLines = Optional.empty();
        super.childEdited();
    }

    @Override
    public String toString(){
        return getParser().toString() + "{" + super.toString() + "}";
    }
}