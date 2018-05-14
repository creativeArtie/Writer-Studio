package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A {@link TextDataSpan} for meta data in the document area. */
public class TextDataSpanPrint extends TextDataSpan<FormattedSpan>{
    private final CacheKeyMain<TextDataType.Format> cacheFormat;
    private final CacheKeyMain<Integer> cacheIndex;

    /** Creates a {@link TextDataSpanPrint}.
     *
     * @param children
     *      span children
     * @see TextDataParser#parse(SetupPointer)
     */
    public TextDataSpanPrint(List<Span> children){
        super(children);
        cacheFormat = new CacheKeyMain<>(TextDataType.Format.class);
        cacheIndex = CacheKeyMain.integerKey();
    }

    /** Gets the line index.
     *
     * @return answer
     */
    public int getIndex(){
        return getLocalCache(cacheIndex, () -> ((WritingData)getParent())
            .getPrint((TextDataType.Area)getType()).indexOf(this));
    }

    @Override
    protected Class<FormattedSpan> getDataClass(){
        return FormattedSpan.class;
    }

    @Override
    public TextDataType.Type[] listTypes(){
        return TextDataType.Area.values();
    }

    @Override
    public TextDataType.Format getFormat(){
        return getLocalCache(cacheFormat, () -> {
            String format = getRaw().substring(ALIGN_START);
            String start = getType().getKeyName();
            for (TextDataType.Format type: TextDataType.Format.values()){
                if (format.startsWith(type.getKeyName())){
                    return type;
                }
            }
            throw new IllegalStateException("Text data format not found.");
        });
    }

    /** Set the text format.
     *
     * @return answer
     */
    public void setFormat(TextDataType.Format format){
        argumentNotNull(format, "format");
        runCommand(() -> getType().getKeyName() + format.getKeyName() +
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
     * @throws TextAreaLineException
     *      from {@link checkText(String)
     */
    void setData(String raw) throws TextAreaLineException{
        String text =  getType().getKeyName() + getFormat().getKeyName() +
            checkText(raw) + LINED_END;
        runCommand(() -> text);
    }

    /** Escapes the text for this span.
     *
     * @param text
     *      text to escape
     * @see #editText(String)
     * @see WritingData#setMetaText(TextDataType.Meta)
     * @throws TextAreaLineException
     *      text ends with "{@value TOKEN_ESCAPE}"
     */
    static String checkText(String text) throws TextAreaLineException{
        if (text.endsWith(TOKEN_ESCAPE)) throw new TextAreaLineException(text);
        return text;
    }
}