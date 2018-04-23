package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Base class of {@code LinedSpanPoint*} classes.
 */
public abstract class LinedSpanPoint extends LinedSpan implements Catalogued{

    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyMain<String> cacheLookup;

    LinedSpanPoint(List<Span> children){
        super(children);

        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheLookup = CacheKey.stringKey();
    }

    public abstract DirectoryType getDirectoryType();

    @Override
    public List<StyleInfo> getBranchStyles(){
         return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> builder = ImmutableList.builder();
            return builder.addAll(super.getBranchStyles()).add(getIdStatus())
                .build();
        });
    }

    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(span -> getLookupStart()  + span.getLookupText() +
                    getLookupEnd())
                .orElse("")
        );
    }

    protected abstract String getLookupStart();

    protected abstract String getLookupEnd();

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getLocalCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class).map(span -> span.buildId())
        );
    }

    @Override
    public boolean isId(){
        return true;
    }
}
