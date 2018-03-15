package com.creativeartie.jwriter.project;

import java.io.*;
import java.util.*;

import com.google.common.base.*;

public class FileProperty{
    private Properties fileProperty;
    private File file;
    private HashMap<String, Property> loadedProperties;
    private final String VALUE_SEPARATOR = ";";
    private final String ENTRY_SEPARATOR = ":";
    private final String ADDRESS_PROPERTY = "Address";
    private final String NAME_PROPERTY = "Name";

    FileProperty(File file){
        fileProperty = new Properties();
        HashMap<String, Property> keys = null;
        try (FileInputStream input = new FileInputStream(file)){
            fileProperty.load(input);
            String keys = fileProperty.getProperty("Interal.PropertyLists");
            keys = loadProps(keys);
        }
        loadedProperties = keys == null? new HashMap<>() keys;
    }

    private HashMap<String, Property> loadProps(String keys){
        HashMap<String, Property> ans = new HashMap<>();
        for( Map.Entry<String, String> entry: Splitter.on(VALUE_SEPARATOR)
                .withKeyValueSeparator(ENTRY_SEPARATOR)
                .split(keys).entrySet()){
            Property prop;
            switch (entry.getValue()){
            case ADDRESS_PROPERTY:
                prop = new PropertyAddress(entry.getKey(), fileProperty);
                break;
            case NAME_PROPERTY:
                prop = new PropertyName(entry.getKey(), fileProperty);
                break;
            default:
                throw new IllegalArgumentException(
                    "Unknown property type for: " + entry.getKey());
            }
            ans.put(prop.getKeyName(), prop);
        }
        return ans;
    }

    public String getValue(String key){
        String name = key.substring(key, key.indexOf(Property.SEPARATOR));
        if (loadProperties.contains(name)){
            return loadProperties.get(name).getValue(name);
        }
        return "";
    }

    public Property getProperty(String name, String type){
        String name = key.substring(key, key.indexOf(Property.SEPARATOR));
        if (loadProperties.contains(name)){
            return loadProperties.get(name).getValue(name);
        }
        return "";

    }
}