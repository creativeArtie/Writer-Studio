package com.creativeartie.jwriter.lang;

import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;
import static com.google.common.base.Preconditions.*;

/**
 * A subdivision of a {@link Document document text}.
 */
public abstract class Span{

    private final HashSet<Consumer<Span>> removeListeners;
    private final HashSet<Consumer<Span>> changeListeners;
    private final HashSet<Consumer<Span>> updateListeners;

    Span(){
        removeListeners = new HashSet<>();
        changeListeners = new HashSet<>();
        updateListeners = new HashSet<>();
    }

    /** Get the raw text. */
    public abstract String getRaw();

    /** Get the length of the local text length. */
    public abstract int getLocalEnd();

    /** Get the {@link Document root span}. */
    public abstract Document getDocument();

    /** Get the {@link SpanNode parent span}. */
    public abstract SpanNode<?> getParent();

    /** Add a listener when this is removed. */
    public void addRemover(Consumer<Span> listener){
        removeListeners.add(listener);
    }

    /** Calls the remove listeners. */
    void setRemove(){
        removeListeners.forEach(remover -> remover.accept(this));
    }

    /** Add a listener when this span's children has been replaced. */
    public final void addEditor(Consumer<Span> listener){
        changeListeners.add(listener);
    }

    /** Add a listener when this span's text has changed. */
    public final void addUpdater(Consumer<Span> listener){
        updateListeners.add(listener);
    }

    /** Calls the change listeners and all it's parent update listeners. */
    void setUpdated(){
        changeListeners.forEach(changer -> changer.accept(this));
        updateParent();
    }

    /**
     * Calls the update listeners, including the parent's. Helper method of
     * {@link #setUpdated()}.
     *
     * This is recursive method and cannot be inlined into
     * {@linkplain setUpdate()}.
     */
    private final void updateParent(){
        updateListeners.forEach(editor -> editor.accept(this));
        if (! (this instanceof Document)){
            ((Span)getParent()).updateParent();
        }
    }

    /** Info the subclass the document has been edited. */
    protected abstract void docEdited();

    /** Get the start and end of this span in relation the the document. */
    public Range<Integer> getRange(){
        /// Look up in the cache first
        return getDocument().getRangeCache(this, () ->{
            // get the start of the parent's span.
            int ans = getParent().getStart();
            for(Span span: getParent()){
                if (span == this){
                    return Range.closedOpen(ans, ans + getLocalEnd());
                }
                // For each child of the parent exclude this:
                ans += span.getLocalEnd();
            }
            // This Span is not a child of the parent
            assert false: getRaw();
            return null;

        });
    }

    /** Get the start of this span in relation the the document. */
    public final int getStart(){
        return getRange().lowerEndpoint();
    }

    /** Get the end of this span in relation the the document. */
    public final int getEnd(){
        return getRange().upperEndpoint();
    }

    /** Convert a global index to location index. */
    public final int toLocalPosition(int index){
        checkPositionIndex(index, getLocalEnd(), "Index is out of range.");
        return getStart() - index;
    }
}
