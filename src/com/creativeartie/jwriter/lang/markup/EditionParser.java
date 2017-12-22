package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link EditionSpan}.
 */
enum EditionParser implements SetupParser{
    INSTANCE;
    private static final ContentParser TEXT_PARSER = new ContentParser();

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
        TEXT_PARSER.parse(children, pointer);

        return Optional.of(new EditionSpan(children));

    }
}
