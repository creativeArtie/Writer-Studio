package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;
import static com.google.common.base.Preconditions.*;

/**
 * A System of {@link DirectorySpan} references with their status
 */
public final class CatalogueMap extends ForwardingSortedMap<CatalogueIdentity,
        CatalogueData>{
    private final TreeMap<CatalogueIdentity, CatalogueData> idMap;

    public CatalogueMap(){
        idMap = new TreeMap<>();
    }

    void addId(CatalogueIdentity id, SpanBranch span){
        checkNotNull(id, "id object cannot be null");
        checkNotNull(span, "id span cannot be null");

        CatalogueData data = idMap.get(id);
        if (data == null){
            data = new CatalogueData(this, id);
            idMap.put(id, data);
        }
        data.addId(span);
    }

    void addRef(CatalogueIdentity ref, SpanBranch span){
        checkNotNull(ref, "ref object cannot be null");
        checkNotNull(span, "ref span cannot be null");

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
        String ... category)
    {
        SortedMap<CatalogueIdentity, CatalogueData> map = delegate();
        CatalogueIdentity first = new CatalogueIdentity(
            ImmutableList.copyOf(category), "");
        category[category.length - 1] = category[category.length - 1] + (char)0;

        CatalogueIdentity last = new CatalogueIdentity(
            ImmutableList.copyOf(category), "");
        return map.subMap(first, last);
    }
}
