package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt Basic} and the rules it uses.
 *
 * The rules {@code Basic} uses are {@code Raw} and {@code Escape}.
 */
abstract class BasicParseText implements SetupParser{

    private final ImmutableList<String> setupEnders;
    private final SpanLeafStyle leafStyle;

    /** Creates a {@linkplain BasicParseText}.
     *
     * @param style
     *      non-key text leaf styles
     * @param enders
     *      span ending tokens
     */
    BasicParseText(SpanLeafStyle style, String ... enders){
        argumentNotNull(style, "style");
        argumentNotNull(enders, "enders");

        ImmutableList.Builder<String> builder = ImmutableList.builder();

        builder.add(LINED_END);

        for (String ender: enders){
            if (! ender.equals(LINED_END) && ! ender.equals(TOKEN_ESCAPE)){
                /// Ignores argument LINED_END and TOKEN_ESCAPE
                builder.add(ender);
            }
        }

        /// For setup parser.
        builder.add(TOKEN_ESCAPE);
        setupEnders = builder.build();
        leafStyle = style;
    }


    @Override
    public final Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");

        /// Setup
        ArrayList<Span> children = new ArrayList<>();
        boolean more;

        /// Extract the text
        do{
            /// = design/ebnf.txt Raw
            pointer.getTo(children, leafStyle, setupEnders);

            /// no more escape span = no more text to deal with
            more = parseEscape(pointer, children);
        } while (more);

        /// Create span if there are Span extracted
        if (! children.isEmpty()) {
            return Optional.of(buildSpan(children));
        }
        return Optional.empty();
    }

    /** Creates the Span after the parsing complete.
     *
     * @param children
     *    span children
     * @return answer
     */
    protected abstract SpanBranch buildSpan(List<Span> children);

    /** Parse {@link BasicTextEscape}.
     *
     * @param parent
     *      parent span list
     * @param pointer
     *      setup pointer
     * @return success
     */
    private final boolean parseEscape(SetupPointer pointer, List<Span> parent){
        assert pointer != null;
        assert parent != null;

        /// Setup
        ArrayList<Span> children = new ArrayList<>();

        /// build Span if found
        if (pointer.startsWith(children, TOKEN_ESCAPE)){
            /// = design/ebnf.txt Escape
            pointer.nextChars(children, leafStyle, 1);
            parent.add(new BasicTextEscape(children));
            return true;
        }
        return false;
    }
}
