package com.creativeartie.writerstudio.project;

import java.util.*;
import java.util.function.*;

public enum PropertyType{
    ADDRESS("Address", (key, props) -> new PropertyAddress(key, props)),
    NAME("Name", (key, props) -> new PropertyName(key, props));

    private String typeName;
    private BiFunction<String, Properties, Property> propertySupplier;

    private PropertyType(String name, BiFunction<String, Properties, Property>
            supplier){
        typeName = name;
        propertySupplier = supplier;
    }

    public static PropertyType getType(String name){
        for(PropertyType type: values()){
            if (type.typeName.equals(name)){
                return type;
            }
        }
        throw new IllegalArgumentException("PropertyType \"" + name +
            "\" not found.");
    }

    Property buildProperty(String name, Properties file){
        return propertySupplier.apply(name, file);
    }
}