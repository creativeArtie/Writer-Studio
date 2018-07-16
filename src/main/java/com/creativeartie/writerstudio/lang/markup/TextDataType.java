package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Formats for {@link TextSpan}.
 *
 * This usually mean alignment or data type. Formatting will be a different
 * thing altogether.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link #listAligns()}</li>
 * <li>{@link #isAlignType()}</li>
 * </ul>
 */
public enum TextDataType{
    /** Align text to the left. */
    LEFT(ALIGN_LEFT),
    /** Align text to the centre. */
    CENTER(ALIGN_CENTER),
    /** Align text to the right. */
    RIGHT(ALIGN_RIGHT),
    /** Data is a text type. */
    TEXT(ALIGN_TEXT),
    /** Data type is unkown. */
    UNKNOWN("");

    /** List the text alignment. */
    public static TextDataType[] listAligns(){
        return Arrays.copyOf(values(), values().length - 1);
    }

    private String keyName;

    /** Creates a {@linkplain Format}.
     *
     * @param key
     *      storage key.
     */
    private TextDataType(String key){
        keyName = key;
    }

    /** Gets the key name in the file.
     *
     * @return answer
     */
    String getKeyName(){
        return keyName;
    }

    /** Check if type is alignment type.
     *
     * @return answer
     */
    public boolean isAlignType(){
        return ordinal() <= RIGHT.ordinal();
    }


    public static TextDataType getDataType(String key){
        for (TextDataType type: values()){
            if (type.getKeyName().equals(key)){
                return type;
            }
        }
        return TextDataType.TEXT;
    }

}
