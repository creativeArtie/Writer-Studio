package com.creativeartie.writer.lang.markup;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** Describe the type of field.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link InfoFieldParser} through {@link #getType(String)}</li>
 * <li>{@link CheatsheetText} through (@link CheatsheetLabel#getLabel(CheatsheetText)}</li>
 * </ul>
 */
public enum InfoFieldType{
    /** The full citation text. */
    SOURCE,
    /** The in line citation text. */
    IN_TEXT,
    /** The footnote citation text. */
    FOOTNOTE,
    /** The citation reference to another note card. */
    REF,
    /** Error citation line.*/
    ERROR;

    /** Gets the {@linkplain InfoFieldType} for a {@linkplain String}.
     *
     * @param text
     *      text to parse
     * @return answer
     * @see InfoFieldSpan#getFormatTypeField()
     */
    static InfoFieldType getType(String text){
        argumentNotNull(text, "text");

        for (LinedParseCite type: LinedParseCite.values()){
            if (type == LinedParseCite.ERROR){
                return ERROR;
            }
            if (type.getFieldName().equals(text)){
                return values()[type.ordinal()];
            }
        }
        assert false: "unreachable";
        return ERROR;
    }

    public InfoDataType getDataType(){
        switch (this){
        case SOURCE:
        case FOOTNOTE:
            return InfoDataType.FORMATTED;
        case IN_TEXT:
            return InfoDataType.TEXT;
        case REF:
            return InfoDataType.NOTE_REF;
        default:
            return InfoDataType.ERROR;
        }
    }
}
