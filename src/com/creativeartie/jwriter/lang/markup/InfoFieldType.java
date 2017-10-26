package com.creativeartie.jwriter.lang.markup;

import java.util.Optional;

import com.google.common.base.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.lang.*;

public enum InfoFieldType implements DetailStyle{
    /* //TODO add in future version (for automatic InfoFieldType Style)
    AUTHOR, EDITOR, TRANSLATOR, ARTICLE, TITLE, EDITION, PUBLISH_HOUSE,
    PUBLISH_YEAR, MEDIA, ACCESS_LOCATION, ACCESS_DATE, */
    // PAGES(InfoDataParser.NUMBER), // Maybe later version

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

    @Override
    public String getStyleClass(){
        return DetailStyle.styleFromEnum(STYLE_FIELD, name());
    }

    @Deprecated
    public String getCode(){
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, name());
    }
}
