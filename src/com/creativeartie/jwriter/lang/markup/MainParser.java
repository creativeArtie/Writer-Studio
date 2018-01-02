    package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * Parser for {@code MainSpan*} classes.
 */
class MainParser implements SetupParser {
    private static final SetupParser[] PARSERS = SetupParser.combine(
        SetupParser.combine(LinedParseLevel.values(), LinedParsePointer.values()),
        SetupParser.combine(LinedParseCite.values(), LinedParseRest.values())
    );

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        SetupParser found = null;
        for (SetupParser parser: PARSERS){
            if(parser.parse(children, pointer)){
                found = parser;
                break;
            }
        }
        assert found != null;
        if (found == LinedParseRest.NOTE || found == LinedParseCite.INSTANCE){
            return parseNote(children, pointer);
        } else {
            return parseSection(children, pointer);
        }
    }

    private Optional<SpanBranch> parseSection(ArrayList<Span> children,
            SetupPointer pointer){
        while (true){
            pointer.mark();
            Optional<? extends Span> span = Optional.empty();
            for (SetupParser parser : PARSERS){
                span = parser.parse(pointer);
                if (span.isPresent()){
                    break;
                }
            }
            if (span.isPresent()){
                LinedSpan line = (LinedSpan) span.get();
                switch(line.getLinedType()){
                    case HEADING:
                    case OUTLINE:
                        pointer.rollBack();
                        return Optional.of(new MainSpanSection(children));
                    case NOTE:
                    case SOURCE:
                        pointer.rollBack();
                        return Optional.of(new MainSpanSection(children));
                    default:
                        children.add(line);
                }
            } else {
                return Optional.of(new MainSpanSection(children));
            }
        }
    }

    private Optional<SpanBranch> parseNote(ArrayList<Span> children,
            SetupPointer pointer){
        while (true){
            pointer.mark();
            if (! LinedParseCite.INSTANCE.parse(children, pointer)){
                Optional<SpanBranch> span = LinedParseRest.NOTE.parse(pointer);
                if (! span.isPresent()){
                    return Optional.of(new MainSpanNote(children));
                } else {
                    LinedSpanNote found = (LinedSpanNote)span.get();
                    if (found.buildId() != null){
                        pointer.rollBack();
                        return Optional.of(new MainSpanNote(children));
                    }
                }
                children.add(span.get());
            }

        }
    }

}
