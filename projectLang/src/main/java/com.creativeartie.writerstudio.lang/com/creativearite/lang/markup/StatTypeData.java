package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public enum StatTypeData{
    PUBLISH_TOTAL, PUBLISH_GOAL,
    NOTE_TOTAL,
    TIME_TOTAL, TIME_GOAL,
    UNKNOWN;

    public static StatTypeData parse(String key){
        if (key == null){
            return StatTypeData.UNKNOWN;
        }
        for (StatParseData parser: StatParseData.values()){
            if (parser.getSymbol().equals(key)){
                return values()[parser.ordinal()];
            }
        }
        return StatTypeData.UNKNOWN;
    }

    StatParseData getParser(){
        return StatParseData.values()[ordinal()];
    }
}
