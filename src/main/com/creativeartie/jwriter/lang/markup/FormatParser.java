package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link FormatSpanMain}
 */
final class FormatParser implements SetupParser {

    private final String[] spanEnders;
    private final StyleInfoLeaf leafStyle;

    public FormatParser(StyleInfoLeaf style, String ... enders){
        /// Combine the list of span enders and formatting enders
        checkNotNull(enders, "enders");
        spanEnders = SetupParser.combine(listFormatEnderTokens(), enders);
        leafStyle = checkNotNull(style, "style");
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");

        /// Setup format style: bold, italics, underline, coded
        boolean[] formats = new boolean[]{false, false, false, false};

        /// Setup for FormatSpanMain
        ArrayList<Span> children = new ArrayList<>();

        /// check where the loop ends
        boolean more;

        do {
            more = false; /// Assume FormatSpanMain has ended

            /// try to find text first
            if (new FormatParseContent(leafStyle, formats, spanEnders)
                .parse(children, pointer)
            ){
                more = true;
            }

            if (FormatParseAgenda.PARSER.parse(children, pointer)){
                more = true;
            }

            /// Keeps FomratContentParser parsing alone b/c of needs to edit 
            /// format
            int i = 0;
            for (String type : listFormatTextTokens()){
                if (pointer.startsWith(children, type)){
                    /// change format of bold/italics/underline/code
                    formats[i] = ! formats[i];

                    more = true;
                    break;
                }
                i++;
            }

            /// Lastly deal with FormatParseCurly and FormatParseLink together
            for (SetupParser parser: SetupParser.combine(
                FormatParseDirectory.getParsers(formats),
                FormatParseLink.getParsers(formats)
            )){
                if(parser.parse(children, pointer)){
                    /// Striaght forwarding adding of found spans.
                    more = true;
                }
            }
        } while(more);

        /// Add the FormatParser with its children spans if there are children.
        if (children.size() > 0){
            return Optional.of(new FormatSpanMain(children));
        }
        return Optional.empty();
    }
}
