package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.main.*;

/**
 * Created from {@link DirectorySpan}. Used to store {@link CatalogueIdentity}
 */
public class DirectorySpan extends SpanBranch {
    /// helps with categorizing and describes purpose
    private final Optional<DirectoryType> purpose;

    DirectorySpan(List<Span> spanChildren, Optional<DirectoryType> idPurpose){
        super(spanChildren);
        Checker.checkNotNull(idPurpose, "idPurpose");
        purpose = idPurpose;
    }

    CatalogueIdentity buildId(){
        ArrayList<String> builder = new ArrayList<>();
        purpose.ifPresent(found -> builder.add(found.getCategory()));
        Optional<String> idTmp = Optional.empty();
        for(Span child: this){
            if (child instanceof SpanLeaf){
                builder.add(idTmp.orElse(""));
                idTmp = Optional.empty();
            } else {
                idTmp = Optional.of(((ContentSpan)child).getTrimmed()
                    .toLowerCase());
            }
        }
        return new CatalogueIdentity(builder, idTmp.orElse(""));
    }

    public String getIdRaw(){
        StringBuilder builder = new StringBuilder();
        this.forEach((span) -> {
            builder.append(span.getRaw());
        });
        return builder.toString();
    }

    public DirectoryType getPurpose(){
        //TODO
        return purpose.isPresent()? purpose.get(): null;
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of();
    }

    StyleInfo getStatusState(){
        CatalogueData data = getDocument().getCatalogue().get(buildId());
        if (data == null) {
            return CatalogueStatus.NO_ID;
        }
        return data.getState();
    }

    @Override
    public String toString(){
        return "ID" + buildId().toString();
    }

    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}
