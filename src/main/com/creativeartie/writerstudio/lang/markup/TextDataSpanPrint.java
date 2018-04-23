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
    private final CacheKeyMain<TextDataType.Format> cacheFormat;
    private final CacheKeyMain<Integer> cacheIndex;

    public TextDataSpanPrint(List<Span> spans){
        super(spans);
        cacheFormat = new CacheKeyMain<>(TextDataType.Format.class);
        cacheIndex = CacheKey.integerKey();
    }

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

    public void setFormat(TextDataType.Format format){
        runCommand(() -> getType().getKeyName() + format.getKeyName() +
            getData().map(s -> s.getRaw()).orElse("") + LINED_END);
    }

    void deleteLine(){
        runCommand(() -> null);
    }

    void setData(String raw){
        runCommand(() -> getType().getKeyName() + getFormat().getKeyName() +
            raw + LINED_END);
    }
}