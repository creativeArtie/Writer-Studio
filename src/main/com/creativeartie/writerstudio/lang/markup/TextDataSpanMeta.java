package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/**  A {@link TextDataSpan} for meta data in the PDF properties. */
public class TextDataSpanMeta extends TextDataSpan<ContentSpan>{

    /** Creates a {@link TextDataSpanMeta}.
     *
     * @param children
     *      span children
     * @see TextDataParser#parse(SetupPointer)
     */
    TextDataSpanMeta(List<Span> children){
        super(children);
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

    /** Edit the text
     *
     * @param text
     *      the text to edit
     */
    public void editText(String text){
        argumentNotNull(text, "text");
        runCommand(() -> getType().getKeyName() + getFormat().getKeyName() +
            escapeText(text) + LINED_END);
    }

    /** Escapes the text for this span.
     *
     * @param text
     *      text to escape
     * @see #editText(String)
     * @see WritingData#setMetaText(TextDataType.Meta)
     */
    static String escapeText(String text){
        text = CharMatcher.whitespace().trimAndCollapseFrom(text, ' ');
        return CharMatcher.is(CHAR_ESCAPE).replaceFrom(text,
            TOKEN_ESCAPE + CHAR_ESCAPE);
    }
}