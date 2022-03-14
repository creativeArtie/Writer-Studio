package com.creativeartie.writer.lang;

import org.junit.jupiter.api.function.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

// TODO add test to number of id/refs
/// Assertion for {@link CatalogueMap}.
public class IDAssert{
    private ArrayList<CatalogueIdentity> idList;
    private ArrayList<CatalogueStatus> idStatus;
    private ArrayList<Integer> idOrder;

    public IDAssert(){
        idList = new ArrayList<>();
        idStatus = new ArrayList<>();
        idOrder = new ArrayList<>();
    }

    /// please call {@link #addId(IDBuilder, int, [CatalougueStatus])} first
    public IDAssert addId(IDBuilder id){
        return this;
    }

    /// please call {@link #addRef(IDBuilder, int, [CatalougueStatus])} first
    public IDAssert addRef(IDBuilder id){
        return this;
    }

    public IDAssert addId(IDBuilder addId, int i) {
        return addId(addId, i, CatalogueStatus.UNUSED);
    }

    public IDAssert addId(IDBuilder addId, int i, CatalogueStatus newStatus){
        CatalogueIdentity id = addId.build();
        idList.add(id);
        idStatus.add(newStatus);
        idOrder.add(i);
        return this;
    }

    /// please call {@link #addId(IDBuilder, int, [CatalougueStatus])} first
    public IDAssert addRef(IDBuilder addId, int i) {
        return addRef(addId, i, CatalogueStatus.NOT_FOUND);
    }

    public IDAssert addRef(IDBuilder addId, int i, CatalogueStatus newStatus){
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
        /// prints the expected ids
        if (showIds){
            System.out.println(doc.getRaw());
            for(int i = 0; i < idOrder.size(); i++){
                int idx = idOrder.indexOf(i);
                System.out.print(idList.get(idx));
            }
            System.out.println();
        }

        Map<CatalogueIdentity, CatalogueData> map = doc.getCatalogue();
        /// prints the actual ids
        if (showIds){
            for(Map.Entry<CatalogueIdentity, CatalogueData> entry:
                map.entrySet())
            {
                System.out.print(entry.getKey());
            }
            System.out.println();
            System.out.println();
        }

        /// actual tests
        ArrayList<Executable> list = new ArrayList<>();
        list.add(() -> assertEquals(idList.size(), map.size(), "Number of Ids"));
        int i = 0;
        for (Map.Entry<CatalogueIdentity, CatalogueData> entry: map.entrySet()){
            int idx = idOrder.indexOf(i);
            assert idx != -1: idx;
            list.add(() -> assertEquals(idList.get(idx), entry.getKey(),
                "Key idOrder"));
            list.add(() -> assertEquals(idStatus.get(idx), entry.getValue()
                .getState(), "Status"));
            i++;
        }
        assertAll("ids", list);
    }
}
