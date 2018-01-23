package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Grouping of text {@link Span} that creates a {@link CatalogueIdentity}.
 * Represented in design/ebnf.txt as {@code Directory}.
 */
public final class DirectorySpan extends SpanBranch {
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

    /** Get the display for {@link FormatSpanDirectory#getOutput()}*/
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

    /** Get the purpose of this span. */
    public DirectoryType getPurpose(){
        return idPurpose;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
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
        return "ID(" + output.toString() + ")";
    }

    @Override
    protected SetupParser getParser(String text){
        return spanReparser.canParse(text)? spanReparser: null;
    }

    @Override
    protected void childEdited(){
        cacheId = Optional.empty();
        cacheRaw = Optional.empty();
        idChanged();
    }

    @Override
    protected void docEdited(){}
}
