package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, List, Optional

import com.google.common.collect.*; // ImmuableList

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link BasicText} with {@link BasicTextEscape}.
 *
 */
abstract class BasicParseText implements SetupParser{

    /// Describes how the Span will end
    private final ImmutableList<String> setupEnders;

    private final StyleInfoLeaf leafStyle;

    public BasicParseText(StyleInfoLeaf style, String ... enders){
        checkNotNull(style, "style");
        checkNotNull(enders, "enders");

        ImmutableList.Builder<String> builder = ImmutableList.builder();

        builder.add(LINED_END);

        for (String ender: enders){
            if (! ender.equals(LINED_END) && ! ender.equals(CHAR_ESCAPE)){
                /// Ignores argument LINED_END and CHAR_ESCAPE
                builder.add(ender);
            }
        }

        // For setup parser.
        builder.add(CHAR_ESCAPE);
        setupEnders = builder.build();
        leafStyle = style;
    }


    @Override
    public final Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        /// Setup
        ArrayList<Span> children = new ArrayList<>();
        boolean more;

        /// Extract the text
        do{
            pointer.getTo(children, leafStyle, setupEnders);
            /// no more escape span = no more text to deal with
            more = parseEscape(children, pointer);
        } while (more);

        /// Create span if there are Span extracted
        if (! children.isEmpty()) {
            return Optional.of(buildSpan(children));
        }
        return Optional.empty();
    }

    /** Creates the Span after the parsing complete*/
    protected abstract SpanBranch buildSpan(List<Span> children);

    /** Parse {@link BasicTextEscape}. Helper method for parse(SetupPointer)*/
    private boolean parseEscape(List<Span> parent, SetupPointer pointer){
        checkNotNull(parent, "parent");
        checkNotNull(pointer, "pointer");

        /// Setup
        ArrayList<Span> children = new ArrayList<>();

        /// build Span if found
        if (pointer.startsWith(children, CHAR_ESCAPE)){
            pointer.nextChars(children, leafStyle, 1);
            parent.add(new BasicTextEscape(children));
            return true;
        }
        return false;
    }
}
