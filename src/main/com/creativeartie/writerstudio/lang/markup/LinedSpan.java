package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Base class for all {@link LinedSpan}.
 */
public abstract class LinedSpan extends SpanBranch {

    private final CacheKeyMain<LinedType> cacheType;

    LinedSpan(List<Span> children){
        super(children);

        cacheType = new CacheKeyMain<>(LinedType.class);
    }

    public LinedType getLinedType(){
        return getLocalCache(cacheType, () -> LinedType.findType(get(0)
            .getRaw()));
    }

    @Override
    public String toString(){
        return getLinedType() + super.toString() + "\n";
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(getLinedType());
    }

    public int getPublishTotal(){
        return 0;
    }

    public int getNoteTotal(){
        return 0;
    }
}
