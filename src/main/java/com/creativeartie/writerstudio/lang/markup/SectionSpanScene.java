package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Section with a outline as a heading*/
public final class SectionSpanScene extends SectionSpan {
    private static final List<StyleInfo> BRANCH_STYLE = ImmutableList.of(
        AuxiliaryType.SECTION_SCENE);

    private final CacheKeyMain<Integer> cacheLevel;

    private final CacheKeyMain<SectionSpanHead> cacheHead;
    private final CacheKeyList<SectionSpanScene> cacheScenes;

    private final CacheKeyMain<Integer> cachePublish;
    private final CacheKeyMain<Integer> cacheNote;

    /** Creates a {@linkplain SectionSpanScene}.
     *
     * @param children
     *      span children
     * @param reparser
     *      span reparser
     * @see SectionParseScene#buildSpan(ArrayList)
     */
    SectionSpanScene(List<Span> children, SectionParser reparser){
        super(children, reparser);
        cacheLevel = CacheKeyMain.integerKey();

        cacheHead = new CacheKeyMain<>(SectionSpanHead.class);
        cacheScenes = new CacheKeyList<>(SectionSpanScene.class);

        cachePublish = CacheKeyMain.integerKey();
        cacheNote = CacheKeyMain.integerKey();
    }

    /** Gets the parent section.
     *
     * @return answer
     */
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

    /** Gets the parent subscenes.
     *
     * @return answer
     */
    public List<SectionSpanScene> getSubscenes(){
        return getLocalCache(cacheScenes, () ->
            getChildren(SectionSpanScene.class));
    }

    @Override
    public int getLevel(){
        return getLocalCache(cacheLevel, () -> getParent(SectionSpanScene.class)
            .map(s -> s.getLevel() + 1).orElse(1));
    }

    @Override
    public int getPublishTotal(){
        return getLocalCache(cachePublish, () ->
            getPublishCount() +
            getSubscenes().stream().mapToInt(s -> s.getPublishTotal()).sum()
        );
    }

    @Override
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () ->
            getNoteCount() +
            getSubscenes().stream().mapToInt(s -> s.getNoteTotal()).sum()
        );
    }

    @Override
    protected boolean checkStart(String text){
        argumentNotNull(text, "text");
        return allowChild(text, getLevel() - 1, false);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    @Override
    public String toString(){
        return "SCENE " + getLevel() + "{" + super.toString() + "}";
    }
}
