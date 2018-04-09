package com.creativeartie.writerstudio.lang;

import java.util.*; // ArrayList

import com.google.common.collect.*; // ImmutableList

import static com.creativeartie.writerstudio.main.Checker.*;

/** A list of {@link SpanBranch} with the same {@link CatalogueIdentity}. */
public final class CatalogueData{
    private final ArrayList<SpanBranch> idSpans;
    private final ArrayList<SpanBranch> refSpans;
    private final CatalogueMap catalogueParent;
    private final CatalogueIdentity catelogueKey;

    /** {@linkplain CatalogueData}'s constructor.*/
    CatalogueData(CatalogueMap parent, CatalogueIdentity id){
        catalogueParent = checkNotNull(parent, "parent");
        catelogueKey = checkNotNull(id, "id");
        idSpans = new ArrayList<>();
        refSpans = new ArrayList<>();
    }

    void add(Catalogued span){
        checkNotNull(span, "Span");
        checkArgument(span instanceof SpanBranch,
            "Parameter \"span\" is not of type SpanBranch.");

        (span.isId()? idSpans: refSpans).add((SpanBranch)span);
    }

    public CatalogueMap getParent(){
        return catalogueParent;
    }

    public CatalogueIdentity getKey(){
        return catelogueKey;
    }

    /** Get the {@link CatalogueStatus} based on the Span stored. */
    public CatalogueStatus getState(){
        if (idSpans.size() > 1){
            return CatalogueStatus.MULTIPLE;
        } else if (idSpans.isEmpty()){
            assert !refSpans.isEmpty();
            return CatalogueStatus.NOT_FOUND;
        } else if (refSpans.isEmpty()){
            assert !idSpans.isEmpty();
            return CatalogueStatus.UNUSED;
        }
        return CatalogueStatus.READY;
    }

    /** Check if this is ready (that is: {@code idSpan.size() == 1}). */
    public boolean isReady(){
        CatalogueStatus state = getState();
        return state == CatalogueStatus.READY ||
            getState() == CatalogueStatus.UNUSED;
    }

    public SpanBranch getTarget(){
        checkState(idSpans.size() == 1,
            "Id (" + catelogueKey + ") is in the wrong state.");

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
