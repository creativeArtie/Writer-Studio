package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A line of text in the {@link WritingData}. */
public abstract class TextSpan<T extends SpanBranch> extends SpanBranch{

    private final CacheKeyOptional<T> cacheData;
    private final CacheKeyMain<TextType> cacheType;

    /** Create a {@link TextSpan}.
     *
     * @param children
     */
    TextSpan(List<Span> children){
        super(children);

        cacheData = new CacheKeyOptional<>(getDataClass());
        cacheType = new CacheKeyMain<>(TextType.class);
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
    public final TextType getRowType(){
        return getLocalCache(cacheType, () -> {
            if (TextSpan.this instanceof TextSpanUnkown){
                return TextSpanUnkown.TYPE;
            }
            String raw = getRaw().trim();
            for (TextType type: listTypes()){
                if (raw.startsWith(type.getKeyName())){
                    return type;
                }
            }
            assert false: "Unreachable code";
            return null;
        });
    }

    /** List the possible types of this class.
     *
     * @return answer
     * @see #getType()
     */
    protected abstract TextType[] listTypes();

    /** Get the content format
     *
     * @return answer
     */
    public abstract TextDataType getDataType();

    @Override
    protected final SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return AuxiliaryChecker.checkLineEnd(text, isDocumentLast())?
            TextParser.PARSER: null;
    }

    @Override
    public final String toString(){
        String type = this instanceof TextSpanUnkown?
            leafFromFirst(SpanLeafStyle.FIELD).map(s -> s.getRaw() + "?")
                .orElse("???"):
            getRowType().toString();
        return type + "(" + getDataType().toString() + "):" +
            getData().toString() + "\n";
    }

}
