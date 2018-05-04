package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements rule prefixed with {@code design/ebnf.txt InfoField}
 *
 * The value order is set by:
 * <ul>
 * <li>{@link InfoFieldType#parseText()}</li>
 * </ul>
 */
enum InfoFieldParser implements SetupParser{

    /** The full citation text. */
    SOURCE(InfoDataParser.FORMATTED, SOURCE_MAIN),
    /** The in line citation text. */
    IN_TEXT(InfoDataParser.TEXT, SOURCE_IN_TEXT),
    /** The footnote citation text. */
    FOOTNOTE(InfoDataParser.FORMATTED, SOURCE_FOOTNOTE),
    /** The citation reference to another note card. */
    REF(InfoDataParser.NOTE_REF, SOURCE_REFERENCE),
    /** Error citation line.
     *
     * <b> Must be the last InfoFieldParser.</b>
     */
    ERROR(null, "");

    private final Optional<InfoDataParser> dataParser;
    private final String fieldName;

    /** Creates a {@linkplain InfoDataParser}.
     *
     * @param parser
     *      data parser
     * @param name
     *      field name
     */
    private InfoFieldParser(InfoDataParser parser, String name){
        assert name != null: "Null name";
        dataParser = Optional.ofNullable(parser);
        fieldName = name;
    }

    /** Gets the data parser
     *
     * @return answer
     * @see LinedParseCite#parse(SetupPointer)
     */
    Optional<InfoDataParser> getDataParser(){
        return dataParser;
    }

    /** Gets the field name.
     *
     * @return answer
     * @see InfoFieldType#parseText(String)
     */
    String getFieldName(){
        return fieldName;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        /// Different for error
        if (this == InfoFieldParser.ERROR){
            if (pointer.getTo(children, StyleInfoLeaf.FIELD, LINED_DATA,
                    LINED_END)){
                return Optional.of(new InfoFieldSpan(children));
            }
            return Optional.empty();
        }

        assert fieldName.length() > 0: "Empty fieldName";

        /// For all others fields
        if(pointer.trimStartsWith(children, StyleInfoLeaf.FIELD, fieldName)){
            return Optional.of(new InfoFieldSpan(children));
        }

        return Optional.empty();
    }
}
