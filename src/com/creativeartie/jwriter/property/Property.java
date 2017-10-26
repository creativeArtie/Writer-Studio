package com.creativeartie.jwriter.property;

import java.util.ArrayList;

public abstract class Property<T>{
    private final String propertyKey;
    private final PropertyManager parentManager;
    private final ArrayList<PropertyListener<T>> listeners;
    
    Property(String key, PropertyManager manager){
        propertyKey = key;
        parentManager = manager;
        listeners = new ArrayList<>();
    }
    
    public T get(){
        return fromStorage(parentManager.get(propertyKey));
    }
    
    protected abstract T fromStorage(String value);
    
    public void set(T value){
        parentManager.set(propertyKey, toStorage(value));
        for(PropertyListener<T> listener: listeners){
            listener.update(this, value);
        }
    }
    
    protected abstract String toStorage(T value);
    
    public void addAndCall(PropertyListener<T> listener){
        addListener(listener);
        listener.update(this, get());
    }
    
    public void clearListeners(){
        listeners.clear();
    }
    
    public void addListener(PropertyListener<T> listener){
        listeners.add(listener);
    }
    
    public void removeListener(PropertyListener<T> listener){
        listeners.remove(listener);
    }
}
