package com.creativeartie.writer.lang;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

/// Tests for listeners
public class ListenerAssert<T extends SpanNode<?>>{

    public static ListenerAssert<Document>.Builder insert(DocumentAssert doc,
        int location, String input, int ... indexes
    ){
        return insert(doc.getDocument(), location, input, indexes);
    }

    public static ListenerAssert<Document>.Builder insert(Document doc,
        int location, String input, int ... indexes
    ){
        return builder(d -> d.insert(location, input), doc, doc)
            .replacesChildren(indexes);
    }

    public static ListenerAssert<Document>.Builder delete(DocumentAssert doc,
        int start, int end, int ... idx
    ){
        return delete(doc.getDocument(), start, end, idx);
    }

    public static ListenerAssert<Document>.Builder delete(Document doc,
        int start, int end, int ... indexes
    ){
        return builder(d -> d.delete(start, end), doc, doc)
            .replacesChildren(indexes);
    }

    public static <U extends Document> ListenerAssert<U>.Builder builder(
        Consumer<U> consumer, DocumentAssert doc, Class<U> clazz
    ){
        return builder(consumer, clazz.cast(doc.getDocument()));
    }

    public static <U extends Document> ListenerAssert<U>.Builder builder(
        Consumer<U> consumer, U doc
    ){
        return new ListenerAssert<>(consumer, doc, doc). new Builder();
    }

    public static <U extends SpanBranch> ListenerAssert<U>.Builder builder(
        Consumer<U> consumer, DocumentAssert doc, Class<U> clazz,
        int ... indexes
    ){
        return builder(consumer, doc.getDocument(), clazz, indexes);
    }

    public static <U extends SpanBranch> ListenerAssert<U>.Builder builder(
        Consumer<U> consumer,  Document doc, Class<U> clazz, int ... indexes
    ){
        return builder(consumer, doc, clazz.cast(getBranch(doc, indexes)));
    }

    public static <U extends SpanNode<?>> ListenerAssert<U>.Builder builder(
        Consumer<U> consumer, U span
    ){
        return builder(consumer, span.getDocument(), span);
    }

    public static <U extends SpanNode<?>> ListenerAssert<U>.Builder builder(
        Consumer<U> consumer, Document doc, U span
    ){
        return new ListenerAssert<>(consumer, doc, span).new Builder();
    }

    private static SpanNode<?> getBranch(Document doc, int ... indexes){
        SpanNode<?> ptr = doc;
        for (int index: indexes){
            assert index < ptr.size() && index >= 0 : index +
                " not between 0 and " + (ptr.size() - 1);
            ptr = (SpanNode<?>)ptr.get(index);
        }
        return ptr;
    }

    private static ArrayList<SpanNode<?>> listChildren(SpanNode<?> span){
        ArrayList<SpanNode<?>> children = new ArrayList<>();
        for (Span child: span){
            if (child instanceof SpanNode){
                SpanNode<?> found = (SpanBranch)child;
                children.add(found);
                children.addAll(listChildren(found));
            }
        }
        return children;
    }

    private static ArrayList<SpanNode<?>> listParents(SpanNode<?> span){
        ArrayList<SpanNode<?>> parents = new ArrayList<>();
        while (! (span instanceof Document)){
            span = span.getParent();
            parents.add(span);
        }
        return parents;
    }

    public class Builder{
        private Builder(){}

        public Builder replacesChildren(int ... indexes){
            SpanNode<?> span = getBranch(useDoc, indexes);
            expectedEdited.add(span);
            expectedParents.addAll(listParents(span));
            expectedRemoves.addAll(listChildren(span));
            return this;
        }

        public Builder setEdited(int ... indexes){
            SpanNode<?> span = getBranch(useDoc, indexes);
            expectedEdited.add(span);
            expectedParents.addAll(listParents(span));
            return this;
        }

        public Builder setRemoved(int ... indexes){
            SpanNode<?> span = getBranch(useDoc, indexes);
            expectedRemoves.add(span);
            expectedRemoves.addAll(listChildren(span));

            span = span.getParent();
            expectedEdited.add(span);
            expectedParents.addAll(listParents(span));

            return this;
        }

        public ListenerAssert<T> build(){
            for (SpanNode<?> span: expectedRemoves){
                expectedParents.remove(span);
            }
            return ListenerAssert.this;
        }

        public Builder showEdits(){
            showEdits = true;
            return this;
        }

        public Builder setShowEdits(boolean show){
            showEdits = show;
            return this;
        }

        public ListenerAssert<T> noEditsBuild(){
            expectedEdited.clear();
            expectedParents.clear();
            expectedSpans.clear();
            expectedRemoves.clear();
            return ListenerAssert.this;
        }
    }

