package com.creativeartie.writerstudio.lang;

import java.util.ArrayList;
import java.util.Arrays;

public class IDBuilder {
    private ArrayList<String> category;
    private String id;

    public IDBuilder(){
        category = new ArrayList<>();
        id = "";
    }

    public IDBuilder addCategory(String ... cat){
        category.addAll(Arrays.asList(cat));
        return this;
    }

    public IDBuilder setId(String newId){
        id = newId;
        return this;
    }

    public CatalogueIdentity build(){
        CatalogueIdentity ans = new CatalogueIdentity(category, id);
        return ans;
    }

    public IDBuilder reset(){
        category = new ArrayList<>();
        id = "";
        return this;
    }
}
