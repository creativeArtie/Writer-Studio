package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

interface SpecParseData extends SetupParser{

    enum Type{
        INTEGER, DURATION, FORMATTED, STRING;
    }

    Type getDataType();

    boolean isUnknown();

    String getSymbol();

    @Override
    public default Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (isUnknown()){
            pointer.matches(children, SPEC_KEY);
            pointer.startsWith(children, SPEC_KEY_DATA);

            pointer.getTo(children, SpanLeafStyle.DATA, SPEC_SEPARATOR);
            pointer.startsWith(children, SPEC_SEPARATOR);

            return Optional.of(new SpecSpanDataString(children));
        }
        if (pointer.startsWith(children, SpanLeafStyle.FIELD, getSymbol())){
            pointer.startsWith(children, SPEC_KEY_DATA);

            pointer.getTo(children, SpanLeafStyle.DATA, SPEC_SEPARATOR);
            pointer.startsWith(children, SPEC_SEPARATOR);

            switch (getDataType()){
            case INTEGER:
                return Optional.of(new SpecSpanDataInt(children));
            case DURATION:
                return Optional.of(new SpecSpanDataTime(children));
            case FORMATTED:
                // return Optional.of(new SpecSpanDataFormatted(children));
            default:
                return Optional.of(new SpecSpanDataString(children));
            }
        }
        return Optional.empty();
    }
}
