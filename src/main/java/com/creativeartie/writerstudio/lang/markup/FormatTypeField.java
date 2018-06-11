package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Statstics key for {@link FormatSpanPointKey}.*/
public enum FormatTypeField{
    /** To show the current page number */
    PAGE_NUMBER("Stats.PageNumber"),
    /** To show the word count round to the significate digit*/
    WORD_COUNT("Stats.WordCountEst"),
    ERROR("Error");

    private String fieldKey;

    /** Constructor with a key
     * @param key
     *      reference key name
     */
    private FormatTypeField(String key){
        fieldKey = key;
    }

    /** Gets reference key name
     *
     * @return answer
     */
    public String getFieldKey(){
        return fieldKey;
    }

    /** Finds the field for a key
     *
     * @param key
     *      reference key name
     * @return answer or null
     */
    public static FormatTypeField findField(String key){
        if (key == null || key.isEmpty()) return null;

        for (FormatTypeField type: values()){
            if (type == ERROR){
                return type;
            }
            if (type.fieldKey.equals(key)){
                return type;
            }
        }
        assert false: "Unreachable code.";
        return null;
    }
}
