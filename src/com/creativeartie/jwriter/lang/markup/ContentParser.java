package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.main.*;

/**
 * Creates a text span upto a certain character.
 */
class ContentParser extends BasicParseText{
    
    public ContentParser(List<String> enders){
        super(SetupLeafStyle.TEXT, enders);
    }
    
    public ContentParser(SetupLeafStyle style, List<String> enders){
        super(style, enders);
    }
    
    public ContentParser(SetupLeafStyle spanStyle, String ... spanEnders){
        super(spanStyle, spanEnders);
    }
    
    public ContentParser(String ... spanEnders){
        super(SetupLeafStyle.TEXT, spanEnders);
    }
    
    @Override
    protected ContentSpan buildSpan(List<Span> children, 
        List<String> enders, SetupLeafStyle style
    ){
        Checker.checkNotNull(children, "children");
        Checker.checkNotNull(enders, "enders");
        Checker.checkNotNull(style, "style");
        return new ContentSpan(children, enders, style);
    }
    
}
