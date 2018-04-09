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

    private Optional<EditionType> cacheEdition;
    private Optional<String> cacheDetail;
    private Optional<List<StyleInfo>> cacheBranchStyles;

    EditionSpan(List<Span> children){
        super(children);
    }

    /**
     * Get the span edition that can be used to describe the status of a
     * section.
     */
    public EditionType getEdition(){
        cacheEdition = getCache(cacheEdition, () -> {
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
        return cacheEdition.get();
    }

    /** Get more detail of the status.*/
    public String getDetail(){
        cacheDetail = getCache(cacheDetail, () ->
            getDetailSpan().map(span -> span.getTrimmed()).orElse(""));
        return cacheDetail.get();
    }

    public Optional<ContentSpan> getDetailSpan(){
        return spanAtLast(ContentSpan.class);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheBranchStyles = getCache(cacheBranchStyles, () ->
            (List<StyleInfo>) ImmutableList.of((StyleInfo) getEdition()));
        return cacheBranchStyles.get();
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(EDITION_BEGIN) && AuxiliaryChecker.canParse(text,
            LINED_END)? EditionParser.INSTANCE: null;
    }

    @Override
    protected void childEdited(){
        cacheEdition = Optional.empty();
        cacheDetail = Optional.empty();
        cacheBranchStyles = Optional.empty();
    }

    @Override
    protected void docEdited(){}

    @Override
    public String toString(){
        return getEdition() + "(" + getDetailSpan().toString() + ")";
    }
}
