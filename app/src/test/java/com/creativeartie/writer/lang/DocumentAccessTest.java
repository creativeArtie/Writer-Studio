package com.creativeartie.writer.lang;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import com.creativeartie.writer.lang.markup.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("General Document Tests.")
public class DocumentAccessTest{

    /// %Part 1: Before All Setup ##############################################

    /// Data structure to be use in tests.
    private static class TestData{
        /// #testColumn(), #testLine(), #testLeafByIndex()
        private int itemPointer;
        /// #testColumn(), #testLine(), #testLeafByStream() #testLeafByIndex()
        private boolean useEmpty;
        /// #testColumn(), #testLine(), #testLeafByStream() #testLeafByIndex()
        private int[] forSpan;
        /// #testColumn()
        private int expectColumn;
        /// #testLine()
        private int expectLine;
        /// #testLeafByStream()
        private int testIndex;
        private boolean isContains;
    }

    private static int countColumn;
    private static int countLine;
    private static int countIt;

    /// Create a {@link TestData}. Used in the majority of the cases
    private static String leafSpan(String span, int ... indexes){
        boolean isFirst = true;
        for(int i = 0; i < span.length(); i++){
            int store = isFirst? countIt: -1;

            TestData data = new TestData();
            data.itemPointer = testData.size() - 1;
            data.useEmpty = false;
            data.forSpan = indexes;
            data.expectColumn = countColumn;
            data.expectLine = countLine;
            data.testIndex = store;
            data.isContains = true;
            testData.add(data);

            if (span.charAt(i) == '\n'){
                countLine++;
                countColumn = 0;
            } else {
                countColumn++;
            }
        }
        countIt++;
        return span;
    }

    private static Collection<TestData> testData;

    private static Document filledDoc;
    private static Document emptyDoc;

    @BeforeAll
    public static void data() {
        testData = new ArrayList<>();
        StringBuilder raw = new StringBuilder();
        countColumn = 0;
        countLine = 1;

        /// For before filled document
        TestData data = new TestData();
        data.itemPointer = -1;
        data.useEmpty = false;
        data.forSpan = new int[0];
        data.expectColumn = 0;
        data.expectLine = 1;
        data.testIndex = -1;
        data.isContains = false;
        testData.add(data);

        raw.append(leafSpan("=",                0, 0, 0));
        raw.append(leafSpan("@",                0, 0, 1));
        raw.append(leafSpan("Chapter 1",        0, 0, 2, 0, 0));
        raw.append(leafSpan(":",                0, 0, 3));
        raw.append(leafSpan("\\",               0, 0, 4, 0, 0, 0));
        raw.append(leafSpan("\n",               0, 0, 4, 0, 0, 1));
        raw.append(leafSpan(" Story of nobody", 0, 0, 4, 0, 1));
        raw.append(leafSpan("\n",               0, 0, 5));

        raw.append(leafSpan("***\n", 0, 1, 0));

        /// Index after filled document
        data = new TestData();
        data.itemPointer = testData.size() - 1;
        data.useEmpty = false;
        data.forSpan = new int[]{0, 1, 0};
        data.expectColumn = 0;
        data.expectLine = countLine;
        data.testIndex = countIt++;
        data.isContains = false;
        testData.add(data);

        /// 2 indexes after filled document
        data = new TestData();
        data.itemPointer = testData.size() - 1;
        data.useEmpty = false;
        data.forSpan = new int[0];
        data.expectColumn = 0;
        data.expectLine = 1;
        data.testIndex = countIt++;
        data.isContains = false;
        testData.add(data);

        /// empty document to somewhere out there
        data = new TestData();
        data.itemPointer = testData.size() - 1;
        data.useEmpty = true;
        data.forSpan = new int[0];
        data.expectColumn = 0;
        data.expectLine = 1;
        data.testIndex = countIt++;
        data.isContains = false;
        testData.add(data);

        /// empty document to 0
        data = new TestData();
        data.itemPointer = 0;
        data.useEmpty = true;
        data.forSpan = new int[0];
        data.expectColumn = 0;
        data.expectLine = 1;
        data.testIndex = countIt++;
        data.isContains = false;
        testData.add(data);

        /// empty document to 1
        data = new TestData();
        data.itemPointer = 1;
        data.useEmpty = true;
        data.forSpan = new int[0];
        data.expectColumn = 0;
        data.expectLine = 1;
        data.testIndex = countIt++;
        data.isContains = false;
        testData.add(data);

        filledDoc = new WritingText(raw.toString());
        emptyDoc = new WritingText("");
    }

    /// %Part 2: Column and Lines ##############################################

    /// %Part 2.1: Test Column =================================================

