package com.creativeartie.writerstudio.file;

import com.creativeartie.writerstudio.lang.markup.*;

import java.io.*;
import java.util.zip.*;
import java.util.*;

import com.google.common.base.MoreObjects;
import static com.creativeartie.writerstudio.main.Checker.*;
import com.creativeartie.writerstudio.resource.*;

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

    public void save(OutputStream out){
    }
}
