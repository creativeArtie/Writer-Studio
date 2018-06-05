package com.creativeartie.writerstudio.lang;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import com.google.common.cache.*;
import com.google.common.collect.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A list of {@link Span spans} which mainly implements {@linkplain List}.
 *
 * Purpose
 * <ul>
 * <li> Update and editing the span </li>
 * <li> Get span branch with certain class and location.</li>
 * <li> Get span leaf as a list or itself.</li>
 * <li> Adding and removing listeners.</li>
 * <li> Implements {@link List} class.</li>
 * </ul>
 * @param <T>
 *      Type of span stored
 */
public abstract class SpanNode<T extends Span> extends Span implements List<T>{

    /// %Part 1: Constructor & Fields ##########################################

    private final Cache<CacheKeyMain<?>, Object> spanMainCache;
    private final Cache<CacheKeyOptional<?>, Optional<?>> spanOptionalCache;
    private final Cache<CacheKeyList<?>, List<?>> spanListCache;

    private final Cache<CacheKeyMain<?>, Object> docMainCache;
    private final Cache<CacheKeyOptional<?>, Optional<?>> docOptionalCache;
    private final Cache<CacheKeyList<?>, List<?>> docListCache;

    private final HashSet<Consumer<SpanNode<T>>> childEditedListeners;
    private final HashSet<Consumer<SpanNode<T>>> spanRemovedListeners;
    private final HashSet<Consumer<SpanNode<T>>> spanEditedListeners;
    private final HashSet<Consumer<SpanNode<T>>> docEditedListeners;
    private boolean editedChild;
    private boolean editedTarget;

    /** Creates an instanceof {@linkplain SpanNode}.*/
    protected SpanNode(){
        spanMainCache = CacheBuilder.newBuilder().build();
        spanOptionalCache = CacheBuilder.newBuilder().build();
        spanListCache = CacheBuilder.newBuilder().build();

        docOptionalCache = CacheBuilder.newBuilder().build();
        docMainCache = CacheBuilder.newBuilder().build();
        docListCache = CacheBuilder.newBuilder().build();

        childEditedListeners = new HashSet<>();
        spanRemovedListeners = new HashSet<>();
        spanEditedListeners = new HashSet<>();
        docEditedListeners = new HashSet<>();
        editedChild = false;
        editedTarget = false;
    }

    /// %Part 2: Update and Edit ###############################################

    /** Runs a command that replace the text in the {@linkplain SpanNode}.
     *
     * @param command
     *      using command
     */
    protected abstract void runCommand(Command command);

    /** Update the span and it's parents
     *
     * @param spans
     *      new children spans
     * @see #SpanBranch#reparseText(String, SetupParser)
     */
    final void updateSpan(List<T> spans){
        setChildren(spans);
        setEdited();
        getParent().updateParent();
    }

    /** Set the span children
     *
     * @param spans
     *      new children spans
     * @see SpanBranch#SpanBranch(List)
     * @see #updateSpan(List)
     */
    abstract void setChildren(List<T> spans);

    /** Recrusively update the parent or document span.
     *
     * @see #updateSpan(List);
     */
    final void updateParent(){
        clearSpanCache();
        editedChild = true;

        if (this instanceof SpanBranch){
            getParent().updateParent();
        } else {
            ((Document)this).updateDoc();
        }
    }

    /** Set the span it's children to remove.
     *
     * @see Document#reparseDocument(String)
     * @see SpanBranch#setChildren(List)
     */
    final void setRemove(){
        getDocument().removeSpan(this);
        for (T child: this){
            if (child instanceof SpanBranch) ((SpanBranch)child).setRemove();
        }
    }

    final void setEdited(){
        clearSpanCache();
        editedTarget = true;
    }

