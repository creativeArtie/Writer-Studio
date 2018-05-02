package com.creativeartie.writerstudio.lang.markup;

import java.util.Optional;

import java.util.ArrayList;

import com.google.common.base.CharMatcher;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;
import com.creativeartie.writerstudio.lang.*;

/**
 * Parsers for {@link InfoDataSpan} which includes all {@link InfoData*} classes.
 */
enum InfoDataParser implements SetupParser{
    FORMATTED(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (FORMATTED_DATA.parse(pointer, children))
        {
            return Optional.of(new InfoDataSpanFormatted(children));
        }
        return Optional.empty();
    }), TEXT(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (CONTENT_DATA.parse(pointer, children)){
            return Optional.of(new InfoDataSpanText(children));
        }
        return Optional.empty();
    }), NOTE_REF(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (DirectoryParser.REF_NOTE.parse(pointer, children)){
            return Optional.of(new InfoDataSpanRef(children));
        }
        return Optional.empty();
    }), ERROR (pointer -> Optional.empty());

    private final SetupParser parser;

    private InfoDataParser(SetupParser dataParser){
        parser = dataParser;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        return parser.parse(pointer); /// check inside
    }
}
