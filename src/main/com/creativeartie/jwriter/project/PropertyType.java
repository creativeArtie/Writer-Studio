package com.creativeartie.jwriter.project;

import java.util.*;
import java.util.function.*;

public enum PropertyType{
    ADDRESS("Address"), NAME("Name");

    private String typeName;
    private BiFunction<String, FileProperty, Property> supplier;

    private PropertyType(String name){
        typeName = name;
    }

    public static PropertyType getType(){

    }
}