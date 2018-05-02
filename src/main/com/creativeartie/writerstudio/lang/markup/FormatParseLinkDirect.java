package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatDirectLink}. */
final class FormatParseLinkDirect extends FormatParseLink {

    /** Creates a {@linkplain FormatParseLinkDirect}.
     *
     * @param formats
     *      format lists
     * @see FormattedParser#getParsers(boolean[])
     */
    FormatParseLinkDirect(boolean[] formats){
        super(LINK_BEGIN, formats);
    }

    @Override
    Optional<SpanBranch> parseSpan(SetupPointer pointer,
            ArrayList<Span> children){
        argumentNotNull(children, "children");
        argumentNotNull(pointer, "pointer");


        CONTENT_LINE_LINK.parse(pointer, children);


        parseRest(pointer, children);
        return Optional.of(new FormatSpanLinkDirect(children, this));
    }
}
