package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List

import com.google.common.base.*; // CharMatcher

import com.creativeartie.writerstudio.lang.*; // List
import static com.creativeartie.writerstudio.main.Checker.*;

/**  A {@link TextDataSpan} for meta data in the PDF properties.
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

    @Override
    public String replaceText(String text){
        return CharMatcher.whitespace().trimAndCollapseFrom(text, ' ');
    }
}