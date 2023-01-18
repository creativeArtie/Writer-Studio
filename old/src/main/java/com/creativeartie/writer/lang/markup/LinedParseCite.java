package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

public enum LinedParseCite implements SetupParser{
    /** The full citation text. */
    SOURCE(SOURCE_MAIN, FORMATTED_DATA),
    /** The in line citation text. */
    IN_TEXT(SOURCE_IN_TEXT, CONTENT_DATA),
    /** The footnote citation text. */
    FOOTNOTE(SOURCE_FOOTNOTE, FORMATTED_DATA),
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
        fieldName = name;
        dataParser = parser;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        pointer.mark();
        if (! pointer.startsWith(children, LINED_CITE)){
            pointer.rollBack();
            return Optional.empty();
        }
        if (this == ERROR){
            pointer.getTo(children, SpanLeafStyle.FIELD, LINED_DATA, LINED_END);
            return parseRest(pointer, children, SpanLeafStyle.TEXT);
        }
        if (pointer.trimStartsWith(children, SpanLeafStyle.FIELD, fieldName)){
            return parseRest(pointer, children, SpanLeafStyle.DATA);
        }
        pointer.rollBack();
        return Optional.empty();
    }

    private Optional<SpanBranch> parseRest(SetupPointer pointer,
            ArrayList<Span> children, SpanLeafStyle last){
        pointer.trimStartsWith(children, LINED_DATA);
        if (dataParser != null){
            dataParser.parse(pointer, children);
        }
        pointer.getTo(children, last, LINED_END);
        pointer.startsWith(children, LINED_END);
        return Optional.of(new LinedSpanCite(children));
    }

    String getFieldName(){
        return fieldName;
    }

    static LinedParseCite getParser(String text){
        argumentNotNull(text, "text");
        text = text.substring(LINED_CITE.length());
        for (LinedParseCite cite: values()){
            if (cite == ERROR){
                return cite;
            }
            if (text.startsWith(cite.getFieldName())){
                return cite;
            }
        }
        return ERROR;
    }
}
