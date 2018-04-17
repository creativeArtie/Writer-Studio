package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.collect.*;
import com.google.common.base.*;

/** A {@link TextDataSpan} for meta data in the document area.
 */
public class TextDataSpanPrint extends TextDataSpan<FormattedSpan>{
    private Optional<TextDataType.Format> cacheFormat;
    private Optional<Integer> cacheIndex;
    public TextDataSpanPrint(List<Span> spans){
        super(spans);
    }

    public int getIndex(){
        cacheIndex = getCache(cacheIndex, () -> ((WritingData)getParent())
            .getPrint((TextDataType.Area)getType()).indexOf(this));
        return cacheIndex.get();
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
        cacheFormat = getCache(cacheFormat, () -> {
            String format = getRaw().substring(ALIGN_START);
            String start = getType().getKeyName();
            for (TextDataType.Format type: TextDataType.Format.values()){
                if (format.startsWith(type.getKeyName())){
                    return type;
                }
            }
            throw new IllegalStateException("Text data format not found.");
        });
        return cacheFormat.get();
    }

    public void setFormat(TextDataType.Format format){
        runCommand(() -> getType().getKeyName() + format.getKeyName() +
            getData().map(s -> s.getRaw()).orElse(""));
    }

    void deleteLine(){
        runCommand(() -> "");
    }

    void setData(String raw){
        runCommand(() -> getType().getKeyName() + getFormat().getKeyName() +
            raw + LINED_END);
    }

    @Override
    protected void childEdited(){
        super.childEdited();
        cacheFormat = Optional.empty();
        cacheIndex = Optional.empty();
    }
}