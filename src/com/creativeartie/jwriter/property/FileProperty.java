package com.creativeartie.jwriter.property;

import java.io.*;

public class FileProperty extends Property<File>{

    FileProperty(String key, PropertyManager manager){
        super(key, manager);
    }

    @Override
    protected File fromStorage(String value){
        return new File(value);
    }

    @Override
    protected String toStorage(File value){
        return value.getPath();
    }
}
