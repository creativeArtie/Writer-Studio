package com.creativeartie.jwriter.lang;

import java.util.*;

import static org.junit.Assert.*;


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
        @Override protected void childEdited(){}
        @Override protected void docEdited(){}

        @Override public String toString(){
            return "unused:" + get(0);
        }
    }

    private static final SetupParser END_PARSER = new SetupParser(){
        @Override public Optional<SpanBranch> parse(SetupPointer pointer){
            ArrayList<Span> children = new ArrayList<>();
            pointer.getTo(children, ((char)0) + "");
            return Optional.of(new LastBranch(children));
        }
    };

    public static DocumentAssert assertDoc(int childrenSize, String rawText,
            Document test){
        return new DocumentAssert(test).assertDoc(childrenSize, rawText);
    }

    public static DocumentAssert assertDoc(int childrenSize, String rawText,
            SetupParser ... parsers){
        SetupParser[] input = SetupParser.combine(parsers, END_PARSER);
        Document test = new Document(rawText, input){
            @Override protected void docEdited(){}
            @Override protected void childEdited(){}
        };
        return assertDoc(childrenSize, rawText, test);
    }

    public static String getError(String name, Object test){
        return "Wrong " + name + " for " + test.toString();
    }

    private final Document doc;
    private IDTestDocument idTester;
    private boolean editPass;
    private int editedSpans;
    private int totalSpans;

    private DocumentAssert(Document document){
        doc = document;
        idTester = new IDTestDocument();
        editPass = true;
    }

    public Document getDocument(){
        return doc;
    }

    public DocumentAssert assertDoc(int childrenSize, String rawText){
        assertEquals(getError("text", doc), rawText,      doc.getRaw());
        assertEquals(getError("size", doc), childrenSize, doc.size());
        return this;
    }

    private Span[] getFamily(int ... idx){
        Span span = doc;
        Span parent = null;
        for (int i: idx){
            assertTrue("Not a span node: " + span, span instanceof SpanNode);
            assertTrue("Index " + i + " not in range: " + span.toString(),
                ((SpanNode<?>)span).size() > i);
            parent = span;
            span = ((SpanNode<?>)span).get(i);
        }
        return new Span[]{span, parent};
    }

    public <T extends SpanBranch> T getChild(Class<T> clazz, int ... idx){
        Span child = getFamily(idx)[0];
        assertTrue("Expects instance of " + clazz.getSimpleName() + ", gotten " +
            child.getClass(), clazz.isInstance(child));
        return clazz.cast(child);

    }

    @Deprecated
    public SpanBranch getChild(int ... idx){
        Span child = getFamily(idx)[0];
        assertTrue("Expects instance of SpanBranch, gotten " + child.getClass(),
            child instanceof SpanBranch);
        return (SpanBranch) child;
    }

    public SpanBranch assertChild(int size, String rawText, int ... idx){
        Span[] prep = getFamily(idx);

        assertTrue("Wrong class for " + prep[0] +
            "Expects instance of SpanBranch, gotten " + prep[0].getClass(),
            prep[0] instanceof SpanBranch);
        SpanBranch test = (SpanBranch) prep[0];

        assertEquals(getError("text", test),     rawText, test.getRaw());
        assertEquals(getError("size", test),     size,    test.size());
        assertSame  (getError("document", test), doc,     test.getDocument());
        assertSame  (getError("parent", test),   prep[1], test.getParent());
        return test;
    }

    private void assertLeaf(int start, int end, String rawText,
            StyleInfoLeaf info, int ... idx){
        Span[] prep = getFamily(idx);
        assertTrue("Wrong class for " + prep[0] +
            "Expects instance of SpanLeaf, gotten " + prep[0].getClass(),
            prep[0] instanceof SpanLeaf);
        SpanLeaf test = (SpanLeaf) prep[0];

        assertEquals(getError("leaf text",  test), rawText, test.getRaw());
        assertEquals(getError("leaf style", test), info,    test.getLeafStyle());
        assertSame  (getError("document",   test), doc,     test.getDocument());
        assertSame  (getError("parent",     test), prep[1], test.getParent());
        assertEquals(getError("leaf start", test), start,   test.getStart());
        assertEquals(getError("leaf end",   test), end,     test.getEnd());
    }


    public void assertTextLeaf(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.TEXT, idx);
    }

    public void assertKeyLeaf(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.KEYWORD, idx);
    }

    public void assertIdLeaf(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.ID, idx);
    }

    public void assertDataLeaf(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.DATA, idx);
    }

    public void assertFieldLeaf(int start, int end, String rawText,
            int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.FIELD, idx);
    }

    public void assertPathLeaf(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, StyleInfoLeaf.PATH, idx);
    }

    public void assertLast(){
        assertFalse("Find more texts.", doc.get(doc.size() - 1) instanceof
            LastBranch);
    }

    public void assertLast(String rawText){
        SpanBranch last = doc.get(doc.size() - 1);
        assertTrue("No more text found.", last instanceof LastBranch);
        assertEquals("Wrong unparsed text", rawText, last.getRaw());
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

    public void assertIds(boolean showIds){
        idTester.assertIds(doc, showIds);
    }

    public void assertIds(){
        idTester.assertIds(doc);
    }

    public void insert(int location, String input, int ... idx){
        willEdit((SpanNode<?>)getFamily(idx)[0]);
        doc.insert(location, input);
        idTester = new IDTestDocument();
        assertTrue("No span changed.", editPass);
        assertEquals("Wrong number of updater & editor called", totalSpans,
            editedSpans);
    }

    public void delete(int start, int end, int ... idx){
        willEdit((SpanNode<?>)getFamily(idx)[0]);
        doc.delete(start, end);
        idTester = new IDTestDocument();
        assertTrue("No span changed.", editPass);
        assertEquals("Wrong number of updater & editor called", totalSpans,
            editedSpans);
    }

    private DocumentAssert willEdit(SpanNode<?> span){
        editPass = false;
        editedSpans = 0;
        Document doc = span.getDocument();
        doc.addUpdater(out -> editedSpans++);
        doc.addRemover(out -> fail("Wrong span removed:" + out));
        doc.addEditor(span == doc? out -> editPass = true:
            out -> fail("Wrong span updated:" + out));
        totalSpans = 1;
        willEdit(doc, span, false);
        return this;
    }

    private void willEdit(SpanNode<?> root, Span target, boolean isChild){
        for(Span span: root){
            if (span == target){
                span.addEditor(out -> editPass = true);
                doc.addUpdater(out -> editedSpans++);
                doc.addRemover(out -> fail("Wrong span removed:" + out));
                isChild = false;
            } else {
                span.addEditor(out -> fail("Wrong span changed:" + out));
                if (isChild){
                    doc.addUpdater(out -> fail("Wrong span updated:" + out));
                    doc.addRemover(out -> editedSpans++);
                } else {
                    doc.addUpdater(out -> editedSpans++);
                    doc.addRemover(out -> fail("Wrong span removed:" + out));
                }

            }
            if (span instanceof SpanNode<?>){
                willEdit((SpanNode<?>)span, target, isChild);
            }
            totalSpans++;
        }
    }
}