    /** Clear span's cache.
     *
     * @see Document#reparseDocument(String)
     * @see #updateSpan(List)
     * @see #updateParent()
     */
    final void clearSpanCache(){
        spanMainCache.invalidateAll();
        spanOptionalCache.invalidateAll();
        spanListCache.invalidateAll();
    }

    /** clear the document's cache
     *
     * @see Document#updateDoc()
     */
    void clearDocCache(){
        docMainCache.invalidateAll();
        docOptionalCache.invalidateAll();
        docListCache.invalidateAll();
    }

    /** Fires remove listeners.
     *
     * @see #updateDoc()
     */
    final void fireRemoveListeners(){
    fire(spanRemovedListeners);
        fire(docEditedListeners);
    }

    /** Recusively Fires span edited, child edited and doc listeners.
     *
     * @see #updateDoc()
     */
    final void fireListeners(){
        /// Fire target edited listeners
        if (editedTarget){
            fire(spanEditedListeners);
            editedTarget = false;
        }

        /// Fire child edited listeners
        if (editedChild){
            fire(childEditedListeners);
            editedChild = false;
        }

        /// Fire doc edited listeners
        fire(docEditedListeners);

        /// Recusive call
        for (Span span: this){
            if (span instanceof SpanNode){
                ((SpanNode<?>)span).fireListeners();
            }
        }
    }

    /** fires a list of listeners
     *
     * @param listeners
     *      firing listeners
     * @see #fireRemoveListeners()
     * @see #fireListeners()
     */
    private void fire(HashSet<Consumer<SpanNode<T>>> listeners){
        for (Consumer<SpanNode<T>> listener: listeners) listener.accept(this);
    }

    /// %Part 3: Get Span ######################################################

    /** Gets a list of children {@link SpanBranch}.
     *
     * @param clazz
     *      span class
     * @return answer
     */
    protected final <T> List<T> getChildren(Class<T> clazz){
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (Span span: this){
            if (clazz.isInstance(span)){
                builder.add(clazz.cast(span));
            }
        }
        return builder.build();
    }

    /** Get the first span if it a subclass of {@code clazz}.
     *
     * @param clazz
     *      span class
     * @return answer
     */
    protected <T extends SpanBranch> Optional<T> spanAtFirst(Class<T> clazz){
        argumentNotNull(clazz, "clazz");

        if (isEmpty()){
            return Optional.empty();
        }
        if (clazz.isInstance(get(0))){
            return Optional.of(clazz.cast(get(0)));
        }
        return Optional.empty();
    }

    /** Find the first span that subclasses of {@code clazz}.
     *
     * @param clazz
     *      span class
     * @return answer
     */
    protected <T extends SpanBranch> Optional<T> spanFromFirst(Class<T> clazz){
        argumentNotNull(clazz, "clazz");

        for(Span span: this){
            if (clazz.isInstance(span)){
                return Optional.of(clazz.cast(span));
            }
        }
        /// Not found or no children
        return Optional.empty();
    }

    /** Get the last span if it a subclass of {@code clazz}.
     *
     * @param clazz
     *      span class
     * @return answer
     */
    protected <T extends SpanBranch> Optional<T> spanAtLast(Class<T> clazz){
        argumentNotNull(clazz, "clazz");
        if (isEmpty()){
            return Optional.empty();
        }
        if (clazz.isInstance(get(size() - 1))){
            return Optional.of(clazz.cast(get(size() - 1)));
        }
        return Optional.empty();
    }

    /** Find the first span that subclasses of {@code clazz}.
     *
     * @param clazz
     *      span class
     * @return answer
     */
    protected <T extends SpanBranch> Optional<T> spanFromLast(Class<T> clazz){
        argumentNotNull(clazz, "clazz");
        for(int i = size() - 1; i >= 0; i--){
            Span span = get(i);
            if (clazz.isInstance(span)){
                return Optional.of(clazz.cast(span));
            }
        }
        /// Not found or no children
        return Optional.empty();
    }

