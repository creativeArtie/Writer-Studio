package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Parser for {@link ContentSpan}.
 */
final class ContentParser extends BasicParseText{

    private boolean willReparse;

    public ContentParser(List<String> enders){
        this(true, StyleInfoLeaf.TEXT, enders);
    }

    public ContentParser(boolean reparse, List<String> enders){
        this(reparse, StyleInfoLeaf.TEXT, enders);
    }

    public ContentParser(StyleInfoLeaf style, List<String> enders){
        this(true, style, enders);
    }

    public ContentParser(boolean reparse, StyleInfoLeaf style,
            List<String> enders){
        this(reparse, style, checkNotNull(enders, "enders")
            .toArray(new String[0]));
    }

    public ContentParser(String ... enders){
        this(true, enders);
    }

    public ContentParser(boolean reparse, String ... enders){
        this (reparse, StyleInfoLeaf.TEXT, enders);
    }

    public ContentParser(StyleInfoLeaf style, String ... enders){
        this (true, style, enders);
    }

    public ContentParser(boolean reparse, StyleInfoLeaf style,
            String ... enders){
        super(reparse, style, enders);
    }

    @Override
    protected ContentSpan buildSpan(List<Span> children){
        checkNotNull(children, "children");
        return new ContentSpan(children, this);
    }

}
