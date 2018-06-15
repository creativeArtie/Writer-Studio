package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A {@link TextSpanMatter} for meta data in the document area. */
public class TextSpanMatter extends SpanBranch{

    private final CacheKeyOptional<T> cacheData;
    private final CacheKeyMain<TextDataType.Type> cacheType;

    /** Create a {@link TextSpanMatter}.
     *
     * @param children
     */
    TextSpanMatter(List<Span> children){
        super(children);

        cacheData = new CacheKeyOptional<>(getDataClass());
        cacheType = new CacheKeyMain<>(TextDataType.Type.class);
    }

    /** Gets the data.
     *
     * @return answer
     */
    public final Optional<FormattedSpan> getData(){
        return getLocalCache(cacheData, () -> spanFromLast(FormattedSpan.class));
    }

    /** Get the type of data.
     *
     * @return answer
     */
    public final TextTypeMatter getType(){
        return getLocalCache(cacheType, () -> {
            String raw = getRaw();
            for (TextTypeMatter type: TextTypeMatter.values()){
                if (raw.startsWith(type.getKeyName())){
                    return type;
                }
            }
            throw new IllegalStateException("Data type not found.");
        });
    }

    public TextMatterAlign getFormat(){
        return getLocalCache(cacheFormat, () -> getAlignSpan()
            .map(s -> TextMatterAlign.valueOf(s.getRaw())
            .orElse(TextMatterAlign.LEFT);
    }

    /** Set the text format.
     *
     * @param format
     *      setting format
     */
    public synchronized void setFormat(TextMatterAlign format){
        argumentNotNull(format, "format");
        Optional<SpecSpanField> field = getAlignSpan();
        if (field.isPresent()){
            field.get().setData(format.name());
        }
        runCommand(() -> getType().getKeyName() + format.name() +
            getData().map(s -> s.getRaw()).orElse("") + LINED_END);
    }

    private Optional<SpecSpanField> getAlignSpan(){
        return getChildren(TextSpanField.class)
            .stream().filter(s -> s.getKey() != TextTypeKey.ALGIN)
            .findFirst();;
    }

    /** Delete this line. */
    synchronized void deleteLine(){
        runCommand(() -> null);
    }

    /** Change the text line.
     *
     * @param raw
     *      new raw text
     * @throws TextAreaLineException
     *      from {@link checkText(String)
     */
    synchronized void setData(String raw) throws TextMatterException{
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
     * @throws TextMatterException
     *      text ends with "{@value TOKEN_ESCAPE}"
     */
    static String checkText(String text) throws TextMatterException{
        if (text.endsWith(TOKEN_ESCAPE)) throw new TextMatterException(text);
        return text;
    }

    @Override
    protected final SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            TextParseMatter.PARSER: null;
    }
}
