package com.creativeartie.jwriter.lang;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;


public class DocumentAssert {

    private final Document doc;
    private final IDTestDocument idTester;

    private DocumentAssert(Document document){
        doc = document;
        idTester = new IDTestDocument();
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
        assertEquals(getError("text", test), rawText,      test.getRaw());
        assertEquals(getError("size", test), childrenSize, test.size());
        return new DocumentAssert(test);
    }

    public static DocumentAssert assertDoc(int childrenSize, String rawText,
        SetupParser ... parsers
    ){
        Document test = new Document(rawText, parsers){};
        return assertDoc(childrenSize, rawText, test);
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
}
