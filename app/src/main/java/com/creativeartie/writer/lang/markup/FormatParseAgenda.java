package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatAgenda}. */
enum FormatParseAgenda implements SetupParser {
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        if(pointer.startsWith(children, CURLY_AGENDA)){
            CONTENT_AGENDA.parse(pointer, children);

            /// Complete the last steps
            pointer.startsWith(children, CURLY_END);
            return Optional.of(new FormatSpanAgenda(children));
        }
        return Optional.empty();
    }
}
