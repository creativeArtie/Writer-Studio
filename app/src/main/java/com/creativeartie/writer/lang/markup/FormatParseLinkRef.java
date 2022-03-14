package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatRefLink}. */
final class FormatParseLinkRef extends FormatParseLink {

    /** Creates a {@linkplain FormatParseLinkRef}.
     *
     * @param formats
     *      format lists
     * @see FormatParseLink#getParsers(boolean[])
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