    /** Gets the previous sibling {@link SpanBranch}.
     *
     * @param clazz
     *      span class
     * @return answer
     */
    public final <U extends SpanBranch> Optional<U> spanBefore(Class<U> clazz){
        argumentNotNull(clazz, "clazz");
        int current = getParent().indexOf(this);
        int i = 0;
        for (Span search: getParent()){
            if (i == current){
                return Optional.empty();
            }
            if (clazz.isInstance(search)){
                return Optional.of(clazz.cast(search));
            }
            i++;
        }
        assert false: "span is not a child.";
        return null;

    }

    /** Gets a list of children {@link SpanLeaf}.
     *
     * @param info
     *      leaf info
     * @return answer
     */
    protected final List<SpanLeaf> getChildren(StyleInfoLeaf info){
        ImmutableList.Builder<SpanLeaf> builder = ImmutableList.builder();
        for (Span span: this){
            if (span instanceof SpanLeaf &&
                ((SpanLeaf)span).getLeafStyle() == info
            ){
                builder.add((SpanLeaf) span);
            }
        }
        return builder.build();
    }

    /** Get the fist leaf span if it a has the style of {@code info}.
     *
     * @param info
     *      leaf info
     * @return answer
     */
    protected Optional<SpanLeaf> leafFromFirst(StyleInfoLeaf info){
        argumentNotNull(info, "info");
        for (Span span: this){
            if (span instanceof SpanLeaf){
                SpanLeaf found = (SpanLeaf) span;
                if (found.getLeafStyle().equals(info)){
                    return Optional.of(found);
                }
            }
        }
        /// Not found or no children
        return Optional.empty();
    }

    /** Find the first leaf span that has the style of {@code info}.
     *
     * @param info
     *      leaf info
     * @return answer
     */
    protected Optional<SpanLeaf> leafFromLast(StyleInfoLeaf info){
        argumentNotNull(info, "info");
        for(int i = size() - 1; i >= 0; i++){
            Span span = get(i);
            if (span instanceof SpanLeaf){
                SpanLeaf found = (SpanLeaf) span;
                if (found.getLeafStyle().equals(info)){
                    return Optional.of(found);
                }
            }
        }
        /// Not found or no children
        return Optional.empty();
    }

    /** Get the Span leaf with global position. Uses binary serach.
     *
     * @param pos
     *      leaf position
     * @return answer
     */
    public final Optional<SpanLeaf> getLeaf(int pos){
        indexClose(pos, "pos", 0, getEnd());

        /// Gets the last span leaf
        if (pos == getEnd()){
            Span span = this;
            while (span instanceof SpanNode){
                SpanNode<?> parent = (SpanNode<?>)span;
                if (parent.isEmpty()){
                    return Optional.empty();
                }
                span = parent.get(parent.size() - 1);
            }
            return Optional.of((SpanLeaf) span);
        }

        Range<Integer> range = getRange();
        if (range.contains(pos)){
            /// Binary serach setup
            int low = 0;
            int high = size() - 1;
            int mid;
            Span span;
            while (low <= high){
                /// Get index
                mid = (low + high) / 2;
                span = get(mid);

                Range<Integer> child = span.getRange();
                if (child.lowerEndpoint() > pos){
                    high = mid - 1;
                } else if (child.upperEndpoint() <= pos){
                    low = mid + 1;
                } else {
                    /// Find span, but recurives if leaf not found yet.
                    return  span instanceof SpanLeaf?
                        Optional.of((SpanLeaf) span):
                        ((SpanNode<?>)span).getLeaf(pos);
                }
            }
        }
        /// exception for not found. Somehow the code can end up here.
        throw new IndexOutOfBoundsException(pos + " is not between :" + range);
    }

    /** Gets all the {@link SpanLeaf} found in its descendants.
     *
     * @return answer
     */
    public abstract List<SpanLeaf> getLeaves();

    /// %Part 4: Caches ########################################################

