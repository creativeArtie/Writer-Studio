package com.creativeartie.jwriter.lang;

import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;
import static com.google.common.base.Preconditions.*;
/**
 * A subdivision of a {@link Document}
 */
public abstract class Span{

    private final HashSet<DetailListener> removeListeners;
    private final HashSet<DetailListener> changeListeners;
    private final HashSet<DetailListener> editListeners;

    Span(){
        removeListeners = new HashSet<>();
        changeListeners = new HashSet<>();
        editListeners = new HashSet<>();
    }

    public abstract String getRaw();

    public abstract int getLength();

    public abstract Document getDocument();

    public abstract SpanNode<?> getParent();

    public void addRemover(DetailListener listener){
        removeListeners.add(listener);
    }

    void setRemove(){
        removeListeners.forEach(remover -> remover.changed(this));
    }

    public void addChanged(DetailListener listener){
        changeListeners.add(listener);
    }

    public void addEdited(DetailListener listener){
        editListeners.add(listener);
    }

    void setEdit(){
        changeListeners.forEach(changer -> changer.changed(this));
        editListeners.forEach(editor -> editor.changed(this));
        if (! (this instanceof Document)){
            getParent().setEdit();
        }
    }

    protected abstract void docEdited();

    public Range<Integer> getRange(){
        return getDocument().getRangeCache(this, () ->{
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

    public int toLocalPosition(int index){
        checkPositionIndex(index, getLength(), "Index is out of range.");
        return getStart() - index;
    }
}
