package com.creativeartie.jwriter.lang;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;
import static com.google.common.base.Preconditions.*;

/**
 * A list of {@link Span spans}. Mainly implements {@linkplain List}.
 * @param <T>
 *      Type of span stored
 */
public abstract class SpanNode<T extends Span> extends Span
        implements List<T> {

    protected SpanNode(){}

    @Override
    public final String getRaw(){
        return getDocument().getTextCache(this, () -> {
            /// Raw text is adding up raw text of each child.
            StringBuilder builder = new StringBuilder();

            for (Span span: this){
                builder.append(span.getRaw());
            }
            return builder.toString();
        });
    }

    @Override
    public int getLocalEnd(){
        return getRaw().length();
    }

    @Override
    public void setRemove(){
        for(Span child: this){
            child.setRemove();
        }
        super.setRemove();
    }

    /** Get the first span if it a subclass of {@code clazz}. */
    protected <T extends SpanBranch> Optional<T> spanAtFirst(Class<T> clazz){
        if (isEmpty()){
            /// Shouldn't really happen
            return Optional.empty();
        }
        if (clazz.isInstance(get(0))){
            return Optional.of(clazz.cast(get(0)));
        }
        return Optional.empty();
    }

    /** Find the first span that subclasses of {@code clazz}. */
    protected <T extends SpanBranch> Optional<T> spanFromFirst(Class<T> clazz){
        for(Span span: this){
            if (clazz.isInstance(span)){
                return Optional.of(clazz.cast(span));
            }
        }
        return Optional.empty();
    }

    /** Get the last span if it a subclass of {@code clazz}. */
    protected <T extends SpanBranch> Optional<T> spanAtLast(Class<T> clazz){
        if (isEmpty()){
            /// Shouldn't really happen
            return Optional.empty();
        }
        if (clazz.isInstance(get(size() - 1))){
            return Optional.of(clazz.cast(get(size() - 1)));
        }
        return Optional.empty();
    }

    /** Find the first span that subclasses of {@code clazz}. */
    protected <T extends SpanBranch> Optional<T> spanFromLast(Class<T> clazz){
        for(int i = size() - 1; i >= 0; i--){
            Span span = get(i);
            if (clazz.isInstance(span)){
                return Optional.of(clazz.cast(span));
            }
        }
        return Optional.empty();
    }

    /** Get the fist leaf span if it a has the style of {@code info}. */
    protected Optional<SpanLeaf> leafFromFrist(StyleInfoLeaf info){
        for (Span span: this){
            if (span instanceof SpanLeaf){
                SpanLeaf found = (SpanLeaf) span;
                if (found.getLeafStyle().equals(info)){
                    return Optional.of(found);
                }
            }
        }
        return Optional.empty();
    }

    /** Find the first leaf span that has the style of {@code info}. */
    protected Optional<SpanLeaf> leafFromLast(StyleInfoLeaf info){
        for(int i = size() - 1; i >= 0; i++){
            Span span = get(i);
            if (span instanceof SpanLeaf){
                SpanLeaf found = (SpanLeaf) span;
                if (found.getLeafStyle().equals(info)){
                    return Optional.of(found);
                }
            }
        }
        return Optional.empty();
    }

    /** Get the Span leaf with global position. Uses binary serach.*/
    public SpanLeaf getLeaf(int pos){
        /// Range check
        Range<Integer> range = getRange();
        if (! range.contains(pos)){
            throw new IndexOutOfBoundsException(pos + " is not between :" +
                range);
        }

        if (range.contains(pos)){
            /// Binary serach setup
            int low = 0;
            int high = size() - 1;
            int mid;
            Span span;
            while (low <= high){
                // move indexes
                mid = (low + high) / 2;
                span = get(mid);
                Range<Integer> child = span.getRange();
                if (child.lowerEndpoint() > pos){
                    high = mid - 1;
                } else if (child.upperEndpoint() <= pos){
                    low = mid + 1;
                } else {
                    /// Find span, but recurives if leaf not found yet.
                    return  span instanceof SpanLeaf? (SpanLeaf) span:
                        ((SpanNode)span).getLeaf(pos);
                }
            }
        }

        /// exception for not found. Somehow the code can end up here.
        throw new IndexOutOfBoundsException(pos + " is not between :" + range);
    }

    public abstract List<SpanLeaf> getLeaves();

    /// Implements List (ForwardList cannot be the super class)
    public abstract List<T> delegate();

    @Override
    public boolean add(T e){
        return delegate().add(e);
    }

    @Override
    public void add(int index, T element){
        delegate().add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c){
        return delegate().addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c){
        return delegate().addAll(index, c);
    }

    @Override
    public void clear(){
        delegate().clear();
    }

    @Override
    public boolean contains(Object o){
        return delegate().contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c){
        return delegate().containsAll(c);
    }

    @Override
    public boolean equals(Object o){
        return delegate().equals(o);
    }

    @Override
    public T get(int index){
        return delegate().get(index);
    }

    @Override
    public int hashCode(){
        return delegate().hashCode();
    }

    @Override
    public int indexOf(Object o){
        return delegate().indexOf(o);
    }

    @Override
    public boolean isEmpty(){
        return delegate().isEmpty();
    }

    @Override
    public Iterator<T> iterator(){
        return delegate().iterator();
    }

    @Override
    public int lastIndexOf(Object o){
        return delegate().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator(){
        return delegate().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index){
        return delegate().listIterator(index);
    }

    @Override
    public T remove(int index){
        return delegate().remove(index);
    }

    @Override
    public boolean remove(Object o){
        return delegate().remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c){
        return delegate().removeAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator){
        delegate().replaceAll(operator);
    }

    @Override
    public boolean retainAll(Collection<?> c){
        return delegate().retainAll(c);
    }

    @Override
    public T set(int index, T element){
        return delegate().set(index, element);
    }

    @Override
    public int size(){
        return delegate().size();
    }

    @Override
    public void sort(Comparator<? super T> c){
        delegate().sort(c);
    }

    @Override
    public Spliterator<T> spliterator(){
        return delegate().spliterator();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex){
        return delegate().subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray(){
        return delegate().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a){
        return delegate().toArray(a);
    }

    @Override
    public String toString(){
        return delegate().toString();
    }

}