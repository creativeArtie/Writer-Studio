package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.api.function.*;

import java.time.*;
import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

/// Group of tests related to Document
public class DocumentAssert {

    /// %Part 1 Last Branch ####################################################

    /// Span covering unused text.
    private static class LastBranch extends SpanBranch{

        private LastBranch(List<Span> children){
            super(children);
        }

        @Override public List<StyleInfo> getBranchStyles(){
            return new ArrayList<>();
        }
        @Override protected SetupParser getParser(String text){
            return null;
        }

        @Override public String toString(){
            return "unused:" + get(0);
        }
    }

    /// Parser for {@link LastBranch}.
    private static final SetupParser END_PARSER = (pointer) -> {
        ArrayList<Span> children = new ArrayList<>();
        pointer.getTo(children, ((char)0) + "");
        return Optional.of(new LastBranch(children));
    };

    /// %Part 2: Creation ######################################################

    public static DocumentAssert assertDoc(int childrenSize, String rawText,
            Document test){
        return new DocumentAssert(test).assertDoc(childrenSize, rawText);
    }

    public static DocumentAssert assertDoc(int childrenSize, String rawText,
            SetupParser ... parsers){
        SetupParser[] input = SetupParser.combine(parsers, END_PARSER);
        Document test = new Document(rawText, input){};
        return assertDoc(childrenSize, rawText, test);
    }

    private final Document testDocument;
    private IDAssert idTester;

    private DocumentAssert(Document document){
        testDocument = document;
        idTester = new IDAssert();
    }

    /// %Part 3: Document Assertion ############################################

    public DocumentAssert assertDoc(int size, String raw){
        ArrayList<Executable> doc = new ArrayList<>();
        doc.add(() -> assertEquals(raw, testDocument.getRaw(), "getRaw()"));
        doc.add(() -> assertEquals(size, testDocument.size(), "size()"));
        doc.add(() -> assertLocations(testDocument, true, true));
        assertAll("document", doc);
        return this;
    }

    /// For {@link Document#isDocumentFirst()} and
    /// {@link Document#isDocumentLast()}
    private Executable assertLocations(SpanNode<?> span, boolean begin,
            boolean end){
        ArrayList<Executable> self = new ArrayList<>();
        int i = 0;
        for (Span test: span){
            boolean first = i == 0 && begin;
            self.add(() ->
                assertEquals(first, test.isDocumentFirst(), "isDocumentFirst()")
            );

            boolean last = i == span.size() - 1 && end;
            self.add(() ->
                assertEquals(last, test.isDocumentLast(), "isDocumentLast()")
            );

            /// Also test for children spans
            if (test instanceof SpanNode){
                self.add(() -> assertLocations((SpanNode<?>) test,
                    first, last));
            }
            i++;
        }
        return () -> assertAll(span.toString(), self);
    }

    /// %Part 4: Assert Child ##################################################

    /// Gets a child,
    private Span assertChild(int ... indexes){
        Span pointer = testDocument;
        ArrayList<Executable> search = new ArrayList<>();
        for (int i: indexes){
            SpanNode<?> parent = (SpanNode<?>) pointer;
            SpanNode<?> test = pointer instanceof SpanNode? (SpanNode<?>) pointer:
                null;
            assertAll("index for " + pointer,
                () -> assertTrue(test != null, "not span node"),
                () -> assertTrue(test.size() > i,
                    () -> "out of range, not: " + test.size() + ">" + i),
                () -> assertEquals(parent, test.get(i).getParent(),
                    () -> "parent of " + test.get(i)),
                () -> assertEquals(testDocument, test.get(i).getDocument(),
                    () -> "document of " + test.get(i))
            );
            pointer = ((SpanNode<?>) pointer).get(i);
        }
        return pointer;
    }

    /// Finds a child with this class type
    <T extends SpanBranch> T assertChild(Class<T> clazz,
            int ... indexes){
        Span child = assertChild(indexes);
        assertTrue(clazz.isInstance(child),
            () -> "Wrong class: " + clazz + ", gotten " +
            child.getClass() + ": " + child);
        return clazz.cast(child);

    }

    /// Main test a {@link SpanBranch} without using {@link SpanBranchAssert}.
    public SpanBranch assertChild(int size, String text,
            int ... indexes){
        Span test = assertChild(indexes);
        assertAll("SpanBranch assertChild",
            () -> assertTrue(test instanceof SpanBranch,        "class"),
            () -> assertEquals(text, test.getRaw(),             "getRaw()"),
            () -> assertEquals(size, ((SpanBranch)test).size(), "size()")
        );
        return (SpanBranch) test;
    }

    /// %Part 5: Assert Leaf ###################################################

    /// {@link SpanLeaf} tests.
    private void assertLeaf(int start, int end, String raw,
            StyleInfoLeaf info, int ... indexes){
        Span child = assertChild(indexes);

        assertAll(child.toString(),
            () -> assertTrue(child instanceof SpanLeaf, "Not leaf"),
            () -> assertEquals(raw,   child.getRaw(),   "getRaw()"),
            () -> assertEquals(start, child.getStart(), "getStatrt()"),
            () -> assertEquals(end,   child.getEnd(),   "getEnd()"),
            () -> assertEquals(info, ((SpanLeaf)child).getLeafStyle(),
                "getLeafStyle()")
        );
    }

