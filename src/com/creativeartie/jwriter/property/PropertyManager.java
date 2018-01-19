package com.creativeartie.jwriter.property;

import java.util.*;
import java.io.*;

public class PropertyManager{
    private static final String EXTENSION = ".properties";
    private final HashMap<String, Property<?>> properties;

    private final Properties defaultProp;
    private final Properties currentProp;

    private final String baseFile;
    private final String userFile;
    public PropertyManager(String base, String user) throws IOException{
        baseFile = "/" + base + EXTENSION;
        defaultProp = new Properties();
        defaultProp.load(PropertyManager.class.getResourceAsStream(baseFile));
        currentProp = new Properties(defaultProp);
        userFile = user + EXTENSION;
        File data = new File(userFile);
        if (! data.createNewFile()){
            try (FileInputStream load = new FileInputStream(data)){
                currentProp.load(load);
            }
        }
        properties = new HashMap<>();
    }

    public MapProperty getMapProperty(String key, String field, String line){
        MapProperty ans = getProperty(key, MapProperty.class);
        if (ans == null){
            ans = new MapProperty(key, this, field, line);
            properties.put(key, ans);
        }
        return ans;
    }

    public StyleProperty getStyleProperty(String key){
        StyleProperty ans = null;
        try {
            ans = getProperty(key, StyleProperty.class);
        } catch (IllegalArgumentException ex){
            return new StyleProperty(key, this);
        }
        if (ans == null){
            ans = new StyleProperty(key, this);
            properties.put(key, ans);
        }
        return ans;
    }

    public IntegerProperty getIntProperty(String key){
        IntegerProperty ans = getProperty(key, IntegerProperty.class);
        if (ans == null){
            ans = new IntegerProperty(key, this);
            properties.put(key, ans);
        }
        return ans;
    }

    private <T> T getProperty(String key, Class<T> cast){
        Property<?> ans = found(key);
        if (ans != null){
            if (cast.isInstance(ans)){
                return cast.cast(ans);
            }
            throw new IllegalArgumentException(
                "Property is not an object of: " + cast.getSimpleName());
        }
        return null;
    }

    private Property<?> found(String key){
        if (currentProp.getProperty(key) == null){
            throw new IllegalArgumentException("Property is not found: " + key +
                ".");
        }
        if (properties.containsKey(key)){
            return properties.get(key);
        }
        return null;
    }

    public void store() throws Exception{
        try(FileOutputStream out = new FileOutputStream(userFile)){
            currentProp.store(out, "");
        }
    }

    String get(String key){
        return currentProp.getProperty(key);
    }

    void set(String key, String property){
        currentProp.setProperty(key, property);
    }
}
