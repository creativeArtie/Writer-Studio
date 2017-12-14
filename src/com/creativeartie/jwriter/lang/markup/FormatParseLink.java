package com.creativeartie.jwriter.lang.markup;

import java.util.*;


import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.*;

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
        spanStart = Checker.checkNotNull(start, "starts");
        formatList = Checker.checkArraySize(formats, "formats", FORMAT_TYPES);
    }

    protected boolean[] getFormats(){
        return formatList;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, spanStart)){
            return parseFinish(children, pointer);
        }
        return Optional.empty();
    }

    protected abstract Optional<SpanBranch> parseFinish(
        ArrayList<Span> spanChildren, SetupPointer childPointer);

    protected void parseRest(ArrayList<Span> children,
            SetupPointer pointer){

        /// Create display text if any
        if (pointer.startsWith(children, LINK_TEXT)){
            /// Add the text itself
            new ContentParser(LINK_END).parse(children, pointer);
        }

        /// Add the ">"
        pointer.startsWith(children, LINK_END);
    }
}
