package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;
/**
 * A subdivision of a {@link Document}
 */
public abstract class Span{
    
    private final HashSet<DetailListener> removeListeners;
    private final HashSet<DetailListener> editListeners;
    
    Span(){
        removeListeners = new HashSet<>();
        editListeners = new HashSet<>();
    }
    
    public abstract String getRaw();
    
    public abstract int getLength();
    
    public abstract Document getDocument();
    
    public abstract SpanNode<?> getParent();
    
    public void addRemover(DetailListener listener){
        removeListeners.add(listener);
    }
    
    public void setRemove(){
        removeListeners.forEach(remover -> remover.changed(this));
    }
    
    public void addEditor(DetailListener listener){
        editListeners.add(listener);
    }
    
    public void setEdit(){
        editListeners.forEach(editor -> editor.changed(this));
        if (! (this instanceof Document)){
            getParent().setEdit();
        }
    }
    
    void invalidateCache(){}
        
    public Range<Integer> getRange(){
        return getDocument().getRange(this, () ->{
            int ans = getParent().getStart();
            for(Span span: getParent()){
                if (span == this){
                    return Range.closedOpen(ans, ans + getLength());
                }
                ans += span.getLength();
            }
            assert false: getRaw();
            return Range.closedOpen(ans, ans + getLength());
            
        });
    }
    
    public int getStart(){
        return getRange().lowerEndpoint();
    }
    
    public int getEnd(){
        return getRange().upperEndpoint();
    }
}
