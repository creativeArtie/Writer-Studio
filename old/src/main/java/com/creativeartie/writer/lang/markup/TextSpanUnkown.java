package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

/**  A {@link TextSpan} for meta data in the PDF properties. */
public final class TextSpanUnkown extends TextSpan<ContentSpan>{

    public static final TextType TYPE = () -> "";

    /** Create a {@link TextSpan}.
     *
     * @param children
     */
    TextSpanUnkown(List<Span> children){
        super(children);
    }

    @Override
    protected TextType[] listTypes(){
        throw new UnsupportedOperationException("No list types");
    }

    @Override
    public TextDataType getDataType(){
        return TextDataType.TEXT;
    }

    @Override
    protected Class<ContentSpan> getDataClass(){
        return ContentSpan.class;
    }
}
