package com.creativeartie.jwriter.lang.markup;

import java.util.Optional;

import java.util.ArrayList;

import com.google.common.base.CharMatcher;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.*;

/**
 * Parsers for {@link InfoDataSpan} which includes all {@link InfoData*} classes.
 */
enum InfoDataParser implements SetupParser{
    FORMATTED(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (new FormatParser(StyleInfoLeaf.DATA).parse(children, pointer))
        {
            return Optional.of(new InfoDataSpanFormatted(children));
        }
        return Optional.empty();
    }), TEXT(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (CONTENT_DATA.parse(children, pointer)){
            return Optional.of(new InfoDataSpanText(children));
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
