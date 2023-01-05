package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class BasicSpanTest extends SpanBranchTestBase {
    @Test
    void testFullRef() {
        ReferencePointerSpan span = ReferencePointerSpan.createSpan(newParent(), "{^cat:id}");
        Assertions.assertNotNull(span);
        addStyleTest("{", SpanStyles.FOOTREF, SpanStyles.OPERATOR);
        addStyleTest("^", SpanStyles.FOOTREF, SpanStyles.OPERATOR);
        addStyleTest("cat", SpanStyles.FOOTREF, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", SpanStyles.FOOTREF, SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.FOOTREF, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.FOOTREF, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testPartRef() {
        ReferencePointerSpan span = ReferencePointerSpan.createSpan(newParent(), "{*cat:id");
        Assertions.assertNotNull(span);
        addStyleTest("{", SpanStyles.ENDREF, SpanStyles.OPERATOR);
        addStyleTest("*", SpanStyles.ENDREF, SpanStyles.OPERATOR);
        addStyleTest("cat", SpanStyles.ENDREF, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("-", SpanStyles.ENDREF, SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ENDREF, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(span);
    }

    @Test
    void testPositions() {
        ReferencePointerSpan test1 = ReferencePointerSpan.createSpan(newParent(), "{*cat:id}");
        getDocument().add(test1);
        ReferencePointerSpan test2 = ReferencePointerSpan.createSpan(newParent(), "{^test}");
        getDocument().add(test2);
        Assertions.assertEquals(0, test1.getIdPosition(), "Test 1 position");
        Assertions.assertEquals(9, test2.getIdPosition(), "Test 2 position");
    }

    @Test
    void testErrorRef() {
        ReferencePointerSpan span = ReferencePointerSpan.createSpan(newParent(), "{cat:id}");
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
