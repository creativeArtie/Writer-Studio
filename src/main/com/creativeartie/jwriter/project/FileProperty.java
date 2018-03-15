package com.creativeartie.jwriter.project;

import java.io.*;
import java.util.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class FileProperty extends ForwardingMap<String, Property>{
    private Properties fileProperty;
    private File file;
    private HashMap<String, Property> loadedProperties;
    private final String VALUE_SEPARATOR = ";";
    private final String ENTRY_SEPARATOR = ":";
    private final String ADDRESS_PROPERTY = "Address";
    private final String NAME_PROPERTY = "Name";

    FileProperty(File file) throws FileNotFoundException, IOException{
        fileProperty = new Properties();
        HashMap<String, Property> props = null;
        try (FileInputStream input = new FileInputStream(file)){
            fileProperty.load(input);
            String keys = fileProperty.getProperty("Interal.PropertyLists");
            props = loadProps(keys);
        }
        loadedProperties = props == null? new HashMap<>(): props;
    }

    private HashMap<String, Property> loadProps(String keys){
        HashMap<String, Property> ans = new HashMap<>();
        for( Map.Entry<String, String> entry: Splitter.on(VALUE_SEPARATOR)
                .withKeyValueSeparator(ENTRY_SEPARATOR)
                .split(keys).entrySet()){
            Property prop = PropertyType.getType(entry.getValue())
                .buildProperty(entry.getKey(), fileProperty);
            ans.put(prop.getKeyName(), prop);
        }
        return ans;
    }

    public String getValue(String key){
        String name = key.substring(0, key.indexOf(Property.SEPARATOR));
        if (loadedProperties.containsKey(name)){
            return loadedProperties.get(name).getValue(name);
        }
        return "";
    }

    @Override
    public Map<String, Property> delegate(){
        return ImmutableMap.copyOf(loadedProperties);
    }
}