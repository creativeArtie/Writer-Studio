package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;


import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatContent}. */
final class FormatParseContent extends BasicParseText{

    private boolean[] formatList;

    /** Creates a {@linkplain FormatParseContent}.
     *
     * @param style
     *      text leaves style
     * @param formats
     *      format lists
     * @param enders
     *      span ending tokens
     * @see FormattedParser#parse(SetupPointer)
     */
    FormatParseContent(SpanLeafStyle style, boolean[] formats,
            String ... enders){
        super(style, enders);
        indexEquals(formats.length, "formats.length", FORMAT_TYPES);
        formatList = Arrays.copyOf(formats, formats.length);
    }

    @Override
    protected FormatSpanContent buildSpan(List<Span> children){
        argumentNotNull(children, "children");
        return new FormatSpanContent(children, formatList, this);
    }

}
