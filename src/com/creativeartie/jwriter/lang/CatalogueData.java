package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;

import com.creativeartie.jwriter.main.*;

public final class CatalogueData{
    private final ArrayList<SpanBranch> idSpans;
    private final ArrayList<SpanBranch> refSpans;
    private final CatalogueMap catalogueParent;
    private final CatalogueIdentity catelogueKey;

    public CatalogueData(CatalogueMap parent, CatalogueIdentity id){
        catalogueParent = Preconditions.checkNotNull(parent);
        catelogueKey = Preconditions.checkNotNull(id);
        idSpans = new ArrayList<>();
        refSpans = new ArrayList<>();
    }

    void addId(SpanBranch span){
        idSpans.add(Preconditions.checkNotNull(span));
    }

    void addRef(SpanBranch span){
        refSpans.add(Preconditions.checkNotNull(span));
    }

    public CatalogueMap getParent(){
        return catalogueParent;
    }

    public CatalogueIdentity getKey(){
        return catelogueKey;
    }

    public CatalogueStatus getState(){
        if (idSpans.size() > 1){
            return CatalogueStatus.MULTIPLE;
        } else if (idSpans.isEmpty()){
            assert !refSpans.isEmpty();
            return CatalogueStatus.NOT_FOUND;
        } else if (refSpans.isEmpty()){
            return CatalogueStatus.UNUSED;
        }
        return CatalogueStatus.READY;
    }

    public boolean isReady(){
        return getState() == CatalogueStatus.READY;
    }

    public SpanBranch getTarget(){
        Preconditions.checkState(idSpans.size() == 1,
            "There isn't exactly one target (wrong state: %s).", getState());
        return idSpans.get(0);
    }

    public ImmutableList<SpanBranch> getIds(){
        return ImmutableList.copyOf(idSpans);
    }

    public ImmutableList<SpanBranch> getRefs(){
        return ImmutableList.copyOf(refSpans);
    }

    @Override
    public String toString(){
        return catelogueKey.toString() + ": " + 
            getState().toString() + "\n\tIds{\n\t" +
            idSpans.toString().replace("\n", "\n\t\t") + "\n\t}Refs{" +
            refSpans.toString() + "}\n";
    }
}
