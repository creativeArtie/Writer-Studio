package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

public enum StatParseRecord implements SetupParser{
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        pointer.getTo(children, SpanLeafStyle.DATA, SPEC_SEPARATOR);

        while (! pointer.startsWith(children, SPEC_ROW_END) &&
                pointer.hasNext()){
            for (StatParseField data: StatParseField.values()){
                if (data.parse(pointer, children)){
                    break;
                }
            }
        }
        return Optional.ofNullable(children.isEmpty()? null:
            new StatSpanRecord(children));
    }

}
