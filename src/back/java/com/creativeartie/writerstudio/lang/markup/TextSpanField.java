package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** A single data about the day. */
public final class TextSpanField extends SpecSpanField<TextTypeKey> {


    private CacheKeyMain<TextTypeKey> cacheKey;

    StatSpanField(ArrayList<Span> children){
        super(children);
        cacheKey = new CacheKeyMain<>(TextTypeKey.class);
    }


    public TextTypeKey getKey(){
        return getLocalCache(cacheKey, () -> getKey(TextTypeKey.values()));
    }

}
