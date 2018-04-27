package com.creativeartie.writerstudio.lang;

import java.util.*; // Optional;
import java.util.function.*; // Function;

import com.google.common.collect.*; // Range, ImmuableList;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A subdivision of a {@link Document document text} and {@linkplain Document}.
 *
 * Purpose
 * <ul>
 * <li> Define common methods </li>
 * <li> Answer child related questions. </li>
 * <li> Locate itself by location and indexes. </li>
 * </ul>
 */
public abstract class Span{

    /// %Part 1: Constructor ###################################################

    /** Package-ize Span creation.*/
    Span(){}

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
     * {@link Document} will throws an {@link IllegalOperationExcpetion} if
     * called.
     *
     * @return answer
     */
    public abstract SpanNode<?> getParent();

    /** Get the {@link SpanNode parent span}.
     *
     * {@link Document} will throws an {@link IllegalOperationExcpetion} if
     * called.
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
        return checkLocation(parent -> parent.get(0));
    }

    /** Is this {@code Span}'s text is at the last in the document.
     *
     * @return answer
     */
    public final boolean isDocumentLast(){
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
}
