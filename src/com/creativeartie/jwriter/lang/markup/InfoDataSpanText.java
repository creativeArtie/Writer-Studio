package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.creativeartie.jwriter.property.*;

import com.creativeartie.jwriter.lang.*;

public class InfoDataSpanText extends InfoDataSpan{

    @Override
    public ContentSpan getData(){
        return (ContentSpan)get(0);
    }

    InfoDataSpanText(List<Span> children){
        super(children, InfoDataType.TEXT);
    }
}
