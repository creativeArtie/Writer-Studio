package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements the rules {@code design/ebnf.txt DataSpan} and it's rules.
 *
 * The rules {@code Basic} uses are {@code DataMeta} and {@code DataType}.
 */
class TextParseMeta implements SetupParser {

    public static TextParseMeta[] buildParsers(){
        TextParseMeta ans = new TextParseMeta[TextTypeMeta.values()];
        int i = 0;
        for (TextParseMeta matter: TextParseMeta.values()){
            ans[i++] = new TextParseMeta(matter.getKeyName());
        }
        return ans;
    }

    private final String keyName;

    private TextParseMeta(String key){
        assert key != null: "Null key";
        keyName = key;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (! pointer.trimStartsWith(children, SpanLeafStyle.FIELD, keyName){
            return Optional.empty();
        }

        pointer.getTo(children, TEXT_DATA_START, SPEC_ROW_END);

        while (! pointer.startsWith(children, SPEC_ROW_END) &&
                pointer.hasNext()){
            for (TextParseMatter data: TextParseMatter.values()){
                if (data.parse(pointer, children)){
                    break;
                }
            }
        }

        pointer.startsWith(children, TEXT_DATA_START);

        pointer.getTo(children, SpanLeafStyle.

        return Optional.ofNullable(children.isEmpty()? null:
            new TextSpanMeta(children));
    }


}
