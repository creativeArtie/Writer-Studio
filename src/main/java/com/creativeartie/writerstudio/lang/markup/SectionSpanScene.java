package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Section with a outline as a heading*/
public final class SectionSpanScene extends SectionSpan {

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
    protected boolean checkFirst(String line, SectionParser parser){
        if (parser != SectionParseScene.SCENE_6){
            SectionParseScene check =(SectionParseScene) parser;
            check = SectionParseScene.values()[check.ordinal() + 1];

            if (line.startsWith(check.getStarter())){
                return true; /// is child
            }
        }

        if (line.startsWith(parser.getStarter())){
            return true; /// correct start
        }

        return false;
    }

    @Override
    protected boolean checkChildLine(String line, SectionParser parser){
        int level = ((SectionParseScene) parser).ordinal();
        for (SectionParseScene check:
                ImmutableList.copyOf(SectionParseScene.values()).reverse()
            ){
            if (line.startsWith(check.getStarter())){
                return level < check.ordinal();
            }
        }

        /// check for heading, knowing that:
        /// - matching heading 1 matches all child heading
        return ! line.startsWith(SectionParseHead.SECTION_1.getStarter());
    }

    @Override
    public String toString(){
        return "SCENE " + getLevel() + "{" + super.toString() + "}";
    }
}
