package com.creativeartie.writer.lang;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** A subdivision of a {@link Document document text} and {@linkplain Document}.
 *
 * Purpose
 * <ul>
 * <li> Define common methods </li>
 * <li> Answer child related questions. </li>
 * <li> Locate itself by location and indexes. </li>
 * </ul>
 */
public abstract class Span implements Comparable<Span>{

    /// %Part 1: Constructor ###################################################

    private static long counter = Long.MIN_VALUE;
    private long uniqueId;

    private static synchronized long createId(){
        return counter++;
    }

    /** Package-ize Span creation.*/
    Span(){
        uniqueId = createId();
    }

    /// %Part 2: Abstract Get and Set ##########################################

    /** Get the raw text.
     *
     * @return answer
     */
    public abstract String getRaw();

    /** Get the length of the local text length.
     *
     * @return answer
     */
    public abstract int getLocalEnd();

    /** Get the {@link Document root span}.
     *
     * @return answer
     */
    public abstract Document getDocument();

    /** Get the {@link SpanNode parent span}.
     *
     * {@link Document} will throws an
     * {@link UnsupportedOperationException} if called.
     *
     * @return answer
     */
    public abstract SpanNode<?> getParent();

    /** Get the {@link SpanNode parent span}.
     *
     * {@link Document} will throws an
     * {@link UnsupportedOperationException} if called.
     *
     * @param parent
     *      parent to set
     * @see SpanBranch#setChildren(List)
     * @see Document#parseDocument(List)
     * @see Document#setChildren(List)
     */
    abstract void setParent(SpanNode<?> parent);

    /// %Part 3: Child Related Methods #########################################

    /** Check if this span has been removed.
     *
     * @return answer
     */
    public final boolean isRemoved(){
        return this instanceof Document? false:
            (getParent().indexOf(this) == -1? true: getParent().isRemoved());
    }

    /** Gets the parent that the subclasses {@link Class}.
     *
     * @param clazz
     *      class of the require span
     * @return answer
     */
    public <T> Optional<T> getParent(Class<T> clazz){
        argumentNotNull(clazz, "clazz");
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

    /** Finds the index of this span in the parent or return -1.
     *
     * @return answer
     */
    public final int getPosition(){
        if (this instanceof Document){
            return -1;
        }
        SpanNode<?> parent = getParent();
        return parent != null? parent.indexOf(this): -1;
    }


    /** Convert a document index to location index.
     *
     * @param index
     *      document index
     * @return answer
     */
    public final int toLocalPosition(int index){
        indexCloseOpen(index, "index", 0, getEnd());
        return getStart() - index;
    }

    /// %Part 4: Document Related Method #######################################
    /// %Part 4.1: First and Last ==============================================

    /** Is this {@code Span}'s text is at the first in the document.
     *
     * @return answer
     */
    public final boolean isDocumentFirst(){
        if (this instanceof Document) return true;
        return checkLocation(parent -> parent.get(0));
    }

    /** Is this {@code Span}'s text is at the last in the document.
     *
     * @return answer
     */
    public final boolean isDocumentLast(){
        if (this instanceof Document) return true;
        return checkLocation(parent -> parent.get(parent.size() - 1));
    }

    /**
     * Check if the span is at the first or at the end.
     *
     * @param locater
     *      span locater
     * @return answer
     * @see #isDocumentFirst()
     * @see #isDocumentLast()
     */
    private final boolean checkLocation(Function<SpanNode<?>, Span> locater){
        Span child = this;
        SpanNode<?> parent = child.getParent();
        while (locater.apply(parent) == child){
            if (parent instanceof Document){
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

    /** Set if the document is ready to fire listeners.
     *
     * If {@code true} listeners will also be fired at the same time as set
     */
    protected void setFireReady(boolean b){
        getDocument().setFireReady(b);
    }

    /// %Part 4.2: Index Pointer ===============================================

    /** Get the start and end of this span in relation the the document.
     *
     * @return answer
     */
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

    /** Get the span start index the the document.
     *
     * @return answer
     */
    public final int getStart(){
        return getRange().lowerEndpoint();
    }

    /** Get the span end index in the document.
     *
     * @return answer
     */
    public final int getEnd(){
        return getRange().upperEndpoint();
    }

    /** Get the start column in the document.
     *
     * @return answer
     */
    public final int getStartColumn(){
        return getDocument().getColumn(getStart());
    }

    /** Fine line index in the document.
     *
     * @return answer
     */
    public final int getStartLine(){
        return getDocument().getLine(getStart());
    }

    /// %Part 5 Equal Overrides ================================================

    @Override
    public boolean equals(Object obj){
        return obj instanceof Span && ((Span)obj).uniqueId == uniqueId;
    }

    @Override
    public int hashCode(){
        return Objects.hash(uniqueId);
    }

    @Override
    public int compareTo(Span span){
        return equals(span)? 0: (span.uniqueId > uniqueId? 1: -1);
    }
}
