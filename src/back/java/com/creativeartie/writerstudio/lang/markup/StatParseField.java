package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

enum StatParseField implements SpecParseData{
    PUBLISH_TOTAL(STAT_PUBLISH_COUNT), PUBLISH_GOAL(STAT_PUBLISH_GOAL),
    NOTE_TOTAL(STAT_NOTE_COUNT),

    TIME_TOTAL(STAT_TIME_COUNT), TIME_GOAL(STAT_TIME_GOAL), UNKNOWN("");

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
