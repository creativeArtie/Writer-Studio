package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt SectionHead1} and {@code SectionHead}. */
enum SectionParseHead implements SectionParser {
    SECTION_1, SECTION_2, SECTION_3, SECTION_4, SECTION_5, SECTION_6;

    private final String starter;

    private SectionParseHead(){
        starter = LEVEL_STARTERS.get(LinedParseLevel.HEADING).get(ordinal());
    }

    @Override
    public boolean isLast(){
        return this == SECTION_6;
    }

    @Override
    public SectionParser getNext(){
        return values()[ordinal() + 1];
    }

    @Override
    public SectionSpan create(ArrayList<Span> children){
        return new SectionSpanHead(children, this);
    }

    @Override
    public SectionParser[] getParsers(){
        return values();
    }

    @Override
    public void headParsing(ArrayList<Span> children, SetupPointer pointer,
            boolean findHead){
        if (!findHead && (this == SECTION_1)){
            if (! pointer.hasNext(LEVEL_HEADINGS)){
                SectionParser.parseContent(children, pointer);
            }
        }
        while (pointer.hasNext(LEVEL_STARTERS.get(LinedParseLevel.OUTLINE))){
            SectionParseScene.SCENE_1.parse(pointer, children);
        }
        if (! isLast()){
            SectionParseHead child = values()[ordinal() + 1];
            while (pointer.hasNext(child.starter)){
                child.parse(pointer, children);
            }
        }
    }

    @Override
    public LinedParseLevel getHeadLineParser(){
        return LinedParseLevel.HEADING;
    }

    @Override
    public String getStarter(){
        return starter;
    }
}
