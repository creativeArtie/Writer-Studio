package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt Note}. */
enum NoteCardParser implements SetupParser {
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        boolean isMiddle = false;
        while (true){ /// repeat until all lines found, emptiness check later
            pointer.mark(); /// in case a middle note line has id in it

            Optional<SpanBranch> line = LinedParseRest.NOTE.parse(pointer);
            if (line.isPresent()){
                if (line.flatMap(span -> ((LinedSpanNote)span).buildId())
                        .isPresent() && isMiddle) {
                    /// stop if middle line + id
                    pointer.rollBack();
                    return buildSpan(children);
                }
                children.add(line.get());
            } else /** if (! line.isPresent()) */{
                boolean ready = false;
                for (LinedParseCite cite: LinedParseCite.values()){
                    ready = cite.parse(pointer, children);
                    if (ready){
                        break;
                    }
                }
                if (! ready){
                    return buildSpan(children);
                }
            }
            isMiddle = true;
        }
    }

    /** build the {@link NoteCardSpan} if there are children
     *
     * @param children
     *      span children
     * @see #parse(SetupParser)
     */
    private Optional<SpanBranch> buildSpan(List<Span> children){
        assert children != null: "Null children";
        return Optional.ofNullable(children.isEmpty()? null:
            new NoteCardSpan(children));
    }
}
