package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt Format1} and {Format2}. */
final class FormattedParser implements SetupParser {

    private final String[] spanEnders;
    private final SpanLeafStyle leafStyle;
    private final boolean withNote;

    /** Creates a {@linkplain FormattedParser}.
     * @param style
     *      content leaf style
     * @param note
     *      allows note spans
     * @param enders
     *      span enders tokens
     * @see FormattedParser#parse(SetupPointer)
     */
    FormattedParser(SpanLeafStyle style, boolean note, String ... enders){
        /// Combine the list of span enders and formatting enders
        spanEnders = listFormatEnderTokens(note, enders);
        leafStyle = argumentNotNull(style, "style");
        withNote = note;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");

        /// Setup format style
        boolean[] formats = new boolean[]{false, false, false, false};

        /// Setup for FormattedSpan
        ArrayList<Span> children = new ArrayList<>();

        /// check where the loop ends
        boolean more;

        do {
            more = false; /// Assume FormattedSpan has ended

            if (FormatParseAgenda.PARSER.parse(pointer, children)){
                more = true;
            }

            /// for content text first
            if (new FormatParseContent(leafStyle, formats, spanEnders)
                .parse(pointer, children)){
                more = true;
            }

            /// Find the formatting tokens
            for (FormatTypeStyle type: FormatTypeStyle.values()){
                if (pointer.startsWith(children, type.getToken())){
                    /// change format of bold/italics/underline/code
                    formats[type.ordinal()] = ! formats[type.ordinal()];

                    more = true;
                    break;
                }
            }

            /// find reference text
            new FormatParsePointKey(formats).parse(pointer, children);

            /// find link
            for (SetupParser parser: FormatParseLink.getParsers(formats)){
                if (parser.parse(pointer, children)){
                    more = true;
                }
            }

            /// finds not if allowed
            if (withNote){
                for (SetupParser parser: FormatParsePointId.getParsers(formats)){
                    if(parser.parse(pointer, children)){
                        /// Striaght forwarding adding of found spans.
                        more = true;
                    }
                }
            }
        } while(more);

        /// Create span if there are children.
        if (children.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new FormattedSpan(children, withNote));
    }
}
