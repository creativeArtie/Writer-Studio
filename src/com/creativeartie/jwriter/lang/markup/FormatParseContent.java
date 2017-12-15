package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link FormatSpanContent}.
 */
final class FormatParseContent extends BasicParseText{

    private boolean[] formatList;

    public FormatParseContent(StyleInfoLeaf style, boolean[] formats,
            boolean reparse, List<String> enders){
        this(style, formats, reparse, checkNotNull(enders, "enders")
            .toArray(new String[0]));
    }

    public FormatParseContent(StyleInfoLeaf style, boolean[] formats,
            boolean reparse, String ... enders){
        super(reparse, style, enders);
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
