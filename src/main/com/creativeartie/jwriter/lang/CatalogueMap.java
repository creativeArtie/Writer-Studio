package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * A map of {@link CatalogueData} that is serachable by
 * {@link CatalogueIdentity}.
 */
public final class CatalogueMap extends ForwardingSortedMap<CatalogueIdentity,
        CatalogueData>{
    private final TreeMap<CatalogueIdentity, CatalogueData> idMap;

    /** {@linkplain CatalogueMap}'s constructor.*/
    CatalogueMap(){
        idMap = new TreeMap<>();
    }

    void addId(CatalogueIdentity id, SpanBranch span){
        checkNotNull(id, "id");
        checkNotNull(span, "span");

        CatalogueData data = idMap.get(id);
        if (data == null){
            data = new CatalogueData(this, id);
            idMap.put(id, data);
        }
        data.addId(span);
    }

    void addRef(CatalogueIdentity ref, SpanBranch span){
        checkNotNull(ref, "ref");
        checkNotNull(span, "span");

        CatalogueData data = idMap.get(ref);
        if (data == null){
            data = new CatalogueData(this, ref);
            idMap.put(ref, data);
        }
        data.addRef(span);
    }

    @Override
    public SortedMap<CatalogueIdentity, CatalogueData> delegate(){
        return ImmutableSortedMap.copyOf(idMap);
    }

    public SortedMap<CatalogueIdentity, CatalogueData> getCategory(
            String ... category){
        checkNotEmpty(category, "category");

        SortedMap<CatalogueIdentity, CatalogueData> map = delegate();
        CatalogueIdentity first = new CatalogueIdentity(
            ImmutableList.copyOf(category), "");

        category[category.length - 1] = category[category.length - 1] + (char)0;
        CatalogueIdentity last = new CatalogueIdentity(
            ImmutableList.copyOf(category), "");
        return map.subMap(first, last);
    }
}