    private boolean showEdits;
    private boolean hasTested;
    private final Document useDoc;
    private final Consumer<T> editCaller;
    private final T editingSpan;

    private final TreeSet<SpanNode<?>> expectedEdited;
    private final ArrayList<SpanNode<?>> actualEdited;

    private final TreeSet<SpanNode<?>> expectedParents;
    private final ArrayList<SpanNode<?>> actualParents;

    private final TreeSet<SpanNode<?>> expectedSpans;
    private final ArrayList<SpanNode<?>> actualSpans;

    private final TreeSet<SpanNode<?>> expectedRemoves;
    private final ArrayList<SpanNode<?>> actualRemoves;

    private ListenerAssert(Consumer<T> caller, Document doc, T span){
        showEdits = false;
        hasTested = false;
        useDoc = doc;
        editCaller = caller;
        editingSpan = span;
        expectedEdited = new TreeSet<>();
        actualEdited = new ArrayList<>();

        expectedParents = new TreeSet<>();
        actualParents = new ArrayList<>();

        expectedSpans = new TreeSet<>(listChildren(doc));
        expectedSpans.add(doc);
        actualSpans = new ArrayList<>();

        expectedRemoves = new TreeSet<>();
        actualRemoves = new ArrayList<>();

        for (SpanNode<?> child: expectedSpans){
            addListeners(child);
        }
    }

    private void addListeners(SpanNode<?> span){
        span.addSpanEdited(this::edited);
        span.addChildEdited(this::childed);
        span.addDocEdited(this::doc);
        span.addSpanRemoved(this::removed);
    }

    /// %Part 2: Listener functions ############################################

    private void edited(SpanNode<?> span){
        actualEdited.add(span);
    }

    private void childed(SpanNode<?> span){
        actualParents.add(span);
    }

    private void doc(SpanNode<?> span){
        actualSpans.add(span);
    }

    private void removed(SpanNode<?> span){
        actualRemoves.add(span);
    }

    public ListenerAssert<T> runCommand(){
        if (hasTested) return this;

        if (showEdits){
            System.err.println("+++++++++++++++++++++++++++++++++++++++++++++");
            System.err.println("Document Setup-------------------------------");
            System.err.println(useDoc);
            System.err.println("SpanNode Editing-----------------------------");
            if (editingSpan instanceof SpanBranch){
                System.err.println(editingSpan);
            } else {
                System.err.println("Document (same as Document Setup)");
            }
        }

        assertDoesNotThrow(() -> editCaller.accept(editingSpan),
                () -> "Caller throws an exeception: " + editingSpan);
        hasTested = true;

        if (showEdits) {
            System.err.println("Post Edited ---------------------------------");
            System.err.println(useDoc);

            System.err.println("Edits----------------------------------------");
            print(expectedEdited, actualEdited);

            System.err.println("Parents--------------------------------------");
            print(expectedParents, actualParents);

            System.err.println("Spans----------------------------------------");
            print(expectedSpans, actualSpans);

            System.err.println("Removes--------------------------------------");
            print(expectedRemoves, actualRemoves);
        }

        return this;
    }

    public ListenerAssert<T> test(){
        if (! hasTested) runCommand();

        ArrayList<Executable> tests = new ArrayList<>();

        /// Tests
        tests.add(search("edited", expectedEdited, actualEdited));

        tests.add(search("parent", expectedParents, actualParents));

        tests.add(search("spans", expectedSpans, actualSpans));

        tests.add(search("remove", expectedRemoves, actualRemoves));

        assertAll("edit listeners", tests);

        return this;
    }

    private void print(Set<SpanNode<?>> expects, List<SpanNode<?>> actual){
        System.err.println(formatList("expect", expects));
        System.err.println(formatList("actual", actual));
    }

    private String formatList(String type, Iterable<SpanNode<?>> list){
        int i = 1;
        String ans = "";
        for (SpanNode<?> span: list){
            ans += type + "[" + i++ + "](" + span.getClass().getSimpleName() +
                "):\t" + span + "\n";
        }
        if (i == 1){
            ans += "[]";
        }
        return ans;
    }

    private Executable search(String name, Set<SpanNode<?>> expect,
            List<SpanNode<?>> actual){
        ArrayList<Executable> list = new ArrayList<>();
        for (SpanNode<?> got: actual){
            list.add(() -> {
                assertTrue(expect.contains(got),
                    () -> "Unexpected" + name + ": "  + got);
                expect.remove(got);
            });
        }

        /// since the one called are removed from `expect`:
        list.add(() ->  assertTrue(
            expect.isEmpty(), () -> formatList("Not Call " + name + ": ", expect)
        ));
        return () -> assertAll(name, list);
    }
}
