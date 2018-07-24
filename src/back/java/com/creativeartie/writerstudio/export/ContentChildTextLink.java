package com.creativeartie.writerstudio.export;

import java.util.*;

class ContentChildTextLink<T extends Number> extends ContentChildText<T>{

    private Optional<String> linkPath;

    ContentChildTextLink(ContentPolicyFont<T> font, String text, String path){
        super(font, text);
        linkPath = Optional.ofNullable(path);
    }

    T getFillHeight(){
        return getHeight();
    }
}
