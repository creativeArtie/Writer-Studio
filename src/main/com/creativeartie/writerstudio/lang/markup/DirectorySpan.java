package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // ArrayList, List, Optional

import com.google.common.collect.*; // ImmutableList

import com.creativeartie.writerstudio.lang.*; // (many)

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Grouping of text {@link Span} that creates a {@link CatalogueIdentity}.
 * Represented in design/ebnf.txt as {@code Directory}.
 */
public final class DirectorySpan extends SpanBranch {
    /// helps with categorizing and describes purpose
    private final DirectoryType idPurpose;
    private Optional<CatalogueIdentity> cacheId;
    private Optional<String> cacheText;
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

    /** Get the display for {@link FormatSpanPointId#getOutput()}*/
    public String getLookupText(){
        cacheText = getCache(cacheText, () -> {
            StringBuilder builder = new StringBuilder();
            this.forEach((span) -> {
                builder.append(span.getRaw());
            });
            return builder.toString();
        });
        return cacheText.get();
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
        cacheText = Optional.empty();
    }

    @Override
    protected void docEdited(){}
}
