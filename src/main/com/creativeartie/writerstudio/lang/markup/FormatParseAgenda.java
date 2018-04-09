package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, Optional

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link FormatSpanCurlyAgenda}.
 */
enum FormatParseAgenda implements SetupParser {
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        if(pointer.startsWith(children, CURLY_AGENDA)){
            CONTENT_AGENDA.parse(children, pointer);

            /// Complete the last steps
            pointer.startsWith(children, CURLY_END);
            return Optional.of(new FormatSpanAgenda(children));
        }
        return Optional.empty();
    }
}
