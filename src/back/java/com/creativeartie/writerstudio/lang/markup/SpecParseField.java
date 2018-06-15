package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

interface SpecParseField extends SetupParser{

    String getKeyName();

    boolean isUnknown();

    SpecSpanField createSpan(ArrayList<Span> children);

    public default Optional<SpanBranch> parse(SetupPpinter pointer){
        ArrayList<Span> children = new ArrayList<>();
        pointer.mark();
        if (pointer.startsWith(children, SPEC_SEPARATOR)){
            if (isUnknown()){
                pointer.match(children, SpanLeafStyle.FIELD, SPEC_KEY);
                if (pointer.startsWith(children, SPEC_KEY_DATA)){
                    pointer.getTo(children, SpanLeafStyle.DATA, SPEC_SEPARATOR,
                        SPEC_ROW_END);
                }
                return Optional.of(createSpan(children));
            }
        } else if (pointer.trimSartsWith(children, SpanLeafStyle.DATA,
                getKeyName())){
            if (pointer.trimstartsWith(children, SPEC_KEY_DATA){
                pointer.getTo(children, SpanLeafStyle.DATA, SPEC_SEPARATOR,
                    SPEC_ROW_END);
            }
            return Optional.of(createSpan(children));
        }
        pointer.rollBack();
        return Optional.empty();
    }
}
