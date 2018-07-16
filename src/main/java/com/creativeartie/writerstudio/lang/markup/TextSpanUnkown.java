package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

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
