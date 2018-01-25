package com.creativeartie.jwriter.lang;

import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;

/**
 * A subdivision of a {@link Document document text}.
 *
 * This class when to set edit/remove, and locations.
 */
public abstract class Span{

    /// Part 1: Basic Cache and basic instance fields setup:

    /** A simple cache method that make use of {@link Optional}.*/
    protected static <T> Optional<T> getCache(Optional<T> found,
            Supplier<T> maker){
        checkNotNull(found, "found");
        checkNotNull(maker, "maker");
        if (found.isPresent()){
            return found;
        }
        return Optional.of(maker.get());
    }

    private final HashSet<Consumer<Span>> removeListeners;
    private final HashSet<Consumer<Span>> changeListeners;
    private final HashSet<Consumer<Span>> updateListeners;

    Span(){
        removeListeners = new HashSet<>();
        changeListeners = new HashSet<>();
        updateListeners = new HashSet<>();
    }

    /// Part 2: Common Methods of Span subclasses

    /** Get the raw text. */
    public abstract String getRaw();

    /** Get the {@link Document root span}. */
    public abstract Document getDocument();

    /** Get the {@link SpanNode parent span}. */
    public abstract SpanNode<?> getParent();

    /// Part 3: Handling changes to the Span.

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
        checkNotNull(listener, "listener");
        changeListeners.add(listener);
    }

    /** Add a listener when this span's text has changed. */
    public final void addUpdater(Consumer<Span> listener){
        checkNotNull(listener, "listener");
        updateListeners.add(listener);
    }

    /** Calls the change listeners and all it's parent update listeners. */
    void setUpdated(){
        if (this instanceof Catalogued){
            ((Catalogued)this).getSpanIdentity().ifPresent(id ->{
                assert getDocument().getCatalogue().get(id) != null;
            });
        }
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

    /** Listened that the document has been edited. */
    protected abstract void clearDocCache();

    /// Part 4: Handling finding positions.

    /** Get the start and end of this span in relation the the document. */
    public Range<Integer> getRange(){
        /// Look up in the cache first
        return getDocument().getRangeCache(this, () ->{
            /// get the start of the parent's span.
            int ans = getParent().getStart();
            for(Span span: getParent()){
                if (span == this){
                    return Range.closedOpen(ans, ans + getLocalEnd());
                }
                /// For each child of the parent exclude this:
                ans += span.getLocalEnd();
            }
            /// This Span is not a child of the parent
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

    // public int getLocalStart(){ return 0; } /// Code here for completeness

    /** Get the length of the local text length. */
    public abstract int getLocalEnd();

    /** Convert a global index to location index. */
    public final int toLocalPosition(int index){
        checkIndex(index, "index", getEnd(), true);
        return getStart() - index;
    }


    /** Is this {@code Span}'s text is at the first in the document. */
    public final boolean isFirst(){
        return checkLocation(parent -> parent.get(0));
    }

    /** Is this {@code Span}'s text is at the last in the document. */
    public final boolean isLast(){
        return checkLocation(parent -> parent.get(parent.size() - 1));
    }

    /**
     * Check if the span is at the first or at the end. Helper method of
     * {@link #isFirst()} and {@link #isLast()}.
     */
    private final boolean checkLocation(Function<SpanNode<?>, Span> locateChild){
        Span child = this;
        SpanNode<?> parent = child.getParent();
        while (locateChild.apply(parent) == child){
            if (parent instanceof Document) {
                /// it is the last of the doucment
                return true;
            } else {
                /// still have parents
                child = parent;
                parent = child.getParent();
            }
        }

        /// it is in the middle of the children list
        return false;
    }
}
