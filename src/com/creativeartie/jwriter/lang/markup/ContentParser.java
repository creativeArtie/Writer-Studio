package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link ContentSpan}.
 */
class ContentParser extends BasicParseText{

    public ContentParser(List<String> enders){
        super(StyleInfoLeaf.TEXT, enders);
    }

    public ContentParser(StyleInfoLeaf style, List<String> enders){
        super(style, enders);
    }

    public ContentParser(StyleInfoLeaf spanStyle, String ... spanEnders){
        super(spanStyle, spanEnders);
    }

    public ContentParser(String ... spanEnders){
        super(StyleInfoLeaf.TEXT, spanEnders);
    }

    @Override
    protected ContentSpan buildSpan(List<Span> children){
        checkNotNull(children, "children");
        return new ContentSpan(children, this);
    }

}
