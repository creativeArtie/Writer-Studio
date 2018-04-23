package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.collect.*;
import com.google.common.base.*;

/** A line of text in the {@link WritingData}. Represented in design/ebnf.txt as
 * {@code DataSpan}.
 */
public abstract class TextDataSpan<T extends SpanBranch> extends SpanBranch{

    private final CacheKeyOptional<T> cacheData;
    private final CacheKeyMain<TextDataType.Type> cacheType;

    public TextDataSpan(List<Span> children){
        super(children);

        cacheData = new CacheKeyOptional<>(getDataClass());
        cacheType = new CacheKeyMain<>(TextDataType.Type.class);
    }

    public Optional<T> getData(){
        return getLocalCache(cacheData, () -> spanFromLast(getDataClass())
            .orElse(null));
    }

    protected abstract Class<T> getDataClass();

    public TextDataType.Type getType(){
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

    protected abstract TextDataType.Type[] listTypes();

    public abstract TextDataType.Format getFormat();

    protected String replaceText(String text){
        return text;
    }

    @Override
    protected SetupParser getParser(String text){
        return AuxiliaryChecker.checkLineEnd(text, isLast())?
            TextDataParser.PARSER: null;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

}