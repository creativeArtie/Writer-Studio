package com.creativeartie.writerstudio.file;

import java.io.*; // InputStream, IOException, OutputStream
import java.util.*;  // Properties

import com.creativeartie.writerstudio.resource.*;  // MetaData

import static com.creativeartie.writerstudio.main.Checker.*;

public final class TextProperties {
    private Properties metaData;

    TextProperties(){
        metaData = new Properties();
    }

    TextProperties(InputStream input) throws IOException{
        metaData = new Properties();
        metaData.load(input);
    }

    public String getText(MetaData key){
        return metaData.getProperty(key.getKey());
    }

    public void setText(MetaData key, String text){
        metaData.setProperty(key.getKey(), text);
    }

    public void save(OutputStream out) throws IOException{
        metaData.store(out, "Meta Data");
    }
}
