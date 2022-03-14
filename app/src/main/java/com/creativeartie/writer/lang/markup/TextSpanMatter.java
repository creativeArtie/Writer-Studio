package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** A {@link TextSpan} for meta data in the document area. */
public final class TextSpanMatter extends TextSpan<FormattedSpan>{
    private final CacheKeyMain<TextDataType> cacheFormat;
    private final CacheKeyMain<Integer> cacheIndex;

    /** Creates a {@link TextSpanMatter}.
     *
     * @param children
     *      span children
     * @see TextParser#parse(SetupPointer)
     */
    TextSpanMatter(List<Span> children){
        super(children);
        cacheFormat = new CacheKeyMain<>(TextDataType.class);
        cacheIndex = CacheKeyMain.integerKey();
    }

    /** Gets the line index.
     *
     * @return answer
     */
    public int getIndex(){
        return getLocalCache(cacheIndex, () -> ((WritingData)getParent())
            .getMatter((TextTypeMatter)getRowType()).indexOf(this));
    }

    @Override
    protected Class<FormattedSpan> getDataClass(){
        return FormattedSpan.class;
    }

    @Override
    protected TextType[] listTypes(){
        return TextTypeMatter.values();
    }

    @Override
    public TextDataType getDataType(){
        return getLocalCache(cacheFormat, () -> leafFromFirst(SpanLeafStyle.DATA)
            .map( s -> TextDataType.getDataType(s.getRaw().trim()) )
            .orElse(TextDataType.TEXT)
        );
    }

    /** Set the text format.
     *
     * @param format
     *      setting format
     */
    public void setFormat(TextDataType format){
        argumentNotNull(format, "format");
        runCommand(() -> getRowType().getKeyName() + TEXT_SEPARATOR +
            format.getKeyName() + TEXT_SEPARATOR +
            getData().map(s -> s.getRaw()).orElse("") + LINED_END);
    }

    /** Delete this line. */
    void deleteLine(){
        runCommand(() -> null);
    }

    /** Change the text line.
     *
     * @param raw
     *      new raw text
     * @throws TextMatterException
     *      from {@link checkText(String)
     */
    void setData(String raw) {
        String text =  getRowType().getKeyName() + TEXT_SEPARATOR +
            getDataType().getKeyName() + TEXT_SEPARATOR +
            fixText(raw) + LINED_END;
        runCommand(() -> text);
    }

    /** Escapes the text for this span.
     *
     * @param text
     *      text to escape
     * @see #editText(String)
     * @see WritingData#setMetaText(TextTypeInfo)
     * @throws TextMatterException
     *      text ends with "{@value TOKEN_ESCAPE}"
     */
    static String fixText(String text){
        text = text.replace(TOKEN_ESCAPE + LINED_END, LINED_END);
        return text.endsWith(TOKEN_ESCAPE)? text.substring(0, text.length() -
            TOKEN_ESCAPE.length()): text;
    }
}
