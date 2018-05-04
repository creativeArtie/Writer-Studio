package com.creativeartie.writerstudio.lang.markup;

import java.util.Optional;

import com.google.common.base.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import com.creativeartie.writerstudio.lang.*;

/**
 * Styles describe the type of data.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link InfoFieldType#parseText()}</li>
 * </ul>
 */
public enum InfoFieldType implements StyleInfo{

    SOURCE, IN_TEXT, FOOTNOTE, REF, ERROR;


    public static InfoFieldType parseText(String text){
        for (InfoFieldParser type: InfoFieldParser.values()){
            if (type.getFieldName().equals(text)){
                return values()[type.ordinal()];
            }
        }
        return InfoFieldType.ERROR;
    }
}
