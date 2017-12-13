package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Created from {@link DirectorySpan}. Used to store {@link CatalogueIdentity}
 */
public class DirectorySpan extends SpanBranch {
    /// helps with categorizing and describes purpose
    private final DirectoryType idPurpose;
    private Optional<CatalogueIdentity> cacheId;
    private Optional<String> cacheRaw;
    private final DirectoryParser spanReparser;

    DirectorySpan(List<Span> spanChildren, DirectoryType purpose,
            DirectoryParser reparser){
        super(spanChildren);
        idPurpose = checkNotNull(purpose, "purpose");
        spanReparser = checkNotNull(reparser, "reparser");
        clearCache();
    }

    /** Creates the id for a {@link Catalogued}*/
    CatalogueIdentity buildId(){
        cacheId = getCache(cacheId, () -> {
            ArrayList<String> builder = new ArrayList<>();
            builder.add(idPurpose.getCategory());

            // idTmp is tmp because the text maybe a category
            Optional<String> idTmp = Optional.empty();
            for(Span child: this){
                if (child instanceof SpanLeaf){
                    /// child == DIRECTORY_CATEGORY:
                    builder.add(idTmp.orElse(""));
                    idTmp = Optional.empty();
                } else {
                    /// child is a text
                    idTmp = Optional.of(((ContentSpan)child).getTrimmed()
                        .toLowerCase());
                }
            }
            return new CatalogueIdentity(builder, idTmp.orElse(""));
        });
        return cacheId.get();
    }

    public String getIdRaw(){
        cacheRaw = getCache(cacheRaw, () -> {
            StringBuilder builder = new StringBuilder();
            this.forEach((span) -> {
                builder.append(span.getRaw());
            });
            return builder.toString();
        });
        return cacheRaw.get();
    }

    public DirectoryType getPurpose(){
        return idPurpose;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    @Override
    public String toString(){
        return "ID" + buildId().toString();
    }

    @Override
    protected SetupParser getParser(String text){
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected void childEdited(){
        clearCache();
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }


    /**
     * Set all cache to empty. Helper method of
     * {@link #ContentSpan(List, ContentParser)} and {@link #childEdited()}.
     */
    private void clearCache(){
        cacheId = Optional.empty();
        cacheRaw = Optional.empty();
    }
}
