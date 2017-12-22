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

    /// willReparse = true if beginning change
    private final boolean willReparse;
    private final ImmutableList<String> reparseEnders;
    private final StyleInfoLeaf leafStyle;

    public BasicParseText(boolean reparse, StyleInfoLeaf style,
            String ... enders){
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

        willReparse = reparse;

        reparseEnders = builder.build();

        // For setup parser.
        builder.add(CHAR_ESCAPE);
        setupEnders = builder.build();
        leafStyle = style;
    }

    /** Check if a text can be parse entirely. */
    final boolean canParse(String text){
        checkNotNull(text, "text");
        return willReparse && checkParse(checkNotNull(text, "text"),
            reparseEnders);

    }

    static boolean checkLineEnd(boolean optional, String text){
        checkNotNull(text, "text");
        return optional?
            canParse(text.substring(0, text.length() - LINED_END.length()),
                LINED_END):
            willEndWith(text, LINED_END);
    }
    static boolean willEndWith(String text, String ender, List<String> endings){
        checkNotNull(text, "text");
        checkNotNull(ender, "ender");
        checkNotNull(endings, "endings");
        return willEndWith(text, ender, endings.toArray(new String[0]));
    }

    static boolean willEndWith(String text, String ender, String ... endings){
        checkNotNull(text, "text");
        checkNotNull(ender, "ender");
        return text.endsWith(ender)?
            canParse(text.substring(0, text.length() - ender.length()),
                SetupParser.combine(endings, ender)): false;
    }

    static boolean canParse(String text, List<String> endings){
        checkNotNull(text, "text");
        checkNotNull(endings, "endings");
        return canParse(text, endings.toArray(new String[endings.size()]));
    }

    static boolean canParse(String text, String ... endings){
        checkNotNull(text, "text");
        checkNotNull(endings, "endings");
        return checkParse(text, Arrays.asList(SetupParser.combine(endings,
            LINED_END)));
    }

    private static boolean checkParse(String text, List<String> endings){
        assert text != null: "Null text";
        assert endings != null: "Null endings";

        boolean isEscaped = false;
        for(int i = 0; i < text.length(); i++){
            if (! isEscaped){
                if (text.startsWith(CHAR_ESCAPE, i)){
                    isEscaped = true;
                } else {
                    for (String ender: endings){
                        if (text.startsWith(ender, i)){
                            return false;
                        }
                    }
                }
            } else {
                isEscaped = false;
            }
        }
        return ! isEscaped;
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
