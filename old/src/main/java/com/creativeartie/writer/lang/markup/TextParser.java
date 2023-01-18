package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements the rules {@code design/ebnf.txt DataSpan} and it's rules.
 *
 * The rules {@code Basic} uses are {@code DataMeta} and {@code DataType}.
 */
enum TextParser implements SetupParser {
    PARSER;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        /// Gets the line type
        TextType type = null;
        for (TextTypeMatter base: TextTypeMatter.values()){
            /// data to be add to the actual document
            if (pointer.trimStartsWith(children, SpanLeafStyle.FIELD,
                    base.getKeyName())){
                type = base;
                break;
            }
        }
        if (type == null){
            for (TextTypeInfo base: TextTypeInfo.values()){
                /// data to add to the meta properties
                if (pointer.trimStartsWith(children, SpanLeafStyle.FIELD,
                        base.getKeyName())){
                    type = base;
                    break;
                }
            }
        }

        if (type == null){
            pointer.matches(children, SpanLeafStyle.FIELD, TEXT_KEY_TEXT);
        }

        pointer.trimStartsWith(children, TEXT_SEPARATOR);

        /// Get the format type
        for (TextDataType format: TextDataType.values()){
            if (format == TextDataType.UNKNOWN){
                pointer.matches(children, SpanLeafStyle.DATA, TEXT_KEY_TEXT);
            } else if (pointer.trimStartsWith(children, SpanLeafStyle.DATA,
                    format.getKeyName())){
                break;
            }
        }

        pointer.trimStartsWith(children, TEXT_SEPARATOR);

        /// For when the data is for the content itself
        if (type instanceof TextTypeMatter){
            NOTE_TEXT.parse(pointer, children);
            pointer.startsWith(children, LINED_END);
            return Optional.of(new TextSpanMatter(children));
        }

        /// For when the data is for meta properties
        CONTENT_BASIC.parse(pointer, children);
        pointer.startsWith(children, LINED_END);
        if (type instanceof TextTypeInfo){
            return Optional.of(new TextSpanInfo(children));
        } else {
            return Optional.of(new TextSpanUnkown(children));
        }
    }
}
