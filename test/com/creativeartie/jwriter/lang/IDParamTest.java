package com.creativeartie.jwriter.lang;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(Parameterized.class)
public abstract class IDParamTest {
    public enum States{ID, REF}
    
    private static States ID = States.ID;
    private static States REF = States.REF;
    
    @Parameters
    public static Collection<Object[]> data() {
        ArrayList<Object[]> data = new ArrayList<>();
        add(data, CatalogueStatus.UNUSED,    new States[]{ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  ID,  ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  ID,  ID, ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  ID,  ID, REF});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  ID,  REF});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  ID,  REF, ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  ID,  REF, REF});
        add(data, CatalogueStatus.READY,     new States[]{ID,  REF});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  REF, ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  REF, ID, ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  REF, ID, REF});
        add(data, CatalogueStatus.READY,     new States[]{ID,  REF, REF});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{ID,  REF, REF, ID});
        add(data, CatalogueStatus.READY,     new States[]{ID,  REF, REF, REF});
        add(data, CatalogueStatus.NOT_FOUND, new States[]{REF});
        add(data, CatalogueStatus.READY,     new States[]{REF, ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{REF, ID,  ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{REF, ID,  ID, ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{REF, ID,  ID, REF});
        add(data, CatalogueStatus.READY,     new States[]{REF, ID,  REF});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{REF, ID,  REF, ID});
        add(data, CatalogueStatus.READY,     new States[]{REF, ID,  REF, REF});
        add(data, CatalogueStatus.NOT_FOUND, new States[]{REF, REF});
        add(data, CatalogueStatus.READY,     new States[]{REF, REF, ID});
        add(data, CatalogueStatus.MULTIPLE,  new States[]{REF, REF, ID, ID});
        add(data, CatalogueStatus.READY,     new States[]{REF, REF, ID, REF});
        add(data, CatalogueStatus.NOT_FOUND, new States[]{REF, REF, REF});
        add(data, CatalogueStatus.READY,     new States[]{REF, REF, REF, ID});
        add(data, CatalogueStatus.NOT_FOUND, new States[]{REF, REF, REF, REF});
        return data;
    }
    
    private static void add(ArrayList<Object[]> data, CatalogueStatus expect, 
            States[] inputs){
        
        data.add(new Object[]{expect, inputs});
    }

    @Parameter
    public CatalogueStatus expected;

    @Parameter(1)
    public States[] input;
    
}
