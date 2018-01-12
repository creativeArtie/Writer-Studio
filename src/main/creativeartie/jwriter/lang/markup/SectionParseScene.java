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
enum SectionParseScene implements SectionParser {
    SCENE_1, SCENE_2, SCENE_3, SCENE_4, SCENE_5, SCENE_6;

    private final String starter;

    private SectionParseScene(){
        starter = getLevelToken(LinedParseLevel.OUTLINE, ordinal());
    }

    @Override
    public boolean isFirst(){
        return this == SCENE_1;
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
    public String getStarter(){
        return starter;
    }
}
