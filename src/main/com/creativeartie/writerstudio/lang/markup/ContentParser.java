package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List

import com.creativeartie.writerstudio.lang.*; // StyleInfoLeaf

import static com.creativeartie.writerstudio.main.Checker.*;

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
