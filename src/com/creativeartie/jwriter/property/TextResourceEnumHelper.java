package com.creativeartie.jwriter.property;

public interface TextResourceEnumHelper{
    public TextResource delegate();
    
    public default String get(){
        return delegate().get();
    }
    
    public default void addAndCall(TextResourceListener listener){
        delegate().addAndCall(listener);
    }
    
    public default void clearListeners(){
        delegate().clearListeners();
    }
    
    public default void addListener(TextResourceListener listener){
        delegate().addListener(listener);
    }
    
    public default void removeListener(TextResourceListener listener){
        delegate().removeListener(listener);
    }
}
