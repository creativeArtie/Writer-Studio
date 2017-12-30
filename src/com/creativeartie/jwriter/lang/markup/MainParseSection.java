package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Parser for {@code MainSpan*} classes.
 */
enum MainParseSection implements SetupParser {
    PARSER;
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(! LinedParseLevel.HEADING.parse(children, pointer)){
            LinedParseLevel.OUTLINE.parse(children, pointer);
        }
        while (true){
            pointer.mark();
            if (LinedParseLevel.HEADING.parse(pointer).isPresent()){
                pointer.rollBack();
                return buildSpan(children);
            } else if (LinedParseLevel.OUTLINE.parse(pointer).isPresent()){
                pointer.rollBack();
                return buildSpan(children);
            }
            for (SetupParser parser: SECTION_PARSERS){
                if (parser.parse(children, pointer)){
                    break;
                }
            }
        }
    }

    private Optional<SpanBranch> buildSpan(List<Span> children){
        return Optional.ofNullable(children.isEmpty()? null:
            new MainSpanNote(children));
    }
}