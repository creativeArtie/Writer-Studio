package com.creativeartie.jwriter.property;

import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

public class MapProperty extends Property<Map<String, String>>{
    private final String lineSpliter;
    private final String fieldSpliter;
    
    MapProperty(String key, PropertyManager manager, String field, String line){
        super(key, manager);
        lineSpliter = line;
        fieldSpliter = field;
    }
    
    @Override
    protected Map<String, String> fromStorage(String value){
        if (value.isEmpty()){
            return ImmutableMap.of(); 
        }
        if (value.endsWith(lineSpliter)){
            value = value.substring(0, value.length() - lineSpliter.length());
        }
        return Splitter.on(lineSpliter).withKeyValueSeparator(fieldSpliter)
            .split(value);
    }
    
    @Override
    protected String toStorage(Map<String, String> value){
        return Joiner.on(lineSpliter).withKeyValueSeparator(fieldSpliter).join(value);
    }
}
