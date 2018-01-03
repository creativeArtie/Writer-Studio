package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link ContentSpan}.
 */
final class ContentParser extends BasicParseText{

    public ContentParser(StyleInfoLeaf style, String ... enders){
        super(style, enders);
    }

    @Override
    protected ContentSpan buildSpan(List<Span> children){
        checkNotNull(children, "children");
        return new ContentSpan(children);
    }

}
