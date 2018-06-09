package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

enum StatParseData implements SpecParseData{

    PUBLISH_TOTAL(STAT_PUBLISH_COUNT), PUBLISH_GOAL(STAT_PUBLISH_GOAL),
    NOTE_TOTAL(STAT_NOTE_COUNT),
    TIME_TOTAL(STAT_TIME_COUNT), TIME_GOAL(STAT_TIME_GOAL),
    UNKNOWN("");

    private String dataSymbol;

    private StatParseData(String symbol){
        dataSymbol = symbol;
    }

    @Override
    public SpecParseData.Type getDataType(){
        if (ordinal() < TIME_TOTAL.ordinal()){
            return SpecParseData.Type.INTEGER;
        } else if (ordinal() < UNKNOWN.ordinal()){
            return SpecParseData.Type.DURATION;
        }
        return SpecParseData.Type.STRING;
    }

    @Override
    public boolean isUnknown(){
        return this == UNKNOWN;
    }

    @Override
    public String getSymbol(){
        return dataSymbol;
    }
}
