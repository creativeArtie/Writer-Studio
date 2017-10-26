package com.creativeartie.jwriter.lang.markup;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * All styles that are not really part of any group of styles.
 */
public enum AuxiliaryStyle implements DetailStyle{
    ESCAPE(      STYLE_OTHER), /// For BasicTextEscape
    NO_ID(       STYLE_OTHER), /// For LinedSpanPoint, MainSpanNote
    DATA_ERROR(  STYLE_OTHER), /// For LinedSpanCite
    MAIN_SECTION( STYLE_MAIN), /// For MainSpanSection
    MAIN_NOTE(    STYLE_MAIN), /// For MainSpanNote
    AGENDA(     STYLE_INLINE), /// For FormatSpanAgenda
    DIRECT_LINK(STYLE_INLINE), /// For FormatSpanLinkDirect
    REF_LINK(   STYLE_INLINE); /// For FormatSpanLinkRef
    
    private final String stylePrefix;
    
    private AuxiliaryStyle(String prefix){
        stylePrefix = prefix;
    }
    
    @Override
    public String getStyleClass(){
        return DetailStyle.styleFromEnum(stylePrefix, name());
    }
        
}
