package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;


/** A line of text in the {@link WritingData}. */
public abstract class TextDataSpan<T extends SpanBranch> extends SpanBranch{

    private final CacheKeyOptional<T> cacheData;
    private final CacheKeyMain<TextDataType.Type> cacheType;
    private final CacheKeyList<StyleInfo> cacheStyles;

    /** Create a {@link TextDataSpan}.
     *
     * @param children
     */
    TextDataSpan(List<Span> children){
        super(children);

        cacheData = new CacheKeyOptional<>(getDataClass());
        cacheType = new CacheKeyMain<>(TextDataType.Type.class);
        cacheStyles = new CacheKeyList<>(StyleInfo.class);
    }

    /** Gets the data.
     *
     * @return answer
     */
    public final Optional<T> getData(){
        return getLocalCache(cacheData, () -> spanFromLast(getDataClass()));
    }

    /** Gets the class type of the data.
     *
     * @return answer
     * @see #getData()
     */
    protected abstract Class<T> getDataClass();

    /** Get the type of data.
     *
     * @return answer
     */
    public final TextDataType.Type getType(){
        return getLocalCache(cacheType, () -> {
            String raw = getRaw();
            for (TextDataType.Type type: listTypes()){
                if (raw.startsWith(type.getKeyName())){
                    return type;
                }
            }
            throw new IllegalStateException("Data type not found.");
        });
    }

    /** List the possible types of this class.
     *
     * @return answer
     * @see #getType()
     */
    protected abstract TextDataType.Type[] listTypes();

    /** Get the content format
     *
     * @return answer
     */
    public abstract TextDataType.Format getFormat();

    @Override
    public final List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> ImmutableList.of(
            (StyleInfo) getType(), (StyleInfo) getFormat()));
    }

    @Override
    protected final SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            TextDataParser.PARSER: null;
    }

}