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
                if (raw.startsWith(type.getKeyName())){
                    return type;
                }
            }
            throw new IllegalStateException("Data type not found.");
        });
        return cacheType.get();
    }

    protected abstract TextDataType.Type[] listTypes();

    public abstract TextDataType.Format getFormat();

    public void editText(String text){
        runCommand(() -> getType().getKeyName() + getFormat().getKeyName() +
            replaceText(text) + "\n");
    }

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


    @Override
    protected void childEdited(){
        cacheData = Optional.empty();
        cacheType = Optional.empty();
    }

    @Override
    protected void docEdited(){}

}