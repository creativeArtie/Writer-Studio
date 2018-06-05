package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** A single line in the manuscript. */
public abstract class LinedSpan extends SpanBranch {

    private final CacheKeyMain<LinedType> cacheType;

    /** Creates a {@linkplain LinedSpan}.
     *
     * @param children
     *      span children
     */
    LinedSpan(List<Span> children){
        super(children);

        cacheType = new CacheKeyMain<>(LinedType.class);
    }

    /** Gets the line type.
     *
     * @return answer
     */
    public LinedType getLinedType(){
        return getLocalCache(cacheType, () -> LinedType.findType(get(0)
            .getRaw()));
    }

    /** Gets the publishing manuscript word count.
     *
     * This excludes footnote, endnotes, citations, and other matters.
     *
     * @return answer
     */
    public int getPublishTotal(){
        return 0;
    }

    /** Gets the research notes word count.
     *
     * This includes footnotes, endnotes and citations.
     *
     * @return answer
     */
    public int getNoteTotal(){
        return 0;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(getLinedType());
    }

    @Override
    public String toString(){
        return getLinedType() + super.toString() + "\n";
    }
}
