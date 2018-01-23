package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;

/** A list of {@link SpanBranch} with the same {@link CatalogueIdentity}. */
public final class CatalogueData{
    private final TreeSet<SpanBranch> idSpans;
    private final TreeSet<SpanBranch> refSpans;
    private final CatalogueMap catalogueParent;
    private final CatalogueIdentity catelogueKey;

    private static int extractInt(SpanBranch span){
        return span.getStart();
    }

    /** {@linkplain CatalogueData}'s constructor.*/
    CatalogueData(CatalogueMap parent, CatalogueIdentity id){
        catalogueParent = checkNotNull(parent, "parent");
        catelogueKey = checkNotNull(id, "id");
        idSpans = new TreeSet<>(Comparator.comparingInt(CatalogueData::extractInt));
        refSpans = new TreeSet<>(Comparator.comparingInt(CatalogueData::extractInt));
    }

    void add(Catalogued span){
        checkArgument(span instanceof SpanBranch,
            "Parameter \"span\" is not of type SpanBranch.");
        /// TODO throw exception with repeat (there are repeated added)

        (span.isId()? idSpans:refSpans).add((SpanBranch)span);
    }

    boolean remove(Catalogued span){
        checkArgument(span instanceof SpanBranch,
            "Parameter \"span\" is not of type SpanBranch.");
        /// TODO throw exception with repeat (there are repeated added)

        (span.isId()? idSpans:refSpans).remove((SpanBranch)span);
        return idSpans.isEmpty() && refSpans.isEmpty();
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

        return idSpans.first();
    }

    public ImmutableList<SpanBranch> getIds(){
        /// TODO to set instead of list
        return ImmutableList.copyOf(idSpans);
    }

    public ImmutableList<SpanBranch> getRefs(){
        /// TODO to set instead of list
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
