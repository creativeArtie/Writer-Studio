package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Parser for {@code MainSpan*} classes. See {@code design/SectionParser.txt}
 * for details
 *
 */
enum SectionParseHead implements SectionParser {
    SECTION_1, SECTION_2, SECTION_3, SECTION_4, SECTION_5, SECTION_6;

    private final String starter;

    private SectionParseHead(){
        starter = getLevelToken(LinedParseLevel.HEADING, ordinal() + 1);
    }

    @Override
    public boolean isFirst(){
        return this == SECTION_1;
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
        if (!findHead && isFirst()){
            if (! pointer.hasNext(SectionParser.HEAD_STARTERS)){
                SectionParser.parseContent(children, pointer);
            }
        }
        while (pointer.hasNext(getLevelTokens(LinedParseLevel.OUTLINE))){
            SectionParseScene.SCENE_1.parse(children, pointer);
        }
        if (! isLast()){
            SectionParseHead child = values()[ordinal() + 1];
            while (pointer.hasNext(child.starter)){
                    child.parse(children, pointer);
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
