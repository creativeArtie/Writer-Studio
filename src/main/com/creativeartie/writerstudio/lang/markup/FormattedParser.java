package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link FormattedSpan}
 */
final class FormattedParser implements SetupParser {

    private final String[] spanEnders;
    private final StyleInfoLeaf leafStyle;
    private final boolean withNote;

    public FormattedParser(StyleInfoLeaf style, boolean note, String ... enders){
        /// Combine the list of span enders and formatting enders
        checkNotNull(enders, "enders");
        spanEnders = listFormatEnderTokens(note, enders);
        leafStyle = checkNotNull(style, "style");
        withNote = note;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");

        /// Setup format style: bold, italics, underline, coded
        boolean[] formats = new boolean[]{false, false, false, false};

        /// Setup for FormattedSpan
        ArrayList<Span> children = new ArrayList<>();

        /// check where the loop ends
        boolean more;

        do {
            more = false; /// Assume FormattedSpan has ended

            /// try to find text first
            if (new FormatParseContent(leafStyle, formats, spanEnders)
                .parse(pointer, children)
            ){
                more = true;
            }

            if (FormatParseAgenda.PARSER.parse(pointer, children)){
                more = true;
            }

            /// Keeps FomratContentParser parsing alone b/c of needs to edit
            /// format
            int i = 0;
            for (String type: FORMAT_KEYS){
                if (pointer.startsWith(children, type)){
                    /// change format of bold/italics/underline/code
                    formats[i] = ! formats[i];

                    more = true;
                    break;
                }
                i++;
            }

            new FormatParsePointKey(formats).parse(pointer, children);

            if (! withNote){
                for (SetupParser parser: FormatParseLink.getParsers(formats)){
                    if (parser.parse(pointer, children)){
                        more = true;
                    }
                }
                continue;
            }

            /// Lastly deal with FormatParseCurly and FormatParseLink together
            for (SetupParser parser: SetupParser.combine(
                FormatParsePointId.getParsers(formats),
                FormatParseLink.getParsers(formats)
            )){
                if(parser.parse(pointer, children)){
                    /// Striaght forwarding adding of found spans.
                    more = true;
                }
            }
        } while(more);

        /// Add the FormattedParser with its children spans if there are children.
        if (children.size() > 0){
            return Optional.of(new FormattedSpan(children, withNote));
        }
        return Optional.empty();
    }
}
