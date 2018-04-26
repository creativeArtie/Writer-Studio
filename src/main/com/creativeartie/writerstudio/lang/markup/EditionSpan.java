package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // List, Optional

import com.google.common.collect.*; // ImmutableList;

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * A {@link Span} for stating the current status of a section with a heading or
 * an outline. Represented in design/ebnf.txt as {@code Edition}.
 */
public final class EditionSpan extends SpanBranch{

    private final CacheKeyMain<EditionType> cacheEdition;
    private final CacheKeyMain<String> cacheDetail;
    private final CacheKeyList<StyleInfo> cacheBranchStyles;

    EditionSpan(List<Span> children){
        super(children);
        cacheEdition = new CacheKeyMain<>(EditionType.class);
        cacheDetail = CacheKeyMain.stringKey();
        cacheBranchStyles = new CacheKeyList<>(StyleInfo.class);
    }

    /**
     * Get the span edition that can be used to describe the status of a
     * section.
     */
    public EditionType getEdition(){
        return getLocalCache(cacheEdition, () -> {
            Span first = get(0);
            if (first instanceof SpanLeaf){
                String text = first.getRaw();
                if (text.length() == 1){
                    return EditionType.OTHER;
                }
                return EditionType.valueOf(text.substring(1));
            }
            return EditionType.NONE;
        });
    }

    /** Get more detail of the status.*/
    public String getDetail(){
        return getLocalCache(cacheDetail, () ->
            getDetailSpan().map(span -> span.getTrimmed()).orElse(""));
    }

    public Optional<ContentSpan> getDetailSpan(){
        return spanAtLast(ContentSpan.class);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheBranchStyles, () ->
            (List<StyleInfo>) ImmutableList.of((StyleInfo) getEdition()));
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(EDITION_BEGIN) && AuxiliaryChecker.notCutoff(text,
            LINED_END)? EditionParser.INSTANCE: null;
    }
    @Override
    public String toString(){
        return getEdition() + "(" + getDetailSpan().toString() + ")";
    }
}
