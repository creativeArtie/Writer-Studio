package com.creativeartie.jwriter.lang;

import java.util.*;

import static org.junit.Assert.*;


public class DocumentAssert {

    private final Document doc;
    private final IDTestDocument idTester;
    private boolean editPass;

    private DocumentAssert(Document document){
        doc = document;
        idTester = new IDTestDocument();
        editPass = true;
    }

    public Document getDocument(){
        return doc;
    }

    public static String getError(String name, Object test){
        return "Wrong " + name + " for " + test.toString();
    }

    public static DocumentAssert assertDoc(int childrenSize, String rawText,
        Document test)
    {
        return new DocumentAssert(test).assertDoc(childrenSize, rawText);
    }

    public static DocumentAssert assertDoc(int childrenSize, String rawText,
        SetupParser ... parsers
    ){
        Document test = new Document(rawText, parsers){

            @Override
            protected void docEdited(){}
    };
        return assertDoc(childrenSize, rawText, test);
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
        SetupLeafStyle info, int ... idx
    ){
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


    public void assertTextLeaf(int start, int end, String rawText, int ... idx)
    {
        assertLeaf(start, end, rawText, SetupLeafStyle.TEXT, idx);
    }

    public void assertKeyLeaf(int start, int end, String rawText, int ... idx)
    {
        assertLeaf(start, end, rawText, SetupLeafStyle.KEYWORD, idx);
    }

    public void assertIdLeaf(int start, int end, String rawText, int ... idx)
    {
        assertLeaf(start, end, rawText, SetupLeafStyle.ID, idx);
    }

    public void assertDataLeaf(int start, int end, String rawText, int ... idx)
    {
        assertLeaf(start, end, rawText, SetupLeafStyle.DATA, idx);
    }

    public void assertFieldLeaf(int start, int end, String rawText, int ... idx)
    {
        assertLeaf(start, end, rawText, SetupLeafStyle.FIELD, idx);
    }

    public void assertPathLeaf(int start, int end, String rawText, int ... idx){
        assertLeaf(start, end, rawText, SetupLeafStyle.PATH, idx);
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
        willEdit(getChild(idx));
        doc.insert(location, input);
        assertTrue("No span changed.", editPass);
    }

    public void delete(int start, int end, int ... idx){
        willEdit(getChild(idx));
        doc.delete(start, end);
        assertTrue("No span changed.", editPass);
    }

    private int editedSpans;
    private int totalSpans;
    private DocumentAssert willEdit(SpanBranch span){
        editPass = false;
        editedSpans = 0;
        Document doc = span.getDocument();
        doc.addUpdater(out -> editedSpans++);
        doc.addRemover(out -> fail("Wrong span removed:" + out));
        doc.addEditor(out -> fail("Wrong span updated:" + out));
        totalSpans = 1;
        willEdit(doc, span, false);
        return this;
    }

    private void willEdit(SpanNode<?> root, SpanBranch target, boolean isChild){
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
