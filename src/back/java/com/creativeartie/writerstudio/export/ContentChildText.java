package com.creativeartie.writerstudio.export;

import java.util.*;

abstract class ContentChildText<T extends Number> extends Content<T>{

    private ContentPolicyFont<T> contentFont;
    private String storedText;

    ContentChildText(ContentPolicyFont<T> font, String text){
        super(font);
        contentFont = font;
        storedText = text;
    }

    final String getText(){
        return storedText;
    }

    @Override
    final T getWidth(){
        return contentFont.getWidth(storedText);
    }

    boolean merge(Content<T> content){
        if (getClass() == content.getClass()){
            ContentChildText text = (ContentChildText)content;
            if (contentFont == text.contentFont){
                storedText += text.storedText;
                return true;
            }
        }
        return false;
    }

    @Override
    final T getHeight(){
        return contentFont.getHeight(storedText);
    }

}
