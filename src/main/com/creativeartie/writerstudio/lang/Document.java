package com.creativeartie.writerstudio.lang;

import java.util.*; // (many)
import java.util.concurrent.*; // Callabl, ExecutionException;
import java.util.function.*; // Function;

import com.google.common.cache.*; // Cache, CacheBuilder, LoadingCache;
import com.google.common.collect.*; // ImmmuableList, Range;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Representation of a text file that is parsed by {@link SetupParser}.
 */
public abstract class Document extends SpanNode<SpanBranch>{

    /// %Part 1: Constructors ##################################################

    private SetupParser[] documentParsers;
    private ArrayList<SpanNode<?>> removeSpan;

    private final Cache<Span, Range<Integer>> spanRanges;
    private final Cache<Span, List<SpanLeaf>> spanLeaves;
    private final Cache<Span, String> spanTexts;
    /// values = Integer[]{column, line}
    private final LoadingCache<Integer, Integer[]> spanLocation;

    private final CatalogueMap catalogueMap;
    private ArrayList<SpanBranch> documentChildren;

    /** Creates a {@linkplain Document}.
     *
     * @param raw
     *      raw text
     * @param parser
     *      text parsers
     */
    protected Document(String raw, SetupParser ... parsers){
        argumentNotNull(raw, "raw");
        documentParsers = argumentNotEmpty(parsers, "parser");
        removeSpan = new ArrayList<>();

        spanRanges = CacheBuilder.newBuilder().weakKeys().build();
        spanLeaves = CacheBuilder.newBuilder().weakKeys().build();
        spanTexts = CacheBuilder.newBuilder().weakKeys().build();
        spanLocation = CacheBuilder.newBuilder().maximumSize(1000)
            .build(CacheLoader.from(pos ->{
                int column = 0;
                int line = 1;
                String input = getRaw();

                for (int i = 0; i < pos; i++){
                    if (input.charAt(i) == '\n'){
                        /// New line
                        column = 0;
                        line++;
                    } else {
                        /// Same line
                        column++;
                    }
                }

                return new Integer[]{column, line};
            }));

        catalogueMap = new CatalogueMap();
        parseDocument(raw); /// Sets spanChildren

        /// load catalogue map
        catalogueMap.clear();
        loadMap(documentChildren);
    }

    /// %Part 2: Updating and Editing Document #################################

    /// %Part 2.1: Main Editing Functions ######################################

    @Override
    protected final void runCommand(Command command){
        reparseDocument(command.getResult());
    }

    /** Insert a {@linkplain String} at a location.
     *
     * @param index
     *      insert index
     * @param input
     *      input text
     */
    public synchronized final void insert(int index, String input){
        argumentOpen(index, "index", 0, getEnd());
        argumentNotNull(input, "input");

        /// Insert into empty doc
        if (isEmpty()){
            reparseDocument(input);
            return;
        }

        /// Insert at the end
        if (index == getLocalEnd()){
            /// Gets the last span leaf's parent
            Span span = locatLeaf(size() - 1).get();
            assert span instanceof SpanLeaf: "Wrong class.";
            span = span.getParent();
            /// try to parse at SpanBranch
            while (span instanceof SpanBranch){
                if (((SpanBranch)span).editRaw(span.getRaw() + input)){
                    return;
                }
                span = span.getParent();
            }
            /// Reparse the whole document
            assert span instanceof Document: "Wrong class.";
            reparseDocument(getRaw() + input);
            return;
        }

        /// Insert at start & middle
        edit(span -> {
            StringBuilder text = new StringBuilder(span.getRaw());
            text.insert(index - span.getStart(), input);
            return text.toString();
        }, index);
    }

    /** Delete a {@linkplain String} at a location.
     *
     * @param start
     *      start index
     * @param end
     *      end index
     */
    public synchronized final void delete(int start, int end){
        checkRange(end, "end", 0, true, getEnd(), true);
        checkRange(start, "start", 0, true, end, true);

        edit(span -> {
            if (span.getEnd() >= end){
                String text = span.getRaw();
                text = text.substring(0,start - span.getStart()) +
                    text.substring(end - span.getStart(), text.length());
                return text.toString();
            }
            return "";
        }, start);
    }