    /** Get local cache for a single object.
     *
     * @param key
     *      cache key
     * @param caller
     *      cache caller
     * @return answer
     */
    protected final <T> T getLocalCache(CacheKeyMain<T> key, Callable<T> caller){
        try {
            return key.cast(spanMainCache.get(key, caller));
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /** Get document cache for a single object.
     *
     * @param key
     *      cache key
     * @param caller
     *      cache caller
     * @return answer
     */
    protected final <T> T getDocCache(CacheKeyMain<T> key, Callable<T> caller){
        try {
            return key.cast(docMainCache.get(key, caller));
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /** Get local cache for a optional object.
     *
     * @param key
     *      cache key
     * @param caller
     *      cache caller
     * @return answer
     */
    protected final <T> Optional<T> getLocalCache(CacheKeyOptional<T> key,
        Callable<Optional<T>> caller
    ){
        try {
            return key.cast(spanOptionalCache.get(key, caller));
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /** Get document cache for a optional object.
     *
     * @param key
     *      cache key
     * @param caller
     *      cache caller
     * @return answer
     */
    protected <T> Optional<T> getDocCache(CacheKeyOptional<T> key,
        Callable<Optional<T>> caller
    ){
        try {
            return key.cast(docOptionalCache.get(key, caller));
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /** Get local cache for a list of objects.
     *
     * @param key
     *      cache key
     * @param caller
     *      cache caller
     * @return answer
     */
    protected final <T> List<T> getLocalCache(CacheKeyList<T> key,
        Callable<List<T>> caller
    ){
        try {
            return key.cast(spanListCache.get(key, caller));
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /** Get document cache for a list of objects.
     *
     * @param key
     *      cache key
     * @param caller
     *      cache caller
     * @return answer
     */
    protected <T> List<T> getDocCache(CacheKeyList<T> key,
        Callable<List<T>> caller
    ){
        try {
            return key.cast(docListCache.get(key, caller));
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /// %Part 5: Listener Add and Remove #######################################

    /** Add span edited listener
     *
     * @param listener
     *      span listener
     */
    public void addSpanEdited(Consumer<SpanNode<T>> listener){
        spanEditedListeners.add(listener);
    }

    /** Remove span edited listener
     *
     * @param listener
     *      span listener
     */
    public void removeSpanEdited(Consumer<SpanNode<T>> listener){
        spanEditedListeners.remove(listener);
    }

    /** Add span removed listener
     *
     * @param listener
     *      span listener
     */
    public void addSpanRemoved(Consumer<SpanNode<T>> listener){
        spanRemovedListeners.add(listener);
    }

    /** Remove span removed listener
     *
     * @param listener
     *      span listener
     */
    public void removeSpanRemoved(Consumer<SpanNode<T>> listener){
        spanRemovedListeners.remove(listener);
    }

    /** Add span's child edited listener
     *
     * @param listener
     *      span listener
     */
    public void addChildEdited(Consumer<SpanNode<T>> listener){
        childEditedListeners.add(listener);
    }

    /** Remove span's child edited listener
     *
     * @param listener
     *      span listener
     */
    public void removeChildEdited(Consumer<SpanNode<T>> listener){
        childEditedListeners.remove(listener);
    }

    /** Add document edited listener.
     *
     * This will called only one more time after this span is removed.
     *
     * @param listener
     *      span listener
     */
    public void addDocEdited(Consumer<SpanNode<T>> listener){
        docEditedListeners.add(listener);
    }

    /** Remove document edited listener.
     *
     * This will called only one more time after this span is removed.
     *
     * @param listener
     *      span listener
     */
    public void removeDocEdited(Consumer<SpanNode<T>> listener){
        docEditedListeners.remove(listener);
    }

    /// %Part 6: Overriding Methods ############################################
    /// %Part 6.1: Span Methods ================================================

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

    /// %Part 6.2: List Methods ================================================

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
