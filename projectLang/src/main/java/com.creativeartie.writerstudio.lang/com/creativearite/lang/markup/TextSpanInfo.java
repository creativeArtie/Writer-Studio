package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/**  A {@link TextSpan} for meta data in the PDF properties. */
public final class TextSpanInfo extends TextSpan<ContentSpan>{

    /** Creates a {@link TextSpanInfo}.
     *
     * @param children
     *      span children
     * @see TextParser#parse(SetupPointer)
     */
    TextSpanInfo(List<Span> children){
        super(children);
    }

    @Override
    protected Class<ContentSpan> getDataClass(){
        return ContentSpan.class;
    }

    @Override
    protected TextType[] listTypes(){
        return TextTypeInfo.values();
    }

    @Override
    public TextDataType getDataType(){
        return TextDataType.TEXT;
    }

    /** Edit the text
     *
     * @param text
     *      the text to edit
     */
    public void editText(String text){
        argumentNotNull(text, "text");
        runCommand(() -> getRowType().getKeyName() + TEXT_SEPARATOR +
            getDataType().getKeyName() + TEXT_SEPARATOR +
            escapeText(text) + LINED_END);
    }

    /** Escapes the text for this span.
     *
     * @param text
     *      text to escape
     * @see #editText(String)
     * @see WritingData#setMetaText(TextTypeInfo)
     */
    static String escapeText(String text){
        text = CharMatcher.whitespace().trimAndCollapseFrom(text, ' ');
        return CharMatcher.is(CHAR_ESCAPE).replaceFrom(text,
            TOKEN_ESCAPE + CHAR_ESCAPE);
    }
}
