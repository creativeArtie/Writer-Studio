package com.creativeartie.writer.lang;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import com.google.common.cache.*;
import com.google.common.collect.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

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
 * @param <S>
 *      Type of span stored
 */
public abstract class SpanNode<S extends Span> extends Span implements List<S>{

    /// %Part 1: Constructor & Fields ##########################################

    private final Cache<CacheKeyMain<?>, Object> spanMainCache;
    private final Cache<CacheKeyOptional<?>, Optional<?>> spanOptionalCache;
    private final Cache<CacheKeyList<?>, List<?>> spanListCache;

    private final Cache<CacheKeyMain<?>, Object> docMainCache;
    private final Cache<CacheKeyOptional<?>, Optional<?>> docOptionalCache;
    private final Cache<CacheKeyList<?>, List<?>> docListCache;

    private final HashSet<Consumer<SpanNode<S>>> childEditedListeners;
    private final HashSet<Consumer<SpanNode<S>>> spanRemovedListeners;
    private final HashSet<Consumer<SpanNode<S>>> spanEditedListeners;
    private final HashSet<Consumer<SpanNode<S>>> docEditedListeners;
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

    /** Insert a child span at the end.
     *
     * @param parser
     *      span branch parser
     * @param text
     *      parsing text
     */
    protected synchronized void addChild(SetupParser parser, String text){
        addChild(parser, text, size());
    }

    /** Insert a child span at the end
     *
     * @param parser
     *      span branch parser
     * @param text
     *      parsing text
     * @see addChild(SetupParser, String)
     */
    protected synchronized void addChild(
        SetupParser parser, String text, int position
    ){
        boolean first = position == 0 && isDocumentFirst();
        SetupPointer pointer = SetupPointer.updatePointer(text,
            getDocument(), first);
        Optional<SpanBranch> span = parser.parse(pointer);
        stateCheck(! pointer.hasNext() && span.isPresent(),
            "Has left over characters when reparsing: " +
            getClass().getSimpleName()
        );
        addChild(span.get(), position);
        updateSpan();
    }

    /** Adds the child into the parent.
     *
     * @param span
     *      adding span
     * @param position
     *      position to add
     */
    abstract void addChild(SpanBranch span, int position);

    /** Update the spans and their parents
     *
     * @param spans
     *      new children spans
     * @see #SpanBranch#reparseText(String, SetupParser)
     */
    final void updateSpan(List<S> spans){
        setChildren(spans);
        setEdited();
        getParent().updateParent();
    }

    /** Update this span and it's parents
     *
     * @see #SpanBranch#reparseText(String, SetupParser)
     */
    final void updateSpan(){
        setEdited();
        if (this instanceof Document){
            ((Document)this).updateDoc();
        } else {
            getParent().updateParent();
        }
    }

    abstract void removeChild(int index);

    /** Set the span children
     *
     * @param spans
     *      new children spans
     * @see SpanBranch#SpanBranch(List)
     * @see #updateSpan(List)
     */
    abstract void setChildren(List<S> spans);

    /** Recrusively update the parent or document span.
     *
     * @see #updateSpan(List);
     */
    final void updateParent(){
        clearSpanCache();
        editedChild = true;
        if (this instanceof Document){
            ((Document)this).updateDoc();
        } else {
            getParent().updateParent();
        }
    }

