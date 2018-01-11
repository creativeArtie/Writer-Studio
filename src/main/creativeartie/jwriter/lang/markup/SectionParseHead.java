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
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.hasNext(starter)){/// <- this is wrong!
            LinedParseLevel.HEADING.parse(children, pointer);
            SectionParser.parseContent(children, pointer);
        } else if (SECTION_1 == this){
            if (! pointer.hasNext(SectionParser.HEAD_STARTERS)){
                SectionParser.parseContent(children, pointer);
            }
        }

        System.out.println(pointer);
        while (pointer.hasNext(getLevelTokens(LinedParseLevel.OUTLINE))){
            SectionParseScene.SCENE_1.parse(children, pointer);
        }
        if (this != SECTION_6){
            while (SectionParser.hasChild(pointer, values(), this)){
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