    public void assertText(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.TEXT, idx);
    }

    public void assertKey(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.KEYWORD, idx);
    }

    public void assertId(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.ID, idx);
    }

    public void assertData(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.DATA, idx);
    }

    public void assertField(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.FIELD, idx);
    }

    public void assertPath(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.PATH, idx);
    }

    /// %Part 6: IDAssertion Methods ###########################################

    public IDBuilder addId(IDBuilder id){
        idTester.addId(id);
        return id;
    }

    public IDBuilder addRef(IDBuilder id){
        idTester.addRef(id);
        return id;
    }

    public IDBuilder addId(IDBuilder addId, int i) {
        idTester.addId(addId, i);
        return addId;
    }

    public IDBuilder addId(IDBuilder addId, CatalogueStatus newStatus, int i){
        idTester.addId(addId, i, newStatus);
        return addId;
    }

    public IDBuilder addRef(IDBuilder addId, int i) {
        idTester.addRef(addId, i);
        return addId;
    }

    public IDBuilder addRef(IDBuilder addId, CatalogueStatus newStatus, int i){
        idTester.addRef(addId, i, newStatus);
        return addId;
    }

    /// %Part 7: Last Assertion Tests ##########################################

    public void assertRest(String last){
        assertRest(last, false);
    }

    public void assertRest(String last, boolean show){
        idTester.assertIds(testDocument, show);
        SpanBranch target = testDocument.get(testDocument.size() - 1);
        assertAll("last",
            () -> assertTrue(target instanceof LastBranch,
                "No more text."),
            () -> assertEquals(last, target.getRaw())
        );
    }

    public void assertRest(){
        assertRest(false);
    }

    public void assertRest(boolean show){
        idTester.assertIds(testDocument, show);
        if (! testDocument.isEmpty()){
            assertFalse(testDocument.get(testDocument.size() - 1) instanceof
                LastBranch, "More text");
        }
    }

    /// %Part 8: Edit Listener Tests ###########################################

    public void insert(int location, String input, int ... idx){
        insert(false, location, input, idx);
    }

    public void insert(boolean verbose, int location, String input, int ... idx){
        EditAssert edit = createAssert(verbose, idx);
        testDocument.insert(location, input);
        idTester = new IDAssert();
        edit.testRest();
    }


    public void delete(int start, int end, int ... idx){
        delete(false, start, end, idx);
    }

    public void delete(boolean verbose, int start, int end, int ... idx){
        EditAssert edit = createAssert(verbose, idx);
        testDocument.delete(start, end);
        idTester = new IDAssert();
        edit.testRest();
    }

    public <T extends SpanBranch> void call(Class<T> clazz,
            Consumer<T> caller, int ... idx) {
        call(false, () -> getChild(clazz, idx), caller, idx);
    }

    public <T extends SpanBranch> void call(boolean verbose, Class<T> clazz,
            Consumer<T> caller, int ... idx) {
        call(verbose, () -> getChild(clazz, idx), caller, idx);
    }

    public <T extends SpanBranch> void call(Supplier<T> supplier,
            Consumer<T> caller, int ... idx) {
        call(false, supplier, caller, idx);
    }

    public <T extends SpanBranch> void call(Supplier<T> supplier,
            Consumer<T> caller, Supplier<SpanNode<?>[]> children){
        call(false, supplier, caller, children);
    }
    public <T extends SpanBranch> void call(boolean verbose, Supplier<T> supplier,
            Consumer<T> caller, Supplier<SpanNode<?>[]> children){
        assertAll("runCommand", () -> {
            assertAll("use spans", () -> children.get(),
                () -> supplier.get());
            SpanNode<?>[] targets = children.get();
            EditAssert edits = new EditAssert(verbose, testDocument, targets);
            int i = 0;
            assertAll("function", () -> caller.accept(supplier.get()));
            assertAll("listeners", () -> edits.testRest());
        });
    }

    public <T extends SpanBranch> void call(boolean verbose,
            Supplier<T> supplier, Consumer<T> caller, int ... idx) {
        assertAll("runCommand", () -> {
            Span target = assertChild(idx);
            assertTrue(target instanceof SpanNode, "Not Branch: " +
                target.getClass());
            EditAssert edit = new EditAssert(verbose, testDocument,
                (SpanBranch) target);
            caller.accept(supplier.get());
            edit.testRest();
        });
    }

    public <T extends SpanBranch> T getChild(Class<T> clazz,
            int ... indexes){
        Span target = assertChild(indexes);
        assertEquals(clazz, target.getClass(), "class");
        return clazz.cast(target);
    }

    private EditAssert createAssert(boolean verbose, int ... indexes){
        Span child = assertChild(indexes);
        assertTrue(child instanceof SpanNode, () -> "Target not branch: " +
            child);
        return new EditAssert(verbose, testDocument, (SpanNode<?>) child);

    }

    /// %Part 9: Get and Print #################################################

    public Document getDocument(){
        return testDocument;
    }

    public void printDocument(){
        System.out.println(testDocument);
    }
}
