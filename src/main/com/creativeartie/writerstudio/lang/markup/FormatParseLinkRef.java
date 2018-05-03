package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatRefLink}. */
final class FormatParseLinkRef extends FormatParseLink {

    /** Creates a {@linkplain FormatParseLinkDirect}.
     *
     * @param formats
     *      format lists
     * @see FormattedParser#getParsers(boolean[])
     */
    FormatParseLinkRef(boolean[] formats){
        super(LINK_REF, formats);
    }

    @Override
    SpanBranch parseSpan(SetupPointer pointer, ArrayList<Span> children){
        argumentNotNull(children, "children");
        argumentNotNull(pointer, "pointer");

        DirectoryParser.REF_LINK.parse(pointer, children);
        parseRest(pointer, children);

        return new FormatSpanLinkRef(children, getFormats(), this);
    }
}
