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

    protected Document(){
        documentChildren = new ArrayList<>();
        catalogueMap = new CatalogueMap();
        spanRanges = CacheBuilder.newBuilder().weakKeys().build();
        spanLeaves = CacheBuilder.newBuilder().weakKeys().build();
    }

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
        update(documentChildren);
    }

    private void update(List<? extends Span> children){
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
                update(branch);
            }
            return child;
        }).forEachOrdered((child) -> {
            child.invalidateCache();
        });
    }

    public CatalogueMap getCatalogue(){
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
    public Document getDocument(){
        return this;
    }

    @Override
    public SpanNode<?> getParent(){
        throw new UnsupportedOperationException("No parents");
    }

    @Override
    public List<SpanBranch> delegate(){
        return ImmutableList.copyOf(documentChildren);
    }

    @Override
    Span removeChild(int index){
        Preconditions.checkPositionIndex(index, documentChildren.size());
        Span ans = documentChildren.remove(index);
        ans.setRemove();
        return ans;
    }

    @Override
    void addChildren(int indexes, List<Span> spans){
        Checker.checkNotNull(spans, "spans");
        Preconditions.checkPositionIndex(indexes, documentChildren.size());


        ArrayList<SpanBranch> insertChar = new ArrayList<>();
        for(Span child: spans){
            if ( !(child instanceof SpanBranch)){
                throw new IllegalArgumentException(
                    "Child is not a brench:" + child);
            }
            insertChar.add((SpanBranch)child);
            ((SpanBranch)child).setParent(this);
        }
        documentChildren.addAll(indexes, insertChar);
    }

    @Override
    public void setRemove(){
        documentChildren.forEach(child ->  child.setRemove());
    }

    @Override
    public Range<Integer> getRange(){
        return getRange(this, () -> Range.closedOpen(0, getLength() + 1));
    }

    protected Range<Integer> getRange(Span child, Callable<Range<Integer>> caller)
    {
        Checker.checkNotNull(child, "child");
        Checker.checkNotNull(caller, "caller");
        try {
            return spanRanges.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    List<SpanLeaf> getLeaves(SpanBranch child, Callable<List<SpanLeaf>> caller){
        Checker.checkNotNull(child, "branch");
        Checker.checkNotNull(caller, "caller");
        try {
            return spanLeaves.get(child, caller);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<SpanLeaf> getLeaves(){
        return ImmutableList.copyOf(getLeaves(this));
    }

    private List<SpanLeaf> getLeaves(SpanNode<?> spans){
        ImmutableList.Builder<SpanLeaf> builder = ImmutableList.builder();
        spans.forEach((span) -> {
            if (span instanceof SpanLeaf){
                builder.add((SpanLeaf) span);
            } else {
                builder.addAll(getLeaves((SpanBranch)span));
            }
        });
        return builder.build();
    }

    public <T> Optional<T> spansAt(int index, Class<T> clazz){
        Preconditions.checkPositionIndex(index, getLength(), "Doc index");
        Checker.checkNotNull(clazz, "class");
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

    private Span locateSpan(int index, SpanNode<?> parent){
        for (Span span: parent){
            if (span.getRange().contains(index)){
                return span;
            }
        }
        return parent.get(parent.size() - 1);
    }

    public List<Span> spansAt(int index){
        Preconditions.checkPositionIndex(index, getLength(), "Doc index");
        if(getLength() == 0){
            return ImmutableList.of();
        }
        ArrayList<Span> ans = new ArrayList<>();
        listSpans(ans, index);
        return ImmutableList.copyOf(ans);
    }

    private int listSpans(ArrayList<Span> spans, int index){
        spans.add(this);
        int length = getLength();
        if (index == length){
            Span search = this.get(size() - 1);
            while (search instanceof SpanNode){
                spans.add(search);
                SpanNode<? extends Span> parent = (SpanNode<? extends Span>)
                    search;
                search = parent.get(parent.size() - 1);
            }
            spans.add(search);
            return search.getLength();
        }

        int start = 0;
        Iterator<? extends Span> it = iterator();
        while (true){
            if (! it.hasNext()){
                throw new IllegalArgumentException("Out of range.");
            }
            Span found = it.next();
            if (start + found.getLength() > index){
                spans.add(found);
                if (found instanceof SpanBranch){
                    it = ((SpanBranch)found).iterator();
                } else {
                    return index - start;
                }
            } else {
                start += found.getLength();
            }
        }
    }

    private void edit(Function<Span, String> editedText, int location)
    {
        boolean isDone = false;
        if (location != getLength()){
            for (SpanBranch span: listSpans(location)){
                String raw = editedText.apply(span);
                if (! raw.isEmpty()){
                    isDone = span.editRaw(raw);
                }
                if (isDone){
                    break;
                }
            }
        }

        if (isDone){
            catalogueMap = new CatalogueMap();
            update(this);
            spanRanges.invalidateAll();
        } else {
            parseDocument(editedText.apply(this));
            spanRanges.invalidateAll();
        }
    }


    public void insert(int location, String input){
        edit(span -> {
                StringBuilder text = new StringBuilder(span.getRaw());
                text.insert(location - span.getStart(), input);
                return text.toString();
            }, location);
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

    @SuppressWarnings("unchecked")
    private List<SpanBranch> listSpans(int location){
        List<SpanBranch> ans = new ArrayList<>();
        Iterator<Span> it = ((SpanNode) this).iterator();
        while(it.hasNext()){
            Span found = it.next();
            if (found.getRange().contains(location)){
                if (found instanceof SpanBranch){
                    ans.add(0, (SpanBranch) found);
                    it = ((SpanBranch) found).iterator();
                } else {
                    return ans;
                }
            }
        }
        assert false: "Span Leaf not found.";
        return ans;
    }

    /* // TODO Speed up preformance by edit only some of the text
    public void insertChar(int location, char ch){
        ArrayList<Span> spans = new ArrayList<>();
        int loc = listSpans(spans, location);
        StringBuilder raw = new StringBuilder(spans.get(spans.size() - 1)
            .getRaw());
        raw.insertChar(loc, ch);
        edit(spans, raw.toString());
    }

    public void deleteChar(int location){
        ArrayList<Span> spans = new ArrayList<>();
        int loc = listSpans(spans, location);
        StringBuilder raw = new StringBuilder(spans.get(spans.size() - 1)
            .getRaw());
        raw.deleteCharAt(loc);
        edit(spans, raw.toString());
    }

    private void edit(List<Span> spans, String raw){

        editMain(spans, raw, spans.size() - 1);
        catalogueMap = new CatalogueMap();
        update(documentChildren);
        spanRanges.invalidateAll();
    }

    private void editMain(List<Span> spans, String base, int index){

        Span replaced = spans.get(index);
        Span editor = spans.get(index - 1);
        String raw = "";
        for (Span span: (SpanNode<? extends Span>)editor){
            if (span == replaced){
                raw += base;
            } else {
                raw += span.getRaw();
            }
        }

        if (index - 1 == 0){
            parseDocument(raw);
        } else {
            DetailUpdater updater = ((SpanBranch)editor)
                .getUpdater(spans.subList(index, spans.size()), raw);
            if (updater.getPlan() == DetailUpdateType.UNABLE){
                editMain(spans, raw, index - 1);
            } else {
                parseText(updater, spans, index, raw);
            }
        }
    }

    private void parseText(DetailUpdater updater, List<Span> spans, int index,
        String base
    ){
        SpanNode<? extends Span> holder = (SpanNode<? extends Span>)spans
            .get(index - 2);
        SpanNode<? extends Span> editor = (SpanNode<? extends Span>)spans
            .get(index - 1);
        String input = base;

        int replace = holder.indexOf(editor);
        DetailUpdateType updateType = updater.getPlan();
        switch (updateType){
        case REPLACE:
            break;
        case MERGE_LAST:
            input = getSpanRaw(replace - 1, holder) + input;
            replace--;
            break;
        case MERGE_BOTH:
            input = getSpanRaw(replace - 1, holder) + input
                + getSpanRaw(replace + 1, holder);
            replace--;
            break;
        case MERGE_NEXT:
            input = input + getSpanRaw(replace + 1, holder);
            break;
        default:
            throw Checker.typeNotUse(updateType, "updateType");
        }
        if (replace < 0){
            replace = 0;
        }
        assert replace >= 0;
        for (int i = 0; i < updateType.getRemoveSpans(); i++){
            if (replace < holder.size()){
                holder.removeChild(replace);
            } else {
                break;
            }
        }
        holder.addChildren(replace, updater.parse(input, this));
    }

    private String getSpanRaw(int i, SpanNode<? extends Span> holder){
        if (0 <= i && i < holder.size()){
            return holder.get(i).getRaw();
        }
        return "";
    }*/
}
