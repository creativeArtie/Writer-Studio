package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.main.ParameterChecker.*;
import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;

public enum StatParseDay implements SetupParser{
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        getParsePartDate(pointer, children, STAT_SEPARATOR);

        while (! pointer.startsWith(children, STAT_ROW_END) &&
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
        pointer.getTo(children, SpanLeafStyle.DATA, ender);
        pointer.startsWith(children, ender);
    }
}