    static Stream<Arguments> testColumn(){
        ArrayList<Arguments> ans = new ArrayList<>();
        for (TestData data: testData){
            ans.add(Arguments.of(data.itemPointer, data.useEmpty, data.forSpan,
                data.expectColumn));
        }
        return ans.stream();
    }

    @ParameterizedTest(name = "Column [empty={1}]: {0}")
    @MethodSource
    public void testColumn(int ptr, boolean empty, int[] indexes, int column){
        Document doc = empty? emptyDoc: filledDoc;
        String size = empty? "[0..0]": "[0..35]";
        if (indexes.length > 0 || (empty && ptr == 0) ){
            assertEquals(column, doc.getColumn(ptr));
        } else {
            Throwable thrown = assertThrows(IndexOutOfBoundsException.class,
                () -> doc.getColumn(ptr));
            assertEquals("Parameter \"position\" (" + ptr +
                ") is not in range of " + size + ".", thrown.getMessage());
        }
    }

    /// %Part 2.2: Test Line ===================================================

    static Stream<Arguments> testLine(){
        ArrayList<Arguments> ans = new ArrayList<>();
        for (TestData data: testData){
            ans.add(Arguments.of(data.itemPointer, data.useEmpty, data.forSpan,
                data.expectLine));
        }
        return ans.stream();
    }

    @ParameterizedTest(name = "Line [empty={1}]: {0}")
    @MethodSource
    public void testLine(int ptr, boolean empty, int[] indexes, int line){
        Document doc = empty? emptyDoc: filledDoc;
        String size = empty? "[0..0]": "[0..35]";
        if (indexes.length > 0 || (empty && ptr == 0) ){
            assertEquals(line, doc.getLine(ptr));
        } else {
            Throwable thrown = assertThrows(IndexOutOfBoundsException.class,
                () -> doc.getLine(ptr));
            assertEquals("Parameter \"position\" (" + ptr +
                ") is not in range of " + size + ".", thrown.getMessage());
        }
    }

    /// %Part 2.3: Helper Method ===============================================

    private Span findLeaf(int[] indexes){
        Span span = filledDoc;
        for(int index: indexes){
            assertTrue(span instanceof SpanNode, "Not span node");
            SpanNode<?> parent = (SpanNode<?>) span;
            assertTrue(index < parent.size(),
                () -> "Index (" + index + ") is not in range(" + parent.size()
                    + "):" + parent);
            span = ((SpanNode<SpanNode<?>>)span).get(index);
        }
        return span;
    }

    /// %Part 3: Get Leaf By Stream ############################################

    static Stream<Arguments> testLeafByStream(){
        ArrayList<Arguments> ans = new ArrayList<>();
        boolean first = true;
        int index = 0;
        for (TestData data: testData){
            if (first){
                first = false;
            } else if (index == data.testIndex){
                continue;
            }
            index = data.testIndex;
            ans.add(Arguments.of(data.useEmpty, data.forSpan, data.testIndex,
                data.isContains));
        }
        return ans.stream();
    }

    @ParameterizedTest(name = "Leaf by Stream [empty={0}]: {2} ({1})")
    @MethodSource
    public void testLeafByStream(boolean empty, int[] indexes, int index,
            boolean contains){
        if (empty || ! contains){
            assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                (empty? emptyDoc: filledDoc).getLeaves().get(index));
        } else {
            Span span = findLeaf(indexes);
            assertSame(span, filledDoc.getLeaves().get(index));
        }
    }

    /// %Part 4: Get Leaf By Index #############################################

    static Stream<Arguments> testLeafByIndex(){
        ArrayList<Arguments> ans = new ArrayList<>();
        for (TestData data: testData){
            ans.add(Arguments.of(data.itemPointer, data.useEmpty, data.forSpan));
        }
        return ans.stream();
    }

    @ParameterizedTest(name = "Leaf by index [empty={1}]: {0} ({2})")
    @MethodSource
    public void testLeafByIndex(int ptr, boolean empty, int[] indexes){
        Document doc = empty? emptyDoc: filledDoc;
        String size = empty? "[0..0]": "[0..35]";
        if (indexes.length == 0 && !(empty && ptr == 0)){
            Throwable thrown = assertThrows(IndexOutOfBoundsException.class,
                () -> System.out.println(doc.getLeaf(ptr)));
            assertEquals("Parameter \"pos\" (" + ptr +
                ") is not in range of " + size + ".", thrown.getMessage());
            return;
        }
        if (empty){
            Optional<SpanLeaf> found = emptyDoc.getLeaf(ptr);
            assertFalse(found.isPresent(), "Leaf is found.");
            return;
        }

        Optional<SpanLeaf> found = filledDoc.getLeaf(ptr);
        assertTrue(found.isPresent(), "Leaf not found.");
        SpanLeaf leaf = found.get();
        Span span = findLeaf(indexes);
        assertSame(span, leaf);
    }

}
