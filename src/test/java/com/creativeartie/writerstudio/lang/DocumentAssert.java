package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

public class DocumentAssert {

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

    private static final SetupParser END_PARSER = (pointer) -> {
        ArrayList<Span> children = new ArrayList<>();
        pointer.getTo(children, ((char)0) + "");
        return Optional.of(new LastBranch(children));
    };

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
    private IDAssertions idTester;

    private DocumentAssert(Document document){
    private DocumentAssert(Document document){
        testDocument = document;
        idTester = new IDAssertions();
    }

    public Document getDocument(){
        return testDocument;
    }

    public DocumentAssert assertDoc(int size, String raw){
		ArrayList<Executable> doc = new ArrayList<>();
        doc.add(() -> assertEquals(raw, testDocument.getRaw(), "text");
        doc.add(() -> assertEquals(size, testDocument.size(), "size");
        doc.add(() -> assertLocations(testDocument, true, true));
        assertAll("document", doc);
        return this;
    }

    private void assertLocations(SpanNode<?> span, boolean begin, boolean end){
		ArrayList<Executable> self = new ArrayList<>();
		int i = 0;
        for (Span test: span){
            boolean first = i == 0 && begin;
            self.add(() -> 
				assertEquals(first, test.isDocumentFirst(), "isFirst")
			);

            boolean last = i == span.size() - 1 && end;
            self.add(() -> 
				assertEquals(last, test.isDocumentLast(), "isLast")
			);
            if (test instanceof SpanNode){
				self.add(() -> assertLocations((SpanNode<?>) test, 
					first, last));
            }
            i++;
        }
        return () -> assertAll(span.toString(), self);
    }
    
    public SpanBranch assertChild(int ... indexes){
		Span pointer = testDocument;
		ArrayList<Executable> search = new ArrayList();
		for (int i: indexes){
			assertAll("index for " + pointer,
				() -> assertTrue(pointer instanceof SpanNode, 
					"not span node"),
				() -> assertTrue(((SpanNode<?>)pointer).size() > i, 
					"index"),
				() -> assertTrue(pointer, (SpanNode<?>)pointer.get(i),
					() -> "parent of " + pointer.get(i)),
				() -> assertEquals(testDocument, pointer, pointer.get(i), 
					() -> "document of " + pointer.get(i))
			);
			pointer = ((SpanNode<?>) pointer).get(i);
		}
		assertTrue(span instanceof SpanBranch, 
			() -> "not span branch:" + span);
		return (SpanBranch) span;
	}

    public <T extends SpanBranch> T assertChild(Class<T> clazz, 
			int ... indexes){
        Span child = assertChild(indexes);
        assertTrue(clazz.isInstance(child), 
			() -> "Wrong class: " + clazz + ", gotten " +
            child.getClass() + ": " + child);
        return clazz.cast(child);

    }

    public void printDocument(){
        System.out.println(testDocument);
    }

    public SpanBranch assertChild(int size, String text, 
			int ... indexes){
        SpanBranch test = assertChild(indexes);
		assertAll("SpanBranch assertChild", 
			() -> assertEquals(text, test.getRaw(), "text"),
			() -> assertEquals(size, test.size(),   "size")
		);
        return test;
    }

    private void assertLeaf(int start, int end, String raw,
            StyleInfoLeaf info, int ... indexes){
        Span[] child = assertChild(indexes);
		
		assertAll(child.toString(),
			() -> assertTrue(child instanceof SpanLeaf, "Not leaf"),
			() -> assertEquals(raw, child.getRaw(),      "text");
			() -> assertEquals(start,   test.getStart(), "start");
			() -> assertEquals(end,     test.getEnd(),   "end");
			() -> assertEquals(info, ((SpanLeaf)child).getLeafStyle(), 
				"style")
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
        idTester.addId(addId, newStatus, i);
        return addId;
    }

    public IDBuilder addRef(IDBuilder addId, int i) {
        idTester.addRef(addId, i);
        return addId;
    }

    public IDBuilder addRef(IDBuilder addId, CatalogueStatus newStatus, int i){
        idTester.addRef(addId, newStatus, i);
        return addId;
    }

    public void assertRest(String last){
        assertRest(false, last);
    }

    public void assertRest(String last, boolean show){
        idTester.assertIds(testDocument, show);
        SpanBranch last = testDocument.get(testDocument.size() - 1);
        assertAll("last",
			() -> assertTrue(last instanceof LastBranch, 
				"No more text."),
			() -> assertEquals(raw, last.getRaw())
		);
    }

    public void assertRest(){
        assertRest(false);
    }
    
    public void assertRest(boolean show){
        idTester.assertIds(testDocument, show);
        assertFalse(testDocument.get(testDocument.size() - 1) instanceof
            LastBranch, "More text");
    }

    public void insert(int location, String input, int ... idx){
        insert(false, location, input, idx);
    }

    public void insert(boolean verbose, int location, String input, int ... idx){
        EditAssert edit = new EditAssert(testDocument, assertChild(idx),
            verbose);
        testDocument.insert(location, input);
        idTester = new IDAssertions();
        edit.testRest();
    }


    public void delete(int start, int end, int ... idx){
        delete(false, start, end, idx);
    }

    public void delete(boolean verbose, int start, int end, int ... idx){
        EditAssert edit = new EditAssert(testDocument, assertChild(idx),
            verbose);
        testDocument.delete(start, end);
        idTester = new IDAssertions();
        edit.testRest();
    }

    public void call(Consumer<SpanNode<?>> caller, int ... idx) {
        SpanNode<?> target = assertChild(idx);
        EditAssert edit = new EditAssert(testDocument, target);
        caller.accept(target);
        edit.testRest();
    }
}
