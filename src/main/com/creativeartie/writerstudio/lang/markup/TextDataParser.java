package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/** Parser for {@link TextDataSpan}.
 */
enum TextDataParser implements SetupParser {
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        TextDataType.Type type = null;
        for (TextDataType.Area base: TextDataType.Area.values()){
            if (pointer.startsWith(children, base.getKeyName())){
                type = base;
                break;
            }
        }
        if (type == null){
            for (TextDataType.Meta base: TextDataType.Meta.values()){
                if (pointer.startsWith(children, base.getKeyName())){
                    type = base;
                    break;
                }
            }
        }
        if (type == null){
            throw new IllegalArgumentException("Corrupted meta file.");
        }
        for (TextDataType.Format format: TextDataType.Format.values()){
            if (pointer.startsWith(children, format.getKeyName())){
                break;
            }
        }
        if (type instanceof TextDataType.Area){
            FORMATTED_DATA.parse(children, pointer);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new TextDataSpanPrint(children));
        }

        CONTENT_DATA.parse(children, pointer);
        pointer.startsWith(children, LINED_END);
        return Optional.of(new TextDataSpanMeta(children));
    }
}