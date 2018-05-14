package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** A line that are cataloged. */
public abstract class LinedSpanPoint extends LinedSpan implements Catalogued{

    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyMain<String> cacheLookup;

    /** Creates a {@linkplain LinedSpanParagraph}.
     *
     * @param children
     *      span children
     */
    LinedSpanPoint(List<Span> children){
        super(children);

        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheLookup = CacheKeyMain.stringKey();
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

    /** Gets the user reference help text.
     *
     * @return answer
     */
    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(DirectorySpan.class)
                .map(s -> getLookupStart()  + s.getLookupText() +
                    getLookupEnd())
                .orElse("")
        );
    }

    /** Get starting text for reference help text
     *
     * @return answer
     * @see #getLookupText()
     */
    protected abstract String getLookupStart();

    /** Get ending text for reference help text
     *
     * @return answer
     * @see #getLookupText()
     */
    protected abstract String getLookupEnd();

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getLocalCache(cacheId, () ->
            spanFromFirst(DirectorySpan.class).map(s -> s.buildId())
        );
    }

    @Override
    public boolean isId(){
        return true;
    }
}