    /** Set the span it's children to remove.
     *
     * @see Document#reparseDocument(String)
     * @see SpanBranch#setChildren(List)
     */
    final void setRemove(){
        getDocument().removeSpan(this);
        for (S child: this){
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
    private final void fire(HashSet<Consumer<SpanNode<S>>> listeners){
        for (Consumer<SpanNode<S>> listener: listeners) listener.accept(this);
    }

    /// %Part 3: Get Span ######################################################

    /** Gets a list of children {@link SpanBranch}.
     *
     * @param clazz
     *      span class
     * @return answer
     */
    public final <T> List<T> getChildren(Class<T> clazz){
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
    public final <T extends SpanBranch> Optional<T> spanAtFirst(Class<T> clazz){
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
    public final <T extends SpanBranch> Optional<T> spanFromFirst(Class<T> clazz){
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
    public final <T extends SpanBranch> Optional<T> spanAtLast(Class<T> clazz){
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
    public final <T extends SpanBranch> Optional<T> spanFromLast(Class<T> clazz){
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
    public final List<SpanLeaf> getChildren(SpanLeafStyle info){
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
    public final Optional<SpanLeaf> leafFromFirst(SpanLeafStyle info){
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
    public final Optional<SpanLeaf> leafFromLast(SpanLeafStyle info){
        argumentNotNull(info, "info");
        for(int i = size() - 1; i >= 0; i--){
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

    /** Gets a {@link SpanLeaf} at the location defined by a function
     *
     * @param type
     *      type indexing function
     * @param location
     *      the location according by the function
     * @return answer
     */
    protected final Optional<SpanLeaf> locateLeaf(ToIntFunction<SpanLeaf> type,
        int location
    ){
        if (isEmpty()){
            return Optional.empty();
        }
        int ptr = 0;
        for (SpanLeaf leaf: getLeaves()){
            int length = leaf.getLength(type);
            if (length + ptr < location){
                ptr += length;
            } else {
                return Optional.of(leaf);
            }
        }
        return Optional.empty();
    }

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
    public void addSpanEdited(Consumer<SpanNode<S>> listener){
        spanEditedListeners.add(listener);
    }

    /** Remove span edited listener
     *
     * @param listener
     *      span listener
     */
    public void removeSpanEdited(Consumer<SpanNode<S>> listener){
        spanEditedListeners.remove(listener);
    }

    /** Add span removed listener
     *
     * @param listener
     *      span listener
     */
    public void addSpanRemoved(Consumer<SpanNode<S>> listener){
        spanRemovedListeners.add(listener);
    }

    /** Remove span removed listener
     *
     * @param listener
     *      span listener
     */
    public void removeSpanRemoved(Consumer<SpanNode<S>> listener){
        spanRemovedListeners.remove(listener);
    }

    /** Add span's child edited listener
     *
     * @param listener
     *      span listener
     */
    public void addChildEdited(Consumer<SpanNode<S>> listener){
        childEditedListeners.add(listener);
    }

    /** Remove span's child edited listener
     *
     * @param listener
     *      span listener
     */
    public void removeChildEdited(Consumer<SpanNode<S>> listener){
        childEditedListeners.remove(listener);
    }

    /** Add document edited listener.
     *
     * This will called only one more time after this span is removed.
     *
     * @param listener
     *      span listener
     */
    public void addDocEdited(Consumer<SpanNode<S>> listener){
        docEditedListeners.add(listener);
    }

    /** Remove document edited listener.
     *
     * This will called only one more time after this span is removed.
     *
     * @param listener
     *      span listener
     */
    public void removeDocEdited(Consumer<SpanNode<S>> listener){
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
    public abstract List<S> delegate();

    @Override
    public boolean add(S e){
        return delegate().add(e);
    }

    @Override
    public void add(int index, S element){
        delegate().add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends S> c){
        return delegate().addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends S> c){
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
    public S get(int index){
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
    public Iterator<S> iterator(){
        return delegate().iterator();
    }

    @Override
    public int lastIndexOf(Object o){
        return delegate().lastIndexOf(o);
    }

    @Override
    public ListIterator<S> listIterator(){
        return delegate().listIterator();
    }

    @Override
    public ListIterator<S> listIterator(int index){
        return delegate().listIterator(index);
    }

    @Override
    public S remove(int index){
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
    public void replaceAll(UnaryOperator<S> operator){
        delegate().replaceAll(operator);
    }

    @Override
    public boolean retainAll(Collection<?> c){
        return delegate().retainAll(c);
    }

    @Override
    public S set(int index, S element){
        return delegate().set(index, element);
    }

    @Override
    public int size(){
        return delegate().size();
    }

    @Override
    public void sort(Comparator<? super S> c){
        delegate().sort(c);
    }

    @Override
    public Spliterator<S> spliterator(){
        return delegate().spliterator();
    }

    @Override
    public List<S> subList(int fromIndex, int toIndex){
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
