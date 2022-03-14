package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;

/** Draft the current status of a section with a heading or an outline. */
public final class EditionSpan extends SpanBranch{

    private final CacheKeyMain<EditionType> cacheEdition;
    private final CacheKeyMain<String> cacheDetail;

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
