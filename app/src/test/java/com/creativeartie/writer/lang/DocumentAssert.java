package com.creativeartie.writer.lang;

import org.junit.jupiter.api.function.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/// Group of tests related to Document
public class DocumentAssert {

    /// %Part 1 Last Branch ####################################################

    /// Span covering unused text.
    private static class LastBranch extends SpanBranch{

        private LastBranch(List<Span> children){
            super(children);
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
        Document test = new Document(rawText, END_PARSER){};
        return assertDoc(childrenSize, rawText, test);
    }

    private final Document testDocument;
    private IDAssert idTester;
    private Optional<ListenerAssert<? extends SpanNode<?>>> listenTester;

    private DocumentAssert(Document document){
        testDocument = document;
        idTester = new IDAssert();
        listenTester = Optional.empty();
    }

    /// %Part 3: Document Assertion ############################################

    public DocumentAssert assertDoc(int size, String raw){
        listenTester.ifPresent(l -> l.runCommand());
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
        for (int i: indexes){
            SpanNode<?> parent = (SpanNode<?>) pointer;
            SpanNode<?> test = pointer instanceof SpanNode? (SpanNode<?>) pointer:
                null;
            assertAll("index for " + pointer,
                () -> assertTrue(test != null, "not span node"),
                () -> assertTrue(test.size() > i,
                    () -> "err of range, not: " + test.size() + ">" + i),
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

    /// Main test a {@link SpanBranch} witherr using {@link SpanBranchAssert}.
    public SpanBranch assertChild(int size, String text,
            int ... indexes){
        Span test = assertChild(indexes);
        assertAll("assertChild: " + test.toString(),
            () -> assertTrue(test instanceof SpanBranch,        "instanceof"),
            () -> assertEquals(text, test.getRaw(),             "getRaw()"),
            () -> assertEquals(size, ((SpanBranch)test).size(), "size()")
        );
        return (SpanBranch) test;
    }

    /// %Part 5: Assert Leaf ###################################################

    /// {@link SpanLeaf} tests.
    private void assertLeaf(int start, int end, String raw,
            SpanLeafStyle info, int ... indexes){
        Span child = assertChild(indexes);

        assertAll(child.toString(),
            () -> assertTrue(child instanceof SpanLeaf, "Not leaf"),
            () -> assertEquals(raw,   child.getRaw(),   "getRaw()"),
            () -> assertEquals(start, child.getStart(), "getStart()"),
            () -> assertEquals(end,   child.getEnd(),   "getEnd()"),
            () -> assertEquals(info, ((SpanLeaf)child).getLeafStyle(),
                "getLeafStyle()")
        );
    }

    public void assertText(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, SpanLeafStyle.TEXT, idx);
    }

    public void assertKey(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, SpanLeafStyle.KEYWORD, idx);
    }

    public void assertId(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, SpanLeafStyle.ID, idx);
    }

    public void assertData(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, SpanLeafStyle.DATA, idx);
    }

    public void assertField(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, SpanLeafStyle.FIELD, idx);
    }

    public void assertPath(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, SpanLeafStyle.PATH, idx);
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
        listenTester.ifPresent(l -> l.test());
        listenTester = Optional.empty();
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
        listenTester.ifPresent(l -> l.test());
        listenTester = Optional.empty();
    }

    /// %Part 8: Edit Listener Tests ###########################################

    public void insert(int location, String input, int ... idx){
        insert(false, location, input, idx);
    }

    public void insert(boolean verbose, int location, String input, int ... idx){
        listenTester = Optional.of(ListenerAssert
            .insert(testDocument, location, input, idx)
            .setShowEdits(verbose)
            .build()
        );
        idTester = new IDAssert();
    }

    public void noInsert(int location, String input, int ... idx){
        noInsert(false, location, input, idx);
    }

    public void noInsert(boolean verbose, int location, String input,
        int ... idx)
    {
        listenTester = Optional.of(ListenerAssert
            .insert(testDocument, location, input, idx)
            .setShowEdits(verbose)
            .noEditsBuild()
        );
        idTester = new IDAssert();
    }

    public void delete(int start, int end, int ... idx){
        delete(false, start, end, idx);
    }

    public void delete(boolean verbose, int start, int end, int ... idx){
        listenTester = Optional.of(ListenerAssert
            .delete(testDocument, start, end, idx).setShowEdits(verbose).build()
        );
        idTester = new IDAssert();
    }

    public void noDelete(int start, int end, int ... idx){
        noDelete(false, start, end, idx);
    }

    public void noDelete(boolean verbose, int start, int end, int ... idx){
        listenTester = Optional.of(ListenerAssert
            .delete(testDocument, start, end, idx).setShowEdits(verbose)
                .noEditsBuild()
        );
        idTester = new IDAssert();
    }

    public void setListenTester(ListenerAssert<?>.Builder listener){
        setListenTester(listener.build());
    }

    public void setListenTester(ListenerAssert<?> listener){
        listenTester = Optional.of(listener);
        idTester = new IDAssert();
    }

    public <T extends SpanNode<?>> T getChild(Class<T> clazz,
            int ... indexes){
        Span target = assertChild(indexes);
        assertEquals(clazz, target.getClass(), "class");
        return clazz.cast(target);
    }
    /// %Part 9: Get and Print #################################################

    public Document getDocument(){
        return testDocument;
    }

    public void printDocument(){
        System.err.println(testDocument);
    }
}
