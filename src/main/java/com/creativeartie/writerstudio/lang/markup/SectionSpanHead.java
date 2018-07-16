package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Section with optional heading. */
public final class SectionSpanHead extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
            AuxiliaryType.SECTION_HEAD);
    private final CacheKeyList<LinedSpan> cacheSectionLines;

    private final CacheKeyList<SectionSpanHead> cacheSections;
    private final CacheKeyList<SectionSpanScene> cacheScenes;

    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain SectionSpanHead}.
     *
     * @param children
     *      span children
     * @param reparser
     *      span reparser
     * @see SectionParseHead#buildSpan(ArrayList)
     */
    SectionSpanHead(List<Span> children, SectionParser reparser){
        super(children, reparser);

        cacheSectionLines = new CacheKeyList<>(LinedSpan.class);

        cacheSections = new CacheKeyList<>(SectionSpanHead.class);
        cacheScenes = new CacheKeyList<>(SectionSpanScene.class);

        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
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
        assert head != null: "Null head";
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
        assert scene != null: "Null scene";
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

    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () ->
            getPublishCount() +
            getSections().stream().mapToInt(s -> s.getPublishTotal()).sum() +
            getScenes().stream().mapToInt(s -> s.getPublishTotal()).sum()
        );
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () ->
            getNoteCount() +
            getSections().stream().mapToInt(s -> s.getNoteTotal()).sum() +
            getScenes().stream().mapToInt(s -> s.getNoteTotal()).sum()
        );
    }

    @Override
    protected boolean checkStart(String text){
        argumentNotNull(text, "text");

        if (getLevel() == 1 && isDocumentFirst()){
            /// Skipping checking when this is the first
            return true;
        }
        return allowChild(text, getLevel() - 1, true);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    public String toString(){
        return "HEAD " + getLevel() + "{" + super.toString() + "}";
    }
}