package com.creativeartie.jwriter.lang;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import com.google.common.collect.*;
import com.google.common.cache.*;

import static com.google.common.base.Preconditions.*;

/**
 * Representation of a text file that is parsed by {@link SetupParser}.
 */
public abstract class Document extends SpanNode<SpanBranch>{

    /// Caches to reduce the need to recalculate data for each span.
    private final Cache<Span, Range<Integer>> spanRanges;
    private final Cache<Span, List<SpanLeaf>> spanLeaves;

    private ArrayList<SpanBranch> documentChildren;
    private CatalogueMap catalogueMap;
    private SetupParser[] documentParsers;

    protected Document(String raw, SetupParser ... parsers){
        checkNotNull(raw, "Raw text can not be empty");
        checkNotNull(parsers, "Parser can not be empty");

        spanRanges = CacheBuilder.newBuilder().weakKeys().build();
        spanLeaves = CacheBuilder.newBuilder().weakKeys().build();
        documentParsers = parsers;

        /// Setup for building the doc and a pointer to use
        parseDocument(raw);
    }

    /**
     * Parses the document. Helper method of
     * {@link #Document(String, SetupParsers...)}, {@link #insert(int, String},
     * and {@link #edit(Function, int}.
     */
    private final void parseDocument(String raw){
        assert raw != null: "Null raw.";

        documentChildren = new ArrayList<>();
        SetupPointer ptr = SetupPointer.newPointer(raw, this);
        /// Setup for runtime exceptions
        int counter = 0;

        /// Parse loop
        while (ptr.hasNext()){
            /// CatalogueStatus checking what happen if SetupPointer fails or
            /// SetupParser[] misses the texts
            if(counter > raw.length()){
                System.out.println(documentChildren);
                System.out.println(ptr);
                throw new RuntimeException("Loop too much");
            }
            counter++;

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

        /// Finalize the parse loop
        catalogueMap = new CatalogueMap();
        updateSpan(documentChildren);
    }

    /** Recursively update all child {@link Span spans}. */
    private final void updateSpan(List<? extends Span> children){
        for (Span child: children){
            /// Fill or refill {@link #catalogueMap}
            if (child instanceof SpanBranch){
                SpanBranch branch = (SpanBranch) child;
                if (branch instanceof Catalogued){
                    Catalogued catalogued = (Catalogued) branch;
                    Optional<CatalogueIdentity> id = catalogued
                            .getSpanIdentity();
                    /// Don't add ID if there isn't one
                    id.ifPresent(found -> {
                        if(catalogued.isId()){
                            catalogueMap.addId(found, branch);
                        } else {
                            catalogueMap.addRef(found, branch);
                        }
                    });
                }
                updateSpan(branch);
            }

            /// Tell the child that the document has been updated.
            child.docEdited();
        }
    }

    public final CatalogueMap getCatalogue(){
        return catalogueMap;
    }

    @Override
    public String toString(){
        if (catalogueMap.isEmpty()){
            return super.toString() + catalogueMap.toString();
        }
        return super.toString() + "\n" + catalogueMap.toString();
    }

    @Override
    public final Document getDocument(){
        return this;
    }

    @Override
    public final SpanNode<?> getParent(){
        throw new UnsupportedOperationException("No parents");
    }

    @Override
    public final List<SpanBranch> delegate(){
        return ImmutableList.copyOf(documentChildren);
    }

    @Override
    public final Range<Integer> getRange(){
        return getRangeCache(this, () -> Range.closedOpen(0, getLength() + 1));
    }

    /**
     * Get a range in cache and wraps {@linkplain ExecutionException} with
     * {@link RuntimeException}.
     */
    final Range<Integer> getRangeCache(Span child,
            Callable<Range<Integer>> caller) {
        checkNotNull(child, "Child span cannot be null.");
        checkNotNull(caller, "Caller function cannot be null.");

        try {
            return spanRanges.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get a leave list in cache and wraps {@linkplain ExecutionException} with
     * {@link RuntimeException}.
     */
    final List<SpanLeaf> getLeavesCache(Span child,
            Callable<List<SpanLeaf>> caller){
        checkNotNull(child, "Child span cannot be null.");
        checkNotNull(caller, "Caller function cannot be null.");

        try {
            return spanLeaves.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public final List<SpanLeaf> getLeaves(){
        return getLeavesCache(this, () ->{
            ImmutableList.Builder<SpanLeaf> builder = ImmutableList.builder();
            for(SpanBranch span: this){
                builder.addAll(span.getLeaves());
            }
            return builder.build();
        });
    }

    /** Locate a {@link Span} that is a instance of a certain class  */
    public final <T> Optional<T> locateSpan(int index, Class<T> clazz){
        checkPositionIndex(index, getLength(), "Char index is out of range.");
        checkNotNull(clazz, "Requested class cannot be null.");

        /// Empty document
        if(getLength() == 0){
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

    /**
     * Locate a {@link SpanLeaf} at a char position. Helper method of
     * {@link #locate(int, String)}, and {@link #edit(Function, int)}.
     */
    public final SpanLeaf getLeaf(int index){
        checkPositionIndex(index, getLength(), "Index is out of range.");

        Span found = locateSpan(index, this);
        while (! (found instanceof SpanLeaf)) {
            found = locateSpan(index, (SpanNode<?>)found);
        }
        assert found instanceof SpanLeaf: "Wrong class: " + found.getClass();
        return (SpanLeaf) found;
    }

    /**
     * Located the span in a {@link SpanNode}. Helper method of
     * {@link #locateSapn(int, Class)}, {@link #getLeaf(int)}.
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

    /** Insert a {@linkplain String} at a location.*/
    public final void insert(int location, String input){
        checkPositionIndex(location, getLength() + 1, "Index is out of range.");
        checkNotNull(input, "Input string cannot be null.");

        if (location == getLength()){
            /// Insert at the end
            SpanNode<?> span = getLeaf(location).getParent();
            while (span instanceof SpanBranch){
                if (((SpanBranch)span).editRaw(span.getRaw() + input)){
                    span = null;
                    break;
                }
                span = span.getParent();
            }
            /// Reparse the whole document
            if (span != null){
                assert span instanceof Document: "Wrong class:" + span.getClass();
                parseDocument(getRaw() + input);
                setUpdated();
            }
            updateEdit();
        } else {
            /// Insert in the begining at in the middle
            edit(span -> {
                StringBuilder text = new StringBuilder(span.getRaw());
                text.insert(location - span.getStart(), input);
                return text.toString();
            }, location);
        }
    }

    /** Insert a {@linkplain String} at a location.*/
    public final void delete(int start, int end){
        checkPositionIndexes(start, end, getEnd());

        edit(span -> {
            if (span.getEnd() > end){
                String text = span.getRaw();
                text = text.substring(0,start - span.getStart()) +
                    text.substring(end - span.getStart(), text.length());
                return text.toString();
            }
            return "";
        }, start);
    }

    /**
     * Edit the document, excluding adding to the end of it. Helper function for
     * {@link #insert(int, String)}, and {@link #delete(int, int)}.
     */
    private final void edit(Function<Span, String> editedText, int location){
        SpanNode<?> span = getLeaf(location).getParent();

        /// Attempt to parse at a SpanBranch level
        while (span instanceof SpanBranch){
            String raw = editedText.apply(span);
            if (! raw.isEmpty()){
                /// edit is within the local text
                if (((SpanBranch)span).editRaw(raw)){
                    /// edit is completed
                    span = null;
                    break;
                }
            }
            span = span.getParent();
        }

        /// Must be parse at Document level
        if (span != null){
            assert span instanceof Document: "Wrong class:" + span.getClass();
            parseDocument(editedText.apply(this));
            setUpdated();
        }
        updateEdit();
    }

    /** Update the document after editing. Helper method of
     * {@link #insert(int, String)}, and {@link #edit(Function, int)}.
     */
    private final void updateEdit(){
        spanRanges.invalidateAll();
        spanLeaves.invalidateAll();
        catalogueMap = new CatalogueMap();
        updateSpan(this);
    }
}
