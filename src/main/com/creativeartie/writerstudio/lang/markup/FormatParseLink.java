package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // Arrays, Optional

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link FormatSpanLink} and parent class of
 * {@link FormatParseLinkDirect}, and {@link FormatParseLinkRef}.
 */
abstract class FormatParseLink implements SetupParser {

    public static FormatParseLink[] getParsers(boolean[] formats){

        boolean[] setup = Arrays.copyOf(formats, formats.length);
        return new FormatParseLink[]{
            new FormatParseLinkRef(setup),
            new FormatParseLinkDirect(setup)
        };
    }

    private final String spanStart;
    private final boolean[] formatList;

    FormatParseLink(String start, boolean[] formats){
        spanStart = checkNotNull(start, "starts");
        checkNotNull(formats, "formats");
        checkEqual(formats.length, "formats.length", FORMAT_TYPES);
        formatList = formats;
    }

    protected final boolean[] getFormats(){
        return formatList;
    }

    @Override
    public final Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, spanStart)){
            return parseFinish(children, pointer);
        }
        return Optional.empty();
    }

    abstract Optional<SpanBranch> parseFinish(ArrayList<Span> spanChildren,
        SetupPointer childPointer);

    protected final void parseRest(ArrayList<Span> children,
            SetupPointer pointer){
        checkNotNull(children, "children");
        checkNotNull(pointer, "pointer");
        /// Create display text if any
        if (pointer.startsWith(children, LINK_TEXT)){
            /// Add the text itself
            CONTENT_LINK.parse(children, pointer);
        }

        /// Add the ">"
        pointer.startsWith(children, LINK_END);
    }
}
