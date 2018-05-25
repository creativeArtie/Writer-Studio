package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;

public class IDAssertions{
    private ArrayList<CatalogueIdentity> idList;
    private ArrayList<CatalogueStatus> idStatus;
    private ArrayList<Integer> idOrder;

    public IDAssertions(){
        idList = new ArrayList<>();
        idStatus = new ArrayList<>();
        idOrder = new ArrayList<>();
    }

    public IDAssertions addId(IDBuilder id){
        // TODO test SpanBranch ided too
        return this;
    }

    public IDAssertions addRef(IDBuilder id){
        // TODO test SpanBranch referenced too
        return this;
    }

    public IDAssertions addId(IDBuilder addId, int i) {
        return addId(addId, CatalogueStatus.UNUSED, i);
    }

    public IDAssertions addId(IDBuilder addId, CatalogueStatus newStatus,
            int i){
        CatalogueIdentity id = addId.build();
        idList.add(id);
        idStatus.add(newStatus);
        idOrder.add(i);
        return this;
    }

    public IDAssertions addRef(IDBuilder addId, int i) {
        return addRef(addId, CatalogueStatus.NOT_FOUND, i);
    }

    public IDAssertions addRef(IDBuilder addId, CatalogueStatus newStatus,
        int i){
        CatalogueIdentity id = addId.build();
        idList.add(id);
        idStatus.add(newStatus);
        idOrder.add(i);
        return this;
    }

    public void assertIds(Document doc){
        assertIds(doc, false);
    }

    public void assertIds(Document doc, boolean showIds){
        if (showIds){
            for(int i = 0; i < idOrder.size(); i++){
                int idx = idOrder.indexOf(i);
                System.out.print(idList.get(idx));
            }
            System.out.println();
        }

        Map<CatalogueIdentity, CatalogueData> map = doc.getCatalogue();
        if (showIds){
            for(Map.Entry<CatalogueIdentity, CatalogueData> entry:
                map.entrySet())
            {
                System.out.print(entry.getKey());
            }
            System.out.println();
            System.out.println();
        }
        assertEquals(idList.size(), map.size(), "Number of Ids");
        int i = 0;
        for (Map.Entry<CatalogueIdentity, CatalogueData> entry: map.entrySet()){
            int idx = idOrder.indexOf(i);
            assert idx != -1: idx;
            assertEquals(idList.get(idx), entry.getKey(), "Key idOrder");
            assertEquals(idStatus.get(idx), entry.getValue().getState(), "Status");
            i++;
        }
    }
}
