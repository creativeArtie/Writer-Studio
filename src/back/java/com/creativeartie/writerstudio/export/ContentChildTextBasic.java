package com.creativeartie.writerstudio.export;

import java.util.*;

class ContentChildTextBasic<T extends Number> extends ContentChildText<T>{

    ContentChildTextBasic(ContentPolicyFont<T> font, String text){
        super(font, text);
    }

    T getFillHeight(){
        return getHeight();
    }
}
