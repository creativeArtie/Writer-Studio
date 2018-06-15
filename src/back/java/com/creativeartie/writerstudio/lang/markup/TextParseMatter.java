package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements the rules {@code design/ebnf.txt DataSpan} and it's rules.
 *
 * The rules {@code Basic} uses are {@code DataMeta} and {@code DataType}.
 */
class TextParseMatter implements SetupParser {

    public static TextParseMatter[] buildParsers(){
        TextParseMatter ans = new TextParseMatter[TextTypeMatter.values()];
        int i = 0;
        for (TextTypeMatter matter: TextTypeMatter.values()){
            ans[i++] = new TextParseMatter(matter.getKeyName());
        }
        return ans;
    }

    private final String keyName;

    private TextParseMatter(String key){
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

        pointer.getTo(children, SpanLeafStyle.DATA, SPEC_SEPARATOR);

        while (! pointer.startsWith(children, TEXT_DATA_START, SPEC_ROW_END) &&
                pointer.hasNext()){
            for (TextParseMatter data: TextParseMatter.values()){
                if (data.parse(pointer, children)){
                    break;
                }
            }
        }

        pointer.startsWith(children, TEXT_DATA_START);

        NOTE_TEXT.parse(children, pointer);

        return Optional.ofNullable(children.isEmpty()? null:
            new TextSpanMatter(children));
    }


}
