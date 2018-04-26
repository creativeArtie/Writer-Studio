package com.creativeartie.writerstudio.lang;

import java.util.*; // HashSet, Optional
import java.util.function.*; // Consumer, Function
import java.util.concurrent.*; // Callable, ExecutionException

import com.google.common.collect.*; // Range, ImmuableList
import com.google.common.cache.*; // Range, ImmuableList

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * A subdivision of a {@link Document document text}.
 */
public abstract class Span{
    /** Get the raw text. */
    public abstract String getRaw();

    /** Get the length of the local text length. */
    public abstract int getLocalEnd();

    /** Get the {@link Document root span}. */
    public abstract Document getDocument();

    /** Get the {@link SpanNode parent span}. */
    public abstract SpanNode<?> getParent();

    abstract void setParent(SpanNode<?> parent);

    public boolean isRemoved(){
        return this instanceof Document? false:
            (getParent().indexOf(this) == -1? true: getParent().isRemoved());
    }

    /**
     * Finds the index of this span in the parent or return -1.
     */
    public final int getPosition(){
        if (this instanceof Document){
            return -1;
        }
        SpanNode<?> parent = getParent();
        return parent != null? parent.indexOf(this): -1;
    }

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

    /** Convert a global index to location index. */
    public final int toLocalPosition(int index){
        checkIndex(index, "index", getLocalEnd(), true);
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

    /** Gets the parent that the subclasses {@link Class}*/
    public <T> Optional<T> getParent(Class<T> clazz){
        checkNotNull(clazz, "clazz");
        ImmutableList.Builder<SpanBranch> builder = ImmutableList.builder();
        SpanNode<?> parent = getParent();
        while(parent instanceof SpanBranch){
            if (clazz.isInstance(parent)){
                /// Finds a match
                return Optional.of(clazz.cast(parent));
            }
            /// get the parent
            parent = parent.getParent();
        }
        /// Not found.
        return Optional.empty();
    }

    public final int getStartColumn(){
        return getDocument().getColumn(getStart());
    }

    /** Fine line index. */
    public final int getStartLine(){
        return getDocument().getLine(getStart());
    }
}
