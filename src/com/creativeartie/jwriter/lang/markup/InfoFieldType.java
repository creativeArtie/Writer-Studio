package com.creativeartie.jwriter.lang.markup;

import java.util.Optional;

import com.google.common.base.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.lang.*;

public enum InfoFieldType implements StyleInfo{

    SOURCE(InfoDataParser.FORMATTED), IN_TEXT(InfoDataParser.TEXT),
    FOOTNOTE(InfoDataParser.TEXT),

    ERROR(null);

    private final Optional<SetupParser> dataParser;

    private InfoFieldType(SetupParser parser){
        dataParser = Optional.ofNullable(parser);
    }

    Optional<SetupParser> getDataParser(){
        return dataParser;
    }

    @Deprecated
    public String getCode(){
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, name());
    }
}
