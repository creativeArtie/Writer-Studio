package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.Optional;

import com.google.common.collect.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Grouping of text {@link Span} that creates a {@link CatalogueIdentity}. */
public final class DirectorySpan extends SpanBranch {

    private final DirectoryType idPurpose;
    private final CacheKeyMain<CatalogueIdentity> cacheId;
    private final CacheKeyMain<String> cacheText;
    private final DirectoryParser spanReparser;

    /** Creates a {@linkplain ContentSpan}.
     *
     * @param children
     *      span children
     * @see ContentParser#buildSpan(List)
     */
    DirectorySpan(List<Span> spanChildren, DirectoryType purpose,
            DirectoryParser reparser){
        super(spanChildren);
        idPurpose = argumentNotNull(purpose, "purpose");
        spanReparser = argumentNotNull(reparser, "reparser");

        cacheId = new CacheKeyMain<>(CatalogueIdentity.class);
        cacheText = CacheKeyMain.stringKey();
    }

    /** Creates the id for a {@link Catalogued}
     *
     * @return answer
     */
    public CatalogueIdentity buildId(){
        return getDocCache(cacheId, () -> {
            ArrayList<String> builder = new ArrayList<>();
            builder.add(idPurpose.getCategory());

            /// idTmp is tmp because the text maybe a category
            Optional<String> idTmp = Optional.empty();
            for(Span child: this){
                if (child instanceof SpanLeaf){
                    /// child == DIRECTORY_CATEGORY:
                    builder.add(idTmp.orElse(""));
                    idTmp = Optional.empty();
                } else /** if (child == ContentSpan) */{
                    /// child is a text
                    idTmp = Optional.of(((ContentSpan)child).getTrimmed()
                        .toLowerCase());
                }
            }
            return new CatalogueIdentity(builder, idTmp.orElse(""));
        });
    }

    /** Get the reference help text.
     *
     * @return answer
     */
    public String getLookupText(){
        return getLocalCache(cacheText, () -> {
            StringBuilder builder = new StringBuilder();
            for(Span span: this){
                /// apply trim on each id part
                String out = span.getRaw().toLowerCase();
                out = CharMatcher.whitespace().trimAndCollapseFrom(out, ' ');
                builder.append(out);
            }
            return builder.toString();
        });
    }

    /** Get the id purpose.
     *
     * @return answer
     */
    public DirectoryType getPurposeType(){
        return idPurpose;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    public String toString(){
        CatalogueIdentity id = buildId();
        StringBuilder output = new StringBuilder();
        for (String cat: id.getCategories()){
            if (output.length() != 0){
                output.append("-");
            }
            output.append(SpanLeaf.escapeText(cat));
        }
        if (output.length() != 0){
            output.append(":");
        }
        output.append(SpanLeaf.escapeText(id.getName()));
        return "id ->" + output.toString() + "";
    }

}
