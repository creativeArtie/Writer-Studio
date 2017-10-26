package com.creativeartie.jwriter.lang;


import static com.creativeartie.jwriter.lang.SetupStrings.*;

public enum SetupLeafStyle implements DetailStyle {
    KEYWORD, ID, FIELD, DATA, PATH, TEXT;
    
    @Override
    public String getStyleClass(){
        return DetailStyle.styleFromEnum(STYLE_BASIC, name());
    }
}
