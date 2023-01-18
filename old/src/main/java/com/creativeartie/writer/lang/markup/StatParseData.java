package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.main.ParameterChecker.*;
import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;

enum StatParseData implements SetupParser{

    PUBLISH_TOTAL(STAT_PUBLISH_COUNT), PUBLISH_GOAL(STAT_PUBLISH_GOAL),
    NOTE_TOTAL(STAT_NOTE_COUNT),
    TIME_TOTAL(STAT_TIME_COUNT), TIME_GOAL(STAT_TIME_GOAL),
    UNKNOWN("");

    enum Type{
        INTEGER, DURATION, STRING;
    }

    private String dataSymbol;

    private StatParseData(String symbol){
        dataSymbol = symbol;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (this == UNKNOWN){
            pointer.matches(children, STAT_KEY_TEXT);
            pointer.startsWith(children, STAT_KEY_DATA);

            pointer.getTo(children, SpanLeafStyle.DATA, STAT_SEPARATOR);
            pointer.startsWith(children, STAT_SEPARATOR);

            return Optional.of(new StatSpanDataString(children));
        }
        if (pointer.startsWith(children, SpanLeafStyle.FIELD, getSymbol())){
            pointer.startsWith(children, STAT_KEY_DATA);

            pointer.getTo(children, SpanLeafStyle.DATA, STAT_SEPARATOR);
            pointer.startsWith(children, STAT_SEPARATOR);

            switch (getDataType()){
            case INTEGER:
                return Optional.of(new StatSpanDataInt(children));
            case DURATION:
                return Optional.of(new StatSpanDataTime(children));
            default:
                return Optional.of(new StatSpanDataString(children));
            }
        }
        return Optional.empty();
    }

    public Type getDataType(){
        if (ordinal() < TIME_TOTAL.ordinal()){
            return Type.INTEGER;
        } else if (ordinal() < UNKNOWN.ordinal()){
            return Type.DURATION;
        }
        return Type.STRING;
    }

    public String getSymbol(){
        return dataSymbol;
    }
}
