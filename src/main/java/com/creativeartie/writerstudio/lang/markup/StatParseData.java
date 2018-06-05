package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

enum StatParseData implements SetupParser{

    PUBLISH_TOTAL(STAT_PUBLISH_COUNT), PUBLISH_GOAL(STAT_PUBLISH_GOAL),
    NOTE_TOTAL(STAT_NOTE_COUNT),
    TIME_TOTAL(STAT_TIME_COUNT), TIME_GOAL(STAT_TIME_GOAL),
    UNKNOWN("");

    private String dataSymbol;

    private StatParseData(String symbol){
        dataSymbol = symbol;
    }

    String getSymbol(){
        return dataSymbol;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (this == UNKNOWN){
            pointer.getTo(children, StyleInfoLeaf.FIELD, STAT_DATA_SEP);
            pointer.startsWith(children, STAT_DATA_SEP);

            pointer.getTo(children, StyleInfoLeaf.DATA, STAT_SEPARATOR);
            pointer.startsWith(children, STAT_SEPARATOR);

            return Optional.of(new StatSpanDataString(children));
        }
        if (pointer.startsWith(children, StyleInfoLeaf.FIELD, dataSymbol)){
            pointer.startsWith(children, STAT_DATA_SEP);

            pointer.getTo(children, StyleInfoLeaf.DATA, STAT_SEPARATOR);
            pointer.startsWith(children, STAT_SEPARATOR);

            if (ordinal() < TIME_TOTAL.ordinal()){
                return Optional.of(new StatSpanDataInt(children));
            } else {
                return Optional.of(new StatSpanDataTime(children));
            }
        }
        return Optional.empty();
    }
}
