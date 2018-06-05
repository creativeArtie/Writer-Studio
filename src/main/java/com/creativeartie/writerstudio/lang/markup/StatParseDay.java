package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

public enum StatParseDay implements SetupParser{
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        getParsePartDate(pointer, children, STAT_DATE_SEP); /// year
        getParsePartDate(pointer, children, STAT_DATE_SEP); /// month
        getParsePartDate(pointer, children, STAT_SEPARATOR); /// day

        while (! pointer.startsWith(children, STAT_DATE_END) &&
                pointer.hasNext()){
            for (StatParseData data: StatParseData.values()){
                if (data.parse(pointer, children)){
                    break;
                }
            }
        }
        return Optional.ofNullable(children.isEmpty()? null:
            new StatSpanDay(children));
    }

    private void getParsePartDate(SetupPointer pointer,
        ArrayList<Span> children, String ender
    ){
        pointer.getTo(children, StyleInfoLeaf.DATA, ender);
        pointer.startsWith(children, ender);
    }
}
