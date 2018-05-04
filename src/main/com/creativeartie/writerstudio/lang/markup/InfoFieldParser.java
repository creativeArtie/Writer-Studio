package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.google.common.base.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import com.creativeartie.writerstudio.lang.*;

/** Implements rule prefixed with {@code design/ebnf.txt InfoField}
 *
 * The value order is set by:
 * <ul>
 * <li>{@link InfoFieldType#parseText()}</li>
 * </ul>
 */
enum InfoFieldParser implements SetupParser{

    SOURCE(InfoDataParser.FORMATTED, SOURCE_MAIN),
    IN_TEXT(InfoDataParser.TEXT, SOURCE_IN_TEXT),
    FOOTNOTE(InfoDataParser.FORMATTED, SOURCE_FOOTNOTE),
    REF(InfoDataParser.NOTE_REF, SOURCE_REFERENCE),

    ERROR(null, "");

    private final Optional<InfoDataParser> dataParser;
    private final String fieldName;

    private InfoFieldParser(InfoDataParser parser, String name){
        dataParser = Optional.ofNullable(parser);
        fieldName = name;
    }

    Optional<InfoDataParser> getDataParser(){
        return dataParser;
    }

    String getFieldName(){
        return fieldName;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer childPointer){
        ArrayList<Span> children = new ArrayList<>();

        if (this == InfoFieldParser.ERROR){
            if (childPointer.getTo(children, StyleInfoLeaf.FIELD, LINED_DATA,
                    LINED_END)){
                return Optional.of(new InfoFieldSpan(children));
            }
            return Optional.empty();
        }

        if(childPointer.trimStartsWith(children, StyleInfoLeaf.FIELD, fieldName)){
            return Optional.of(new InfoFieldSpan(children));
        }


        return Optional.empty();
    }
}
