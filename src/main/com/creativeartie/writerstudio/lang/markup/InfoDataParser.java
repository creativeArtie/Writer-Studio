package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements rule prefixed with {@code design/ebnf.txt InfoData}. */
enum InfoDataParser implements SetupParser{
    /** A {@link FomattedSpan} data. */
    FORMATTED(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (FORMATTED_DATA.parse(pointer, children))
        {
            return Optional.of(new InfoDataSpanFormatted(children));
        }
        return Optional.empty();
    }),
    /** A {@link DirectoryType#RESERACH} {@link DirectorySpan} data. */
    NOTE_REF(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (DirectoryParser.REF_NOTE.parse(pointer, children)){
            return Optional.of(new InfoDataSpanRef(children, InfoDataType.NOTE_REF));
        }
        return Optional.empty();
    }),
    /** A {@link ContentSpan} data. */
    TEXT(pointer -> {
        assert pointer != null: "Null pointer";
        ArrayList<Span> children = new ArrayList<>();
        if (CONTENT_DATA.parse(pointer, children)){
            return Optional.of(new InfoDataSpanText(children));
        }
        return Optional.empty();
    }),
    /** Data placeholder. */
    ERROR (pointer -> Optional.empty());

    private final SetupParser llambaParser;

    /** Creates a {@linkplain InfoDataParser}.
     *
     * @param parser
     *      llamba parser
     */
    private InfoDataParser(SetupParser parser){
        llambaParser = parser;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        return llambaParser.parse(pointer); /// check inside
    }
}
