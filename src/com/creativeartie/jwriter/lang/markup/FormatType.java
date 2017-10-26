package com.creativeartie.jwriter.lang.markup;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

import com.creativeartie.jwriter.lang.DetailStyle;
public enum FormatType implements DetailStyle{
    /// Value order mandated by FormatSpan and FormatParser
    BOLD, ITALICS, UNDERLINE, CODED;
    
    @Override 
    public String getStyleClass(){
        return DetailStyle.styleFromEnum(STYLE_FORMAT, name());
    }
}
