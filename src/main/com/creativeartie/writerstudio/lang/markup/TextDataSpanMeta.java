package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.collect.*;
import com.google.common.base.*;

/**
 * A {@link TextDataSpan} for meta data in the PDF properties.
 */
public class TextDataSpanMeta extends TextDataSpan<ContentSpan>{

    public TextDataSpanMeta(List<Span> spans){
        super(spans);
    }

    @Override
    protected Class<ContentSpan> getDataClass(){
        return ContentSpan.class;
    }

    @Override
    public TextDataType.Type[] listTypes(){
        return TextDataType.Meta.values();
    }

    @Override
    public TextDataType.Format getFormat(){
        return TextDataType.Format.TEXT;
    }
}