package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Draft the current status of a section with a heading or an outline. */
public final class EditionSpan extends SpanBranch{

    private final CacheKeyMain<EditionType> cacheEdition;
    private final CacheKeyMain<String> cacheDetail;
    private final CacheKeyList<StyleInfo> cacheBranchStyles;

    /** Creates a {@linkplain EditionSpan}.
     *
     * @param children
     *      span children
     * @see ContentParser#buildSpan(List)
     */
    EditionSpan(List<Span> children){
        super(children);
        cacheEdition = new CacheKeyMain<>(EditionType.class);
        cacheDetail = CacheKeyMain.stringKey();
        cacheBranchStyles = new CacheKeyList<>(StyleInfo.class);
    }

    /** Get the edition describing the section.
     *
     * @return answer
     */
    public EditionType getEditionType(){
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

    /** Get more detail of the edition.
     *
     * @return answer
     */
    public String getDetail(){
        return getLocalCache(cacheDetail, () -> spanAtLast(ContentSpan.class)
            .map(span -> span.getTrimmed())
            .orElse("")
        );
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheBranchStyles, () ->
            (List<StyleInfo>) ImmutableList.of((StyleInfo) getEditionType()));
    }

    @Override
    protected SetupParser getParser(String text){
        return text.startsWith(EDITION_BEGIN) && AuxiliaryChecker.notCutoff(text,
            LINED_END)? EditionParser.PARSER: null;
    }
    @Override
    public String toString(){
        return "ed(" + getEditionType() + "{" + spanAtLast(ContentSpan.class) +
            "})";
    }
}
