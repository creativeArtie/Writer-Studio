package com.creativeartie.writerstudio.lang;

import java.util.*;

import com.google.common.collect.*;

public final class CatalogueSystem extends ForwardingSortedMap<CatalogueIdentity,
    CatalogueData>
{
    private CatalogueMap documentMap;
    private List<CatalogueMap> referenceMaps;
    private Optional<CatalogueMap> outputMap;

    public CatalogueSystem(){
        documentMap = new CatalogueMap();
        referenceMaps = new ArrayList<>();
        outputMap = Optional.empty();
    }

    void clearMap(){
        documentMap.clearMap();
        outputMap = Optional.empty();
    }

    void add(SpanBranch span){
        documentMap.add(span);
    }

    void add(CatalogueMap map){
        referenceMaps.add(map);
    }

    public CatalogueMap delegate(){
        if (! outputMap.isPresent()){
            CatalogueMap map = new CatalogueMap();
            map.addMap(documentMap);
            for(CatalogueMap ref: referenceMaps){
                map.addMap(ref);
            }
            outputMap = Optional.of(map);
        }
        return outputMap.get();
    }
}
