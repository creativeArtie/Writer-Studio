package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List

import com.creativeartie.writerstudio.lang.*; // Span, StyleInfoLeaf

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Parser for {@link FormatSpanContent}.
 */
final class FormatParseContent extends BasicParseText{

    private boolean[] formatList;

    public FormatParseContent(StyleInfoLeaf style, boolean[] formats,
            String ... enders){
        super(style, enders);
        checkNotNull(formats, "formats");
        checkEqual(formats.length, "formats.length", FORMAT_TYPES);
        formatList = Arrays.copyOf(formats, formats.length);
    }

    @Override
    protected FormatSpanContent buildSpan(List<Span> children){
        checkNotNull(children, "children");
        return new FormatSpanContent(children, formatList, this);
    }

}
