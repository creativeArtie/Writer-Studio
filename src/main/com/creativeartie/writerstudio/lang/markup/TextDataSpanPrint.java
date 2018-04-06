package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

import com.google.common.collect.*;
import com.google.common.base.*;

/**
 * A {@link TextDataSpan} for meta data in the document area.
 */
public class TextDataSpanPrint extends TextDataSpan<FormattedSpan>{
    private Optional<TextDataType.Format> cacheFormat;
    public TextDataSpanPrint(List<Span> spans){
        super(spans);
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
            for (TextDataType.Format type: TextDataType.Format.values()){
                if (type.getKeyName().equals(format)){
                    return type;
                }
            }
            throw new IllegalStateException("Text data format not found.");
        });
        return cacheFormat.get();
    }
}