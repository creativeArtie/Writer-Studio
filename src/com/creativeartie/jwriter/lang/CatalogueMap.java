package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.main.Checker;

/**
 * A System of {@link DirectorySpan} references with their status
 */
public final class CatalogueMap extends ForwardingSortedMap<CatalogueIdentity, 
        CatalogueData>{
    private final TreeMap<CatalogueIdentity, CatalogueData> idMap;
    
    public CatalogueMap(){
        idMap = new TreeMap<>();
    }
    
    public void addId(CatalogueIdentity id, SpanBranch span){
        Checker.checkNotNull(id, "id");
        Checker.checkNotNull(span, "span");
        
        CatalogueData data = idMap.get(id);
        if (data == null){
            data = new CatalogueData(this, id);
            idMap.put(id, data);
        }
        data.addId(span);
    }
    
    public void addRef(CatalogueIdentity ref, SpanBranch span){
        Checker.checkNotNull(ref, "ref");
        Checker.checkNotNull(span, "span");
        
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
