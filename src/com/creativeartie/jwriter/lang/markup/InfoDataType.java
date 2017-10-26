package com.creativeartie.jwriter.lang.markup;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.lang.*;

public enum InfoDataType implements DetailStyle{
    FORMATTED, NUMBER, TEXT;
    
    @Override
    public String getStyleClass(){
        return DetailStyle.styleFromEnum(STYLE_DATA, name());
    }
}
