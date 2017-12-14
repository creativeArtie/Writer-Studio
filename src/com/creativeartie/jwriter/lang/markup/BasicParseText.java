package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Parser for {@link BasicText} with {@link BasicTextEscape}.
 */
abstract class BasicParseText implements SetupParser{

    /// Describes how the Span will end
    private final ImmutableList<String> setupEnders;
    private final ImmutableList<String> reparseEnders;
    private final StyleInfoLeaf leafStyle;

    public BasicParseText(StyleInfoLeaf style, String ... enders){
        checkNotNull(style, "style");

        /// This builder is use to create two separate list, one for parsing,
        /// another for check if text can be parsed entirely.
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        builder.add(LINED_END);

        for (String ender: enders){
            if (! ender.equals(LINED_END) && ! ender.equals(CHAR_ESCAPE)){
                /// Ignores argument LINED_END and CHAR_ESCAPE
                builder.add(ender);
            }
        }

        reparseEnders = builder.build();

        // For setup parser.
        builder.add(CHAR_ESCAPE);
        setupEnders = builder.build();
        leafStyle = style;
    }

    /** Check if a text can be parse entirely. */
    boolean canParse(String text){
        return canParse(checkNotNull(text, "text"), reparseEnders);

    }

    static boolean canParse(String text, List<String> parseList){
        return canParse(text, parseList.toArray(new String[0]));
    }

    static boolean canParse(String text, String ... parseList){
        checkNotNull(text, "text");

        boolean isEscaped = false;
        for(int i = 0; i < text.length(); i++){
            if (! isEscaped){
                if (text.startsWith(CHAR_ESCAPE, i)){
                    isEscaped = true;
                } else {
                    for (String ender: parseList){
                        if (text.startsWith(ender, i)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
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
