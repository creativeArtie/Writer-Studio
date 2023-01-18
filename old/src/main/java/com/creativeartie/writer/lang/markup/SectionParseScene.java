package com.creativeartie.writer.lang.markup;

import java.util.*;
import com.creativeartie.writer.lang.*;

/** Implements {@code design/ebnf.txt SectionScene}. */
enum SectionParseScene implements SectionParser {
    /** Top most section. */ SCENE_1,    /** Scene 2. */ SCENE_2,
    /** Scene 3. */ SCENE_3,             /** Scene 4. */ SCENE_4,
    /** Scene 5. */ SCENE_5,             /** Scene 6. */ SCENE_6;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        ArrayList<Span> children = new ArrayList<>();
        if(SectionParser.isCurrentLevel(
            pointer, LinedParseLevel.OUTLINE, ordinal()
        )){
            LinedParseLevel.OUTLINE.parse(pointer, children);
            SectionParser.parseContent(pointer, children);

        } else {
            /// not the current out
            return Optional.empty();
        }

        return parseRest(pointer, children);
    }

    @Override
    public Optional<SpanBranch> parseChild(SetupPointer pointer){
        ArrayList<Span> children = new ArrayList<>();
        if (SectionParser.isCurrentLevel(
            pointer, LinedParseLevel.OUTLINE, ordinal()
        )){
            LinedParseLevel.OUTLINE.parse(pointer, children);
            SectionParser.parseContent(pointer, children);
        }

        return parseRest(pointer, children);
    }

    private Optional<SpanBranch> parseRest(SetupPointer pointer,
        ArrayList<Span> children
    ){
        if (this != SCENE_6){
            values()[ordinal() + 1].parseChildren(pointer, children);
        }
        return Optional.ofNullable(children.isEmpty()? null:
            new SectionSpanScene(children, this));
    }
}
