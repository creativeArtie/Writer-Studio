package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt LinedCite}. */
enum LinedParseCite implements SetupParser {
    INSTANCE;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        if (pointer.startsWith(children, LINED_CITE)){
            /// Create field span
            Optional<InfoDataParser> used = Optional.empty();
            for (InfoFieldParser parser: InfoFieldParser.values()){
                if (parser.parse(pointer, children)){
                    used = parser.getDataParser();
                    break;
                }
            }

            pointer.trimStartsWith(children, LINED_DATA);

            /// Create the data span
            if (! (used.isPresent() && used.get().parse(pointer, children))){
                pointer.getTo(children, StyleInfoLeaf.TEXT, LINED_END);
            }

            /// Create non dat text
            pointer.startsWith(children, LINED_END);

            return Optional.of(new LinedSpanCite(children));
        }
        return Optional.empty();
    }
}
