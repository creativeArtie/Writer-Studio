package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** A formatted reference text, mainly to docuement statistics. */
public final class FormatSpanPointKey extends FormatSpan{
    private final FormatParsePointKey spanReparser;
    private final CacheKeyMain<FormatTypeField> cacheField;
    private final CacheKeyMain<String> cacheValue;

    /** Creates a {@linkplain FormatSpanLinkRef}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     * @param reparser
     *      span reparser
     * @see FormatParseLinkRef#parseSpan(SetupPointer, List)
     */
    FormatSpanPointKey(List<Span> spanChildren, boolean[] formats,
            FormatParsePointKey reparser){
        super(spanChildren, formats);
        spanReparser = argumentNotNull(reparser, "reparser");

        cacheField = new CacheKeyMain<>(FormatTypeField.class);
        cacheValue = CacheKeyMain.stringKey();
    }

    public FormatTypeField getField(){
        return getLocalCache(cacheField, () -> spanFromFirst(ContentSpan.class)
            /// s == ContentSpan
            .map(s -> s.getTrimmed())
            /// s == String
            .map(s -> FormatTypeField.findField(s))
            .orElse(FormatTypeField.ERROR));
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected String toChildName(){
        return "ref-key";
    }

    @Override
    protected String toChildString(){
        return getField().getFieldKey();
    }
}
