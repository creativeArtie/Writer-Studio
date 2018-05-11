package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements the rules {@code design/ebnf.txt DataSpan} and it's rules.
 *
 * The rules {@code Basic} uses are {@code DataMeta} and {@code DataType}.
 */
enum TextDataParser implements SetupParser {
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        /// Gets the line type
        TextDataType.Type type = null;
        for (TextDataType.Area base: TextDataType.Area.values()){
            /// data to be add to the actual document
            if (pointer.startsWith(children, base.getKeyName())){
                type = base;
                break;
            }
        }
        if (type == null){
            for (TextDataType.Meta base: TextDataType.Meta.values()){
                /// data to add to the meta properties
                if (pointer.startsWith(children, base.getKeyName())){
                    type = base;
                    break;
                }
            }
        }
        if (type == null){
            /// data not found = failure
            throw new IllegalArgumentException("Corrupted meta file:" + pointer);
        }

        /// Get the format type
        for (TextDataType.Format format: TextDataType.Format.values()){
            if (pointer.startsWith(children, format.getKeyName())){
                break;
            }
        }

        /// For when the data is for the content itself
        if (type instanceof TextDataType.Area){
            NOTE_TEXT.parse(pointer, children);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new TextDataSpanPrint(children));
        }

        /// For when the data is for meta properties
        CONTENT_BASIC.parse(pointer, children);
        pointer.startsWith(children, LINED_END);
        return Optional.of(new TextDataSpanMeta(children));
    }
}