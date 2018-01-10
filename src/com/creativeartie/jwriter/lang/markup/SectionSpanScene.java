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
    private Optional<SectionSpanHead> cacheHead;
    private Optional<List<SectionSpanScene>> cacheScenes;

    SectionSpanScene(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return BRANCH_STYLE;
    }

    public SectionSpanHead getSection(){
        cacheHead = getCache(cacheHead, () ->{
            SpanNode<?> span = getParent();
            while (! span instanceof SectionSpanHead){
                span = span.getParent();
                assert ! span instanceof Document;
            }
        });
        return cacheHead.get();
    }

    public List<SectionSpanScene> getSubscenes(){
        cacheScenes = getCache(cacheScenes, () ->
            getChildren(SectionSpanScene.class));
        return cacheScenes.get();
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