package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt Edition}. */
enum EditionParser implements SetupParser{
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        /// Check if the type is one of the support edition
        for(EditionType type : EditionType.getNamedSyntaxes()){
            if (pointer.startsWith(children, EDITION_BEGIN + type.name())){
                return parseRest(pointer, children);
            }
        }
        /// Check if the type is the unsupported, other edition
        if (pointer.startsWith(children, EDITION_BEGIN)){
            return parseRest(pointer, children);
        }
        return Optional.empty();
    }

    /** Complete the creation of the span.
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     */
    private Optional<SpanBranch> parseRest(SetupPointer pointer,
            ArrayList<Span> children){
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";
        /// Add the meta text, if any found
        CONTENT_BASIC.parse(pointer, children);

        return Optional.of(new EditionSpan(children));

    }
}
