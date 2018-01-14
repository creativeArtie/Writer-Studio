package com.creativeartie.jwriter.property;

import java.util.*;

public class TextResource {

    private final String textKey;
    private final TextResourceManager textManager;
    private final ArrayList<TextResourceListener> textListeners;
    
    TextResource(String key, TextResourceManager manager){
        textKey = key;
        textManager = manager;
        textListeners = new ArrayList<>();
    }
    
    public String get(){
        return textManager.get(textKey);
    }
    
    public void addAndCall(TextResourceListener listener){
        addListener(listener);
        listener.update(this, get());
    }
    
    protected void update(){
        textListeners.forEach(listener -> listener.update(this, get()));
    }
    
    public void clearListeners(){
        textListeners.clear();
    }
    
    public void addListener(TextResourceListener listener){
        textListeners.add(listener);
    }
    
    public void removeListener(TextResourceListener listener){
        textListeners.remove(listener);
    }
}
