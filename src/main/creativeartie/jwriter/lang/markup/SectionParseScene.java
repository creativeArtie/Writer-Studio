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
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.hasNext(starter)){
            LinedParseLevel.OUTLINE.parse(children, pointer);
            SectionParser.parseContent(children, pointer);
        }
        if (SCENE_6 != this){
            while(SectionParser.hasChild(pointer, values(), this)){
                values()[ordinal() + 1].parse(children, pointer);
            }
        }
        return Optional.ofNullable(children.isEmpty()? null:
            new SectionSpanHead(children, this));
    }

    @Override
    public String getStarter(){
        return starter;
    }
}
