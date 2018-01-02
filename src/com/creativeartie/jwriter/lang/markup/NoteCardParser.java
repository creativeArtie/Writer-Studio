package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@code MainSpan*} classes.
 */
enum NoteCardParser implements SetupParser {
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        boolean isMiddle = false;
        while (true){
            pointer.mark();
            Optional<SpanBranch> line = LinedParseRest.NOTE.parse(pointer);
            if (line.isPresent()){
                if (line.flatMap(span -> ((LinedSpanNote)span).buildId())
                        .isPresent() && isMiddle) {
                    pointer.rollBack();
                    return buildSpan(children);
                }
                children.add(line.get());
            } else {
                line = LinedParseCite.INSTANCE.parse(pointer);
                if (line.isPresent()){
                    children.add(line.get());
                } else {
                    return buildSpan(children);
                }
            }
            isMiddle = true;
        }
    }

    private Optional<SpanBranch> buildSpan(List<Span> children){
        return Optional.ofNullable(children.isEmpty()? null:
            new MainSpanNote(children));
    }
}