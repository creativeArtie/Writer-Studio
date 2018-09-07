package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt SectionHead1} and {@code SectionHead}. */
enum SectionParseHead implements SectionParser {
    /** Top most section.
     *
     * This is the main parser for {@link WritingText}.
     */
    SECTION_1,
    /** Section 2. */ SECTION_2,     /** Section 3. */ SECTION_3,
    /** Section 4. */ SECTION_4,    /** Section 5. */ SECTION_5,
    /** Section 6. */ SECTION_6;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        ArrayList<Span> children = new ArrayList<>();
        if (SectionParser.isCurrentLevel(pointer, LinedParseLevel.HEADING,
            ordinal())
        ){
            /// Line = current level
            LinedParseLevel.HEADING.parse(pointer, children);
            SectionParser.parseContent(pointer, children);

        } else if (this == SECTION_1 && pointer.isFirst()) {
            /// Line = not level && first
            SectionParser.parseContent(pointer, children);

        } else {
            return Optional.empty();
        }

        return parseRest(pointer, children);
    }

    @Override
    public Optional<SpanBranch> parseChild(SetupPointer pointer){
        stateCheck(this != SECTION_1, "Section 1 can be a child");

        ArrayList<Span> children = new ArrayList<>();
        if (SectionParser.isCurrentLevel(
            pointer, LinedParseLevel.HEADING, ordinal())
        ){
            LinedParseLevel.HEADING.parse(pointer, children);
            SectionParser.parseContent(pointer, children);
        }

        return parseRest(pointer, children);
    }

    private Optional<SpanBranch> parseRest(SetupPointer pointer,
        ArrayList<Span> children
    ){
        /// Get outline
        SectionParseScene.SCENE_1.parseChildren(pointer, children);

        /// Get child headings
        if (this != SECTION_6){
            values()[ordinal() + 1].parseChildren(pointer, children);
        }

        return Optional.ofNullable(children.isEmpty()? null:
            new SectionSpanHead(children, this));
    }
}
