package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class BasicSpanTest extends SpanBranchTestBase {
    @Test
    void testFullRef() {
        ReferenceSpan span = ReferenceSpan.newSpan(newParent(), "{^cat:id}");
        Assertions.assertNotNull(span);
        addStyleTest("{", SpanStyles.FOOTNOTE, SpanStyles.OPERATOR);
        addStyleTest("^", SpanStyles.FOOTNOTE, SpanStyles.OPERATOR);
        addStyleTest("cat", SpanStyles.FOOTNOTE, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", SpanStyles.FOOTNOTE, SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.FOOTNOTE, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.FOOTNOTE, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testPartRef() {
        ReferenceSpan span = ReferenceSpan.newSpan(newParent(), "{*cat:id");
        Assertions.assertNotNull(span);
        addStyleTest("{", SpanStyles.ENDNOTE, SpanStyles.OPERATOR);
        addStyleTest("*", SpanStyles.ENDNOTE, SpanStyles.OPERATOR);
        addStyleTest("cat", SpanStyles.ENDNOTE, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("-", SpanStyles.ENDNOTE, SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ENDNOTE, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(span);
    }

    @Test
    void testPositions() {
        ReferenceSpan test1 = ReferenceSpan.newSpan(newParent(), "{*cat:id}");
        getDocument().add(test1);
        ReferenceSpan test2 = ReferenceSpan.newSpan(newParent(), "{^test}");
        getDocument().add(test2);
        Assertions.assertEquals(0, test1.getIdPosition(), "Test 1 position");
        Assertions.assertEquals(9, test2.getIdPosition(), "Test 2 position");
    }

    @Test
    void testErrorRef() {
        ReferenceSpan span = ReferenceSpan.newSpan(newParent(), "{cat:id}");
        Assertions.assertNotNull(span);
        addStyleTest("{", SpanStyles.ERROR, SpanStyles.OPERATOR);
        addStyleTest("cat:id", SpanStyles.ERROR, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.ERROR, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testTodo() {
        TodoSpan span = TodoSpan.newSpan(newParent(), "{!abc}");
        Assertions.assertNotNull(span);
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("abc", SpanStyles.TODO, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.TODO, SpanStyles.OPERATOR);
        testStyles(span);
    }
}
