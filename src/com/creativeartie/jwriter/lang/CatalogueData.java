package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;

import static com.google.common.base.Preconditions.*;

/**
 * Store a list of {@link SpanBranch} with the same {@link CatalogueIdentity}.
 */
public final class CatalogueData{
    private final ArrayList<SpanBranch> idSpans;
    private final ArrayList<SpanBranch> refSpans;
    private final CatalogueMap catalogueParent;
    private final CatalogueIdentity catelogueKey;

    CatalogueData(CatalogueMap parent, CatalogueIdentity id){
        catalogueParent = checkNotNull(parent);
        catelogueKey = checkNotNull(id);
        idSpans = new ArrayList<>();
        refSpans = new ArrayList<>();
    }

    void addId(SpanBranch span){
        checkNotNull(span, "Span can be null.");
        checkArgument(span instanceof Catalogued,
            "Span is not of type Catalogued.");
        checkArgument(((Catalogued)span).isId(),
            "Span is no an id: " + span.getRaw());

        idSpans.add(span);
    }

    void addRef(SpanBranch span){
        checkNotNull(span, "Span can be null.");
        checkArgument(span instanceof Catalogued,
            "Span is not of type Catalogued.");
        checkArgument(((Catalogued)span).isRef(),
            "Span is no an ref: " + span.getRaw());

        refSpans.add(span);
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
        checkState(idSpans.size() != 1,
            "CatalougeData is not ready: %s.", getState());
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
