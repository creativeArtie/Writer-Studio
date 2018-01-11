package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

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
