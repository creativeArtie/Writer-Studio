package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public class InfoDataSpanFormatted extends InfoDataSpan<FormatSpanMain>{
    
    @Override
    public InfoDataSpanFormatted cast(){
        return this;
    }
    
    @Override
    public FormatSpanMain getData(){
        return spanAtFirst(FormatSpanMain.class).get();
    }
    
    protected InfoDataSpanFormatted(List<Span> children){
        super(children, InfoDataType.FORMATTED);
    }
}
