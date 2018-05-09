package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/** Section with optional heading. */
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

    /** Gets all the lines in a section
     *
     * @return answer
     */
    public List<LinedSpan> getAllLines(){
        return getLocalCache(cacheSectionLines, () -> getAllHeadLines(this));
    }

    /** Recusively find all the lines in a {@link SectionSpanHead}.
     *
     * @param head
     *     extracting head
     * @return answer
     * @see getAllLines()
     */
    private static List<LinedSpan> getAllHeadLines(SectionSpanHead head){
        ImmutableList.Builder<LinedSpan> builder = ImmutableList.builder();
        builder.addAll(head.getLines());
        for (SectionSpanScene scene: head.getScenes()){
            builder.addAll(getAllSceneLines(scene));
        }
        for (SectionSpanHead child: head.getSections()) {
            builder.addAll(getAllHeadLines(child));
        }
        return builder.build();
    }

    /** Recusively find all the lines in a {@link SectionSpanScene}.
     *
     * @param head
     *     extracting head
     * @return answer
     * @see getAllHeadLines(SectionSpanHead)
     */
    private static List<LinedSpan> getAllSceneLines(SectionSpanScene scene){
        ImmutableList.Builder<LinedSpan> builder = ImmutableList.builder();
        builder.addAll(scene.getLines());
        for (SectionSpanScene child: scene.getSubscenes()){
            builder.addAll(getAllSceneLines(child));
        }
        return builder.build();
    }

    /** get child sections.
     *
     * @return answer
     */
    public List<SectionSpanHead> getSections(){
        return getLocalCache(cacheSections, () ->
            getChildren(SectionSpanHead.class));
    }

    /** get child scenes (with level 1).
     *
     * @return answer
     */
    public List<SectionSpanScene> getScenes(){
        return getLocalCache(cacheScenes, () ->
            getChildren(SectionSpanScene.class));
    }

    /** Gets the section publish word count. */
    public int getPublishTotal(){
        return getLocalCache(cachePublish,
            () -> getCount(this, s -> s.getPublishTotal(), true)
        );
    }

    /** Gets the section note word count. */
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () ->
            getCount(this, s -> s.getNoteTotal(), true) +
            getNotes().stream().mapToInt(n -> n.getNoteTotal()).sum()
        );
    }

    /** Recusively count the lines */
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
        if (getLevel() == 1 && isDocumentFirst()){
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