package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatDirectLink}. */
final class FormatParseLinkDirect extends FormatParseLink {

    /** Creates a {@linkplain FormatParseLinkDirect}.
     *
     * @param formats
     *      format lists
     * @see FormatParseLink#getParsers(boolean[])
     */
    FormatParseLinkDirect(boolean[] formats){
        super(LINK_BEGIN, formats);
    }

    @Override
    SpanBranch parseSpan(SetupPointer pointer, ArrayList<Span> children){
        argumentNotNull(children, "children");
        argumentNotNull(pointer, "pointer");

        CONTENT_LINE_LINK.parse(pointer, children);

        parseRest(pointer, children);
        return new FormatSpanLinkDirect(children, getFormats(), this);
    }
}
