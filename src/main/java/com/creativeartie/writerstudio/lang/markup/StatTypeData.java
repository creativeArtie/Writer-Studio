package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public enum StatTypeData implements StyleInfo{
    PUBLISH_TOTAL, PUBLISH_GOAL,
    NOTE_TOTAL,
    TIME_TOTAL, TIME_GOAL,
    UNKNOWN;

    public static StatTypeData parse(String key){
        if (key == null){
            return StatTypeData.UNKNOWN;
        }
        for (SpecParseData parser: SpecParseData.values()){
            if (parser.getSymbol().equals(key)){
                return values()[parser.ordinal()];
            }
        }
        return StatTypeData.UNKNOWN;
    }
}
