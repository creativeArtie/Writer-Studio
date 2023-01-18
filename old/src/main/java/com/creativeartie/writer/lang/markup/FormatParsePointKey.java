package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatReference}. */
class FormatParsePointKey extends FormatParsePoint {

    /** Creates a {@linkplain FormatParsePointKey}.
     * @param formats
     *      format list
     * @see FormattedParser#parse(SetupPointer)
     */
    FormatParsePointKey(boolean[] formats){
        super(CURLY_KEY, formats);
    }

    @Override
    void parseContent(SetupPointer pointer, ArrayList<Span> children){
        CONTENT_KEY.parse(pointer, children);
    }

    @Override
    SpanBranch buildSpan(ArrayList<Span> children){
        argumentNotNull(children, "children");
        return new FormatSpanPointKey(children, getFormats(), this);
    }
}