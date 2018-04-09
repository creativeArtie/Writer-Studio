package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, Optional

import com.creativeartie.writerstudio.lang.*; //SetupParser, SpanBranch

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link EditionSpan}.
 */
enum EditionParser implements SetupParser{
    INSTANCE;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        /// Check if the type is one of the support edition
        for(EditionType type : EditionType.getNamedSyntaxes()){
            if (pointer.startsWith(children, EDITION_BEGIN + type.name())){
                return finalize(children, pointer);
            }
        }
        /// Check if the type is the unsupported, other edition
        if (pointer.startsWith(children, EDITION_BEGIN)){
            return finalize(children, pointer);
        }
        return Optional.empty();
    }

    /**
     * Complete the creation of the span. Helper method of
     * {@link #parse(SetupPointer)}.
     */
    private Optional<SpanBranch> finalize(ArrayList<Span> children,
        SetupPointer pointer)
    {
        assert children != null: "Null children.";
        assert pointer != null: "Null pointer.";
        /// Add the meta text, if any found
        CONTENT_BASIC.parse(children, pointer);

        return Optional.of(new EditionSpan(children));

    }
}
