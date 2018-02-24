package com.creativeartie.jwriter.lang.markup;

import java.util.Optional;

import com.google.common.base.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.lang.*;

/**
 * Styles describe the type of data.
 */
public enum InfoFieldType implements StyleInfo{

    SOURCE(InfoDataParser.FORMATTED), IN_TEXT(InfoDataParser.TEXT),
    FOOTNOTE(InfoDataParser.FORMATTED),

    ERROR(null);

    private final Optional<SetupParser> dataParser;

    private InfoFieldType(SetupParser parser){
        dataParser = Optional.ofNullable(parser);
    }

    Optional<SetupParser> getDataParser(){
        return dataParser;
    }

    public static InfoFieldType parseText(String text){
        String name = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE,
            text.trim());
        for (InfoFieldType type: values()){
            if (type.name().equals(name)){
                return type;
            }
        }
        return InfoFieldType.ERROR;
    }
}
