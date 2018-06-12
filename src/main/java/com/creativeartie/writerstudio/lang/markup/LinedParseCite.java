package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public enum LinedParseCite implements SetupParser{
    /** The full citation text. */
    SOURCE(SOURCE_MAIN, FORMATTED_DATA),
    /** The in line citation text. */
    IN_TEXT(SOURCE_IN_TEXT, CONTENT_DATA),
    /** The footnote citation text. */
    FOOTNOTE(SOURCE_FOOTNOTE, CONTENT_DATA),
    /** The citation reference to another note card. */
    REF(SOURCE_REFERENCE, DirectoryParser.REF_NOTE),
    /** Error citation line.
     *
     * <b> Must be the last InfoFieldParser.</b>
     */
    ERROR(null, null);

    private SetupParser dataParser;
    private String fieldName;

    /** Creates a {@linkplain LinedParseCite}.
     *
     * @param name
     *      field name
     * @param parser
     *      data parser
     */
    private LinedParseCite(String name, SetupParser parser){
        name = fieldName;
        parser = dataParser;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (! pointer.startsWith(children, LINED_CITE)){
            return Optional.empty();
        }
        if (this == ERROR){
            pointer.getTo(children, SpanLeafStyle.FIELD, LINED_DATA, LINED_END);
            return parseRest(pointer, children);
        }
        pointer.trimStartsWith(children, SpanLeafStyle.FIELD, fieldName);
        return parseRest(pointer, children);
    }

    private Optional<SpanBranch> parseRest(SetupPointer pointer,
            ArrayList<Span> children){
        pointer.trimStartsWith(children, LINED_DATA);
        if (dataParser != null){
            dataParser.parse(pointer, children);
        }
        pointer.getTo(children, SpanLeafStyle.TEXT, LINED_END);
        return Optional.of(new LinedSpanCite(children));
    }

    String getFieldName(){
        return fieldName;
    }
}
