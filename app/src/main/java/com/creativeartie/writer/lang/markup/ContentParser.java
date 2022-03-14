package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt Content}. */
final class ContentParser extends BasicParseText{

    /** Creates a {@linkplain ContentParser}.
     *
     * @param style
     *      text leaves style
     * @param enders
     *      span ending tokens
     * @see AuxiliaryData constants starts with "CONTENT"
     * @see DirectoryParser#DirectoryParser(DirectoryType, String)
     */
    ContentParser(SpanLeafStyle style, String ... enders){
        super(style, enders);
    }

    @Override
    protected ContentSpan buildSpan(List<Span> children){
        argumentNotNull(children, "children");
        return new ContentSpan(children);
    }

}
