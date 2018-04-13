package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, Optional

import com.creativeartie.writerstudio.lang.*; // Span, SpanBranch

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import com.creativeartie.writerstudio.main.Checker;

/**
 * Parser for {@link FormatSpanPointId}.
 */
abstract class FormatParsePoint implements SetupParser {

    private final String spanStart;
    private final boolean[] formatList;

    protected FormatParsePoint(String start, boolean[] formats){
        Checker.checkNotNull(start, "start");
        Checker.checkEqual(formats.length, "format", FORMAT_TYPES);
        spanStart = start;
        formatList = formats;
    }

    boolean[] getFormats(){
        return formatList;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, spanStart)){
            /// CatalogueIdentity for the other Parsers
            parseContent(children, pointer);

            /// Complete the last steps
            pointer.startsWith(children, CURLY_END);

            return parseFinish(children, pointer);
        }
        return Optional.empty();
    }

    abstract void parseContent(ArrayList<Span> children, SetupPointer pointer);

    abstract Optional<SpanBranch> parseFinish(ArrayList<Span> children,
        SetupPointer pointer);

    boolean canParse(String text){
        Checker.checkNotNull(text, "text");
        return text.startsWith(spanStart) &&
            AuxiliaryChecker.willEndWith(text, CURLY_END);
    }
}
