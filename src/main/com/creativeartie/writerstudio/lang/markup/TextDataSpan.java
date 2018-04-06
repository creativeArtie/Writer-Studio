package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.collect.*;
import com.google.common.base.*;

/**
 * A single line in {@link WritingData}.
 */
public abstract class TextDataSpan<T extends SpanBranch> extends SpanBranch{

    private Optional<Optional<T>> cacheData;
    private Optional<TextDataType.Type> cacheType;

    public TextDataSpan(List<Span> children){
        super(children);
    }

    public Optional<T> getData(){
        cacheData = getCache(cacheData, () -> spanFromLast(getDataClass()));
        return cacheData.get();
    }

    protected abstract Class<T> getDataClass();

    public TextDataType.Type getType(){
        cacheType = getCache(cacheType, () -> {
            String raw = getRaw();
            for (TextDataType.Type type: listTypes()){
                if (type.getKeyName().equals(raw)){
                    return type;
                }
            }
            throw new IllegalStateException("Data type not found.");
        });
        return cacheType.get();
    }

    protected abstract TextDataType.Type[] listTypes();

    public abstract TextDataType.Format getFormat();


    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }


    @Override
    protected void childEdited(){
        cacheData = Optional.empty();
        cacheType = Optional.empty();
    }

    @Override
    protected void docEdited(){}

}