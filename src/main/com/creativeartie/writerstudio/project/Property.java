package com.creativeartie.writerstudio.project;

import java.util.*;

public abstract class Property{
    public static String SEPARATOR = ".";

    private Properties storedValue;

    private String keyName;

    Property(String name, Properties props){
        keyName = name;
        storedValue = props;
    }

    String getKeyName(){
        return keyName;
    }

    String getProperty(String part){
        return storedValue.getProperty(keyName + SEPARATOR + part);
    }

    void setProperty(String part, String value){
        storedValue.setProperty(keyName + SEPARATOR + part, value);
    }

    public String getValue(String key){
        return convertKey(key.substring(key.indexOf(SEPARATOR)));
    }

    abstract String convertKey(String part);
}