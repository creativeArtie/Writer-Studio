package com.creativeartie.jwriter.property;

public class IntegerProperty extends Property<Integer>{
    
    IntegerProperty(String key, PropertyManager manager){
        super(key, manager);
    }
    
    @Override
    protected Integer fromStorage(String value){
        return Integer.parseInt(value);
    }
    
    @Override
    protected String toStorage(Integer value){
        return String.valueOf(value);
    }
}
