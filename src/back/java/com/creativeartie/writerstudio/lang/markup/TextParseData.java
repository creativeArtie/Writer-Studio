package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

enum TextParseData implements SpecParseData{
    ALGIN(TEXT_ALIGN), UNKNOWN("");

    private String keyName;

    private StatParseField(String name){
        String keyName = name;
    }

    public String getKeyName(){
        return keyName;
    }

    public boolean isUnknown(){
        return this == UNKNOWN;
    }

    public StatSpanField createSpan(ArrayList<Span> children){
        return new StatSpanField(children);
    }
}
