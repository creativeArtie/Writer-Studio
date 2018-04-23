package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public final class SectionSpanScene extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.SECTION_SCENE);
    private final CacheKeyMain<SectionSpanHead> cacheHead;
    private final CacheKeyList<SectionSpanScene> cacheScenes;
    private final CacheKeyList<LinedSpan> cacheLines;

    SectionSpanScene(List<Span> children, SectionParser reparser){
        super(children, reparser);

        cacheHead = new CacheKeyMain<>(SectionSpanHead.class);
        cacheLines = new CacheKeyList<>(LinedSpan.class);
        cacheScenes = new CacheKeyList<>(SectionSpanScene.class);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    public SectionSpanHead getSection(){
        return getLocalCache(cacheHead, () ->{
            SpanNode<?> span = getParent();
            while (! (span instanceof SectionSpanHead)){
                span = span.getParent();
                assert ! (span instanceof Document);
            }
            return (SectionSpanHead) span;
        });
    }

    @Override
    public final List<LinedSpan> getLines(){
        return getLocalCache(cacheLines, () -> {
            ImmutableList.Builder<LinedSpan> lines = ImmutableList.builder();
            for (Span child: this){
                if (child instanceof LinedSpan){
                    lines.add((LinedSpan) child);
                }
            }
            return lines.build();
        });
    }

    public List<SectionSpanScene> getSubscenes(){
        return getLocalCache(cacheScenes, () ->
            getChildren(SectionSpanScene.class));
    }

    @Override
    protected boolean checkStart(String text){
        return allowChild(text, getLevel() - 1, false);
    }

    @Override
    public String toString(){
        return "SCENE " + getLevel() + "{" + super.toString() + "}";
    }
}