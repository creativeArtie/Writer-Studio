package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

public class LinedLevelHeadDebug {

    public static IDBuilder buildId(String name){
        return new IDBuilder().addCategory("link").setId(name);
    }
}
