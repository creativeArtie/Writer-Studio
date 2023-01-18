package com.creativeartie.writer.lang;

import java.util.*;

import com.google.common.collect.*;

public final class CatalogueSystem extends ForwardingSortedMap<CatalogueIdentity,
    CatalogueData>
{
    private CatalogueMap documentMap;
    private List<CatalogueMap> referenceMaps;
    private CatalogueMap outputMap;

    public CatalogueSystem(){
        documentMap = new CatalogueMap();
        referenceMaps = new ArrayList<>();
        outputMap = new CatalogueMap();
    }

    void clearMap(){
        documentMap.clearMap();
        outputMap.clearMap();
        for (CatalogueMap map: referenceMaps){
            outputMap.addMap(map);
        }
    }

    void add(SpanBranch span){
        documentMap.add(span);
        outputMap.add(span);
    }

    void add(CatalogueMap map){
        referenceMaps.add(map);
        outputMap.addMap(map);
    }

    public CatalogueMap delegate(){
        return outputMap;
    }

    public CatalogueMap getDocumentMap(){
        return documentMap;
    }
}
