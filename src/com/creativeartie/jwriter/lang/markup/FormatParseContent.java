package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.*;

/**
 * Parser for {@link FormatSpanContent}.
 */
class FormatParseContent extends BasicParseText{

    private boolean[] formatList;

    public FormatParseContent(StyleInfoLeaf style, boolean[] formats,
        boolean reparse, List<String> enders
    ){
        this(style, formats, reparse,
            Checker.checkNotNull(enders, "enders")
            .toArray(new String[0]));
    }

    public FormatParseContent(StyleInfoLeaf style, boolean[] formats,
            boolean reparse, String ... enders){
        super(reparse, style, enders);
        Checker.checkArraySize(formats, "formats", FORMAT_TYPES);
        formatList = Arrays.copyOf(formats, formats.length);
    }

    @Override
    protected FormatSpanContent buildSpan(List<Span> children){
        return new FormatSpanContent(children, formatList, this);
    }

}
