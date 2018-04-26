package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Line that stores a block quote. Represented in design/ebnf.txt as
 * {@code LinedQuote}.
 */
public final class SectionSpanHead extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
            AuxiliaryType.SECTION_HEAD);
    private final CacheKeyList<LinedSpan> cacheSectionLines;
    private final CacheKeyList<SectionSpanHead> cacheSections;
    private final CacheKeyList<SectionSpanScene> cacheScenes;
    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    SectionSpanHead(List<Span> children, SectionParser reparser){
        super(children, reparser);

        cacheSectionLines = new CacheKeyList<>(LinedSpan.class);
        cacheSections = new CacheKeyList<>(SectionSpanHead.class);
        cacheScenes = new CacheKeyList<>(SectionSpanScene.class);
        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    public List<LinedSpan> getLines(){
        return getLocalCache(cacheSectionLines, () -> {
            ImmutableList.Builder<LinedSpan> lines = ImmutableList.builder();
            for (Span span: this){
                if (span instanceof LinedSpan){
                    lines.add((LinedSpan)span);
                } else if (span instanceof SectionSpanScene){
                    lines.addAll(getLines((SectionSpanScene) span));
                } else if (span instanceof SectionSpanHead){
                    return lines.build();
                }
            }
            return lines.build();
        });
    }

    private static List<LinedSpan> getLines(SectionSpanScene span){
        ImmutableList.Builder<LinedSpan> lines = ImmutableList.builder();
        lines.addAll(span.getLines());
        for (SectionSpanScene child: span.getSubscenes()){
            lines.addAll(getLines(child));
        }
        return lines.build();
    }

    public List<SectionSpanHead> getSections(){
        return getLocalCache(cacheSections, () ->
            getChildren(SectionSpanHead.class));
    }

    public List<SectionSpanScene> getScenes(){
        return getLocalCache(cacheScenes, () ->
            getChildren(SectionSpanScene.class));
    }

    public int getPublishTotal(){
        return getLocalCache(cachePublish,
            () -> getCount(this, span -> span.getPublishTotal(), true)
        );
    }

    public int getNoteTotal(){
        return getLocalCache(cacheNote, () ->
            getCount(this, span -> span.getNoteTotal(), true) +
            getNotes().stream().mapToInt(note -> note.getNoteTotal()).sum()
        );
    }

    private int getCount(Span span, ToIntFunction<LinedSpan> counter,
            boolean isFirst){
        if (isFirst){
            assert span instanceof SectionSpanHead;
            int sum = 0;
            for (Span child: (SectionSpanHead)span){
                /// recursive loop
                sum += getCount(child, counter, false);
            }
            return sum;
        }
        if (span instanceof LinedSpan){
            /// Base case: find LinedSpan
            return counter.applyAsInt((LinedSpan) span);
        } else if (span instanceof SectionSpanScene){
            int sum = 0;
            for (Span child: (SectionSpanScene) span){
                /// recursive loop
                sum += getCount(child, counter, false);
            }
            return sum;
        }
        /// Base case: match none
        return 0;
    }

    @Override
    protected boolean checkStart(String text){
        if (getLevel() == 1 && isFirst()){
            /// Skipping checking when this is the first
            return true;
        }
        return allowChild(text, getLevel() - 1, true);
    }

    @Override
    public String toString(){
        return "HEAD " + getLevel() + "{" + super.toString() + "}";
    }
}