    /** Edit the document, excluding adding to the end of it.
     *
     * @param supplier
     *      span text supplier
     * @param location
     *      replace location
     * @see #insert(int, String)
     * @see #delete(int, int)
     */
    private void edit(Function<Span, String> supplier, int location){
        assert supplier != null: "Null supplier";
        assert location >= 0 && location <= getEnd(): "Wrong location";

        /// gets the span leaf's parent
        Optional<SpanLeaf> found = getLeaf(location);
        if (! found.isPresent()){
            reparseDocument(supplier.apply(this));
            return;
        }
        SpanNode<?> span = found.get().getParent();

        /// Attempt to parse at a SpanBranch level
        while (span instanceof SpanBranch){
            String raw = supplier.apply(span);
            if (! raw.isEmpty()){
                /// edit is within the local text
                if (((SpanBranch)span).editRaw(raw)){
                    /// edit is completed
                    return;
                }
            }
            span = span.getParent();
        }

        /// Must be parse at Document level
        assert span instanceof Document: "Wrong class:" + span.getClass();
        reparseDocument(supplier.apply(this));
        return;
    }

    /** Reparse the whole document.
     *
     * This will also called the updates methods.
     *
     * @param text
     *      new text
     * @see #runCommand(Command)
     * @see #insert(int, String)
     * @see #delete(int, int)
     */
    private void reparseDocument(String text){
        forEach(s -> s.setRemove());
        parseDocument(text);
        updateSpan();
        updateDoc();
    }

    /** Parse the whole document.
     *
     * @param text
     *      new text
     * @see #Document(String, SetupParser ...)
     * @see #reparseDocuemnt(String)
     */
    private final void parseDocument(String raw){
        assert raw != null: "Null raw.";

        documentChildren = new ArrayList<>();
        SetupPointer ptr = SetupPointer.newPointer(raw, this);
        int counter = 0; /// Setup for runtime exceptions

        /// Parse loop
        while (ptr.hasNext()){
            /// One small check to see if it loop to much
            if(counter > raw.length()){
                throw new RuntimeException("Loop too much: " + ptr);
            }
            counter++; /// != ptr location

            /// Finding the correct SetupParser to build span from
            for(SetupParser s: documentParsers){
                Optional<?> span = s.parse(ptr);
                /// Span has been created
                if (span.isPresent()){
                    SpanBranch found = (SpanBranch)span.get();
                    found.setParent(this);
                    documentChildren.add(found);
                    break;
                }
            }
        }
    }

    /// %Part 2.2: Update system

    @Override
    protected final void setChildren(List<SpanBranch> children){
        children.forEach(s -> s.setParent(this));
        documentChildren = new ArrayList<>(children);
    }

    final void removeSpan(SpanNode<?> span){
        removeSpan.add(span);
    }

    final void updateDoc(){
        /// clear caches and data
        spanRanges.invalidateAll();
        spanLeaves.invalidateAll();
        spanTexts.invalidateAll();
        clearCache();
        for (SpanBranch span: this){
            span.clearCache();
        }

        catalogueMap.clear();
        loadMap(this);

        for (SpanNode<?> span: removeSpan){
            span.fireRemoveListeners();
        }
        removeSpan.clear();
        fireListeners();
    }


    /**
     * Recursively update all child {@link Span spans}. Helper method of
     * {@link #parseDocument(String)} and {@link #updateEdit()}.
     */
    private final void loadMap(List<? extends Span> children){
        assert children != null: "Null children";
        for (Span child: children){
            /// Fill or refill {@link #catalogueMap}
            if (child instanceof SpanBranch){
                SpanBranch branch = (SpanBranch) child;
                catalogueMap.add(branch);
                loadMap(branch);
            }
        }
    }

    public final CatalogueMap getCatalogue(){
        return catalogueMap;
    }

    @Override
    public final Document getDocument(){
        return this;
    }
    @Override
    public final Range<Integer> getRange(){
        return getRangeCache(this, () -> Range.closedOpen(0, getLocalEnd()));
    }

    /**
     * Get a range in cache and wraps {@linkplain ExecutionException} with
     * {@link RuntimeException}.
     */
    final Range<Integer> getRangeCache(Span child,
            Callable<Range<Integer>> caller) {
        checkNotNull(child, "child");
        checkNotNull(caller, "caller function (caller)");

        try {
            return spanRanges.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    /**
     * Get a text in cache and wraps {@linkplain ExecutionException} with
     * {@link RuntimeException}.
     */
    final String getTextCache(Span child, Callable<String> caller) {
        checkNotNull(child, "child");
        checkNotNull(caller, "caller");

        try {
            return spanTexts.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    /**
     * Get a leave list in cache and wraps {@linkplain ExecutionException} with
     * {@link RuntimeException}.
     */
    final List<SpanLeaf> getLeavesCache(Span child,
            Callable<List<SpanLeaf>> caller){
        checkNotNull(child, "child");
        checkNotNull(caller, "caller");

        try {
            return spanLeaves.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    @Override
    public final List<SpanLeaf> getLeaves(){
        return getLeavesCache(this, () ->{
            ImmutableList.Builder<SpanLeaf> builder = ImmutableList.builder();
            for(SpanBranch span: this){
                /// span.getLeaves() might be cached, reduces the need to search
                builder.addAll(span.getLeaves());
            }
            return builder.build();
        });
    }

    /** Find column index. */
    final int getColumn(int index){
        try {
            return spanLocation.get(index)[0];
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /** Find line index. */
    final int getLine(int index){
        try {
            return spanLocation.get(index)[1];
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /** Locate a {@link Span} that is a instance of a certain class  */
    public final <T> Optional<T> locateSpan(int index, Class<T> clazz){
        checkRange(index, "index", 0, true, getEnd(), true);
        checkNotNull(clazz, "requested class (clazz).");

        /// Empty document
        if(getLocalEnd() == 0){
            return Optional.empty();
        }

        SpanNode<?> pointer = this;
        do {
            Span found = locateSpan(index, pointer);
            if (found instanceof SpanLeaf){
                /// Nothing found:
                return Optional.empty();
            } else if (clazz.isInstance(found)){
                /// Found and matched reqrested class
                return Optional.of(clazz.cast(found));
            }
            assert found instanceof SpanNode: "Wrong class: " + found.getClass();
            pointer = (SpanNode<?>) found;
        } while(true);

    }

    /** Locate a {@link SpanLeaf} */
     public final Optional<SpanLeaf> locateLeaf(int index){
        checkRange(index, "index", 0, true, getEnd(), true);
        if(getLocalEnd() == 0){
            return Optional.empty();
        }
        Span pointer = this;
        while (pointer instanceof SpanNode<?>){
            pointer = locateSpan(index, (SpanNode<?>) pointer);
        }
        return Optional.of((SpanLeaf)pointer);
    }

    /**
     * Located the span in a {@link SpanNode}. Helper method of
     * {@link #locateSapn(int, Class)}, and {@link #getLeaf(int)}.
     */
    private final Span locateSpan(int index, SpanNode<?> parent){
        assert parent != null: "Null parent";
        for (Span span: parent){
            if (span.getRange().contains(index)){
                return span;
            }
        }
        return parent.get(parent.size() - 1);
    }

    public void addReferences(Document ... docs){
        for (Document doc: docs){
            CatalogueMap map = doc.catalogueMap;
            // TODO work on span branches (no more editing will be doen to them)
            // TODO remove id references (not applicable)
            // TODO merge ids
            catalogueMap.add(map);
        }
    }

    @Override
    public final SpanNode<?> getParent(){
        throw new UnsupportedOperationException("No parents");
    }

    @Override
    public final void setParent(){
        throw new UnsupportedOperationException("No parents can be set");
    }

    @Override
    public final List<SpanBranch> delegate(){
        return ImmutableList.copyOf(documentChildren);
    }
}
