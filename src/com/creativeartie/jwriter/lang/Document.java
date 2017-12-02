package com.creativeartie.jwriter.lang;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.function.Predicate;
import java.util.function.Function;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.cache.*;

import com.creativeartie.jwriter.main.*;
import static com.google.common.base.Preconditions.*;

/**
 * {@code Document} is what {@link com.creativeartie.jwriter.window} and
 * {@link org.community.output} use to represent the text file. This will
 * immediately parse the rawText upon the constructor.
 */
public abstract class Document extends SpanNode<SpanBranch>{

    private final Cache<Span, Range<Integer>> spanRanges;
    private final Cache<Span, List<SpanLeaf>> spanLeaves;

    private ArrayList<SpanBranch> documentChildren;
    private CatalogueMap catalogueMap;
    private SetupParser[] documentParsers;

    protected Document(String raw, SetupParser ... parsers){
        Checker.checkNotNull(raw, "raw");
        Checker.checkNotNull(parsers, "parsers");

        spanRanges = CacheBuilder.newBuilder().weakKeys().build();
        spanLeaves = CacheBuilder.newBuilder().weakKeys().build();
        documentParsers = parsers;

        /// Setup for building the doc and a pointer to use
        parseDocument(raw);
    }

    protected final void parseDocument(String raw){
        Checker.checkNotNull(raw, "raw");

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
        updateMap(documentChildren);
    }

    private final void updateMap(List<? extends Span> children){
        children.stream().map((child) -> {
            if (child instanceof SpanBranch){
                SpanBranch branch = (SpanBranch) child;
                if (branch instanceof Catalogued){
                    Catalogued catalogued = (Catalogued) branch;
                    Optional<CatalogueIdentity> id = catalogued
                            .getSpanIdentity();
                    id.ifPresent(found -> {
                        if(catalogued.isId()){
                            catalogueMap.addId(found, branch);
                        } else {
                            catalogueMap.addRef(found, branch);
                        }
                    });
                }
                updateMap(branch);
            }
            return child;
        }).forEachOrdered((child) -> {
            child.docEdited();
        });
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
    public final void setRemove(){
        documentChildren.forEach(child ->  child.setRemove());
    }

    @Override
    public final Range<Integer> getRange(){
        return getRangeCache(this, () -> Range.closedOpen(0, getLength() + 1));
    }

    final Range<Integer> getRangeCache(Span child,
        Callable<Range<Integer>> caller)
    {
        checkNotNull(child, "Child span cannot be null.");
        checkNotNull(caller, "Caller function cannot be null.");

        try {
            return spanRanges.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    final List<SpanLeaf> getLeavesCache(SpanBranch child,
        Callable<List<SpanLeaf>> caller)
    {
        checkNotNull(child, "Child span cannot be null.");
        checkNotNull(caller, "Caller function cannot be null.");

        try {
            return spanLeaves.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public final List<SpanLeaf> getLeaves(){
        ImmutableList.Builder<SpanLeaf> builder = ImmutableList.builder();
        for(SpanBranch span: this){
            builder.addAll(span.getLeaves());
        }
        return builder.build();
    }

    public <T> Optional<T> locateSpan(int index, Class<T> clazz){
        checkPositionIndex(index, getLength(), "Index is out of range.");
        checkNotNull(clazz, "Caller function cannot be null.");
        if(getLength() == 0){
            return Optional.empty();
        }
        SpanNode<?> pointer = this;
        do {
            Span found = locateSpan(index, pointer);
            if (found instanceof SpanLeaf){
                return Optional.empty();
            } else if (clazz.isInstance(found)){
                return Optional.of(clazz.cast(found));
            }
            pointer = (SpanNode<?>) found;
        } while(true);

    }

    public SpanLeaf getLeaf(int index){
        checkPositionIndex(index, getLength(), "Index is out of range.");
        Span found = locateSpan(index, this);
        while (! (found instanceof SpanLeaf)) {
            found = locateSpan(index, (SpanNode<?>)found);
        }
        return (SpanLeaf) found;
    }

    private Span locateSpan(int index, SpanNode<?> parent){
        assert parent != null: "Null parent";
        for (Span span: parent){
            if (span.getRange().contains(index)){
                return span;
            }
        }
        return parent.get(parent.size() - 1);
    }

    public void insert(int location, String input){
        if (location == getLength()){
            SpanNode<?> span = getLeaf(location).getParent();
            while (span instanceof SpanBranch){
                if (((SpanBranch)span).editRaw(span.getRaw() + input)){
                    break;
                }
                span = span.getParent();
            }
            if (span instanceof Document){
                parseDocument(getRaw() + input);
            }
            updateAll();
        } else {
            edit(span -> {
                StringBuilder text = new StringBuilder(span.getRaw());
                text.insert(location - span.getStart(), input);
                return text.toString();
            }, location);
        }
    }

    public void delete(int start, int end){
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

    private void edit(Function<Span, String> editedText, int location){
        SpanNode<?> span = getLeaf(location).getParent();
        if (location != getLength()){
            while (span instanceof SpanBranch){
                String raw = editedText.apply(span);
                if (! raw.isEmpty()){
                    if (((SpanBranch)span).editRaw(raw)){
                        break;
                    }
                }
                span = span.getParent();
            }
        }
        if (span instanceof Document){
            parseDocument(editedText.apply(this));
        }
        updateAll();
    }

    private void updateAll(){
        spanRanges.invalidateAll();
        spanLeaves.invalidateAll();
        catalogueMap = new CatalogueMap();
        updateMap(this);
    }
}
