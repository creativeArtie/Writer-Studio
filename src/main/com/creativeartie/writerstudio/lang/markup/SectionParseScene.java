package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Implements {@code design/ebnf.txt SectionHead1} and {@code SectionHead}. */
enum SectionParseScene implements SectionParser {
    SCENE_1, SCENE_2, SCENE_3, SCENE_4, SCENE_5, SCENE_6;

    private final String starter;

    private SectionParseScene(){
        starter = LEVEL_STARTERS.get(LinedParseLevel.OUTLINE).get(ordinal());
    }

    public void headParsing(ArrayList<Span> children, SetupPointer pointer,
            boolean findHead){
        checkArgument(findHead, "heading is not found.");
        if (! isLast()){
            SectionParseScene child = values()[ordinal() + 1];
            while (pointer.hasNext(child.starter)){
                child.parse(pointer, children);
            }
        }
    }

    @Override
    public boolean isLast(){
        return this == SCENE_6;
    }

    @Override
    public SectionParser getNext(){
        return values()[ordinal() + 1];
    }

    @Override
    public SectionSpan create(ArrayList<Span> children){
        return new SectionSpanScene(children, this);
    }

    @Override
    public SectionParser[] getParsers(){
        return values();
    }

    @Override
    public LinedParseLevel getHeadLineParser(){
        return LinedParseLevel.OUTLINE;
    }

    @Override
    public String getStarter(){
        return starter;
    }
}
