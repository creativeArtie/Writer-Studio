package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.*;

/**
 * Creates a text span upto a certain character.
 */
class FormatParseContent extends BasicParseText{

    private boolean[] formatList;
    private boolean willReparse;

    public FormatParseContent(StyleInfoLeaf style, boolean[] formats,
        boolean reparse, List<String> enders
    ){
        this(style, formats, reparse,
            Checker.checkNotNull(enders, "enders")
            .toArray(new String[0]));
    }

    public FormatParseContent(StyleInfoLeaf style, boolean[] formats,
            boolean reparse, String ... enders){
        super(style, enders);
        Checker.checkArraySize(formats, "formats", FORMAT_TYPES);
        formatList = Arrays.copyOf(formats, formats.length);
        willReparse = reparse; /// no limitations
    }

    @Override
    protected FormatSpanContent buildSpan(List<Span> children){
        return new FormatSpanContent(children, formatList, this, willReparse);
    }

}
