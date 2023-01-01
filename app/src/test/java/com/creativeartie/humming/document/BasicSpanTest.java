package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class BasicSpanTest extends SpanBranchTestBase {
    @Test
    void testFullRef() {
        ReferencePointerSpan span = ReferencePointerSpan.createSpan(newParent(), "{^cat:id}");
        Assertions.assertNotNull(span);
        addStyleTest("{", StyleClasses.FOOTNOTE, StyleClasses.OPERATOR);
        addStyleTest("^", StyleClasses.FOOTNOTE, StyleClasses.OPERATOR);
        addStyleTest("cat", StyleClasses.FOOTNOTE, StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest(":", StyleClasses.FOOTNOTE, StyleClasses.ID, StyleClasses.OPERATOR);
        addStyleTest("id", StyleClasses.FOOTNOTE, StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest("}", StyleClasses.FOOTNOTE, StyleClasses.OPERATOR);
        testStyles(span);
    }

    @Test
    void testPartRef() {
        ReferencePointerSpan span = ReferencePointerSpan.createSpan(newParent(), "{*cat:id");
        Assertions.assertNotNull(span);
        addStyleTest("{", StyleClasses.ENDNOTE, StyleClasses.OPERATOR);
        addStyleTest("*", StyleClasses.ENDNOTE, StyleClasses.OPERATOR);
        addStyleTest("cat", StyleClasses.ENDNOTE, StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest("-", StyleClasses.ENDNOTE, StyleClasses.ID, StyleClasses.OPERATOR);
        addStyleTest("id", StyleClasses.ENDNOTE, StyleClasses.ID, StyleClasses.TEXT);
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
        addStyleTest("{", StyleClasses.ERROR, StyleClasses.OPERATOR);
        addStyleTest("cat:id", StyleClasses.ERROR, StyleClasses.TEXT);
        addStyleTest("}", StyleClasses.ERROR, StyleClasses.OPERATOR);
        testStyles(span);
    }

    @Test
    void testTodo() {
        TodoSpan span = TodoSpan.newSpan(newParent(), "{!abc}");
        Assertions.assertNotNull(span);
        addStyleTest("{!", StyleClasses.TODO, StyleClasses.OPERATOR);
        addStyleTest("abc", StyleClasses.TODO, StyleClasses.TEXT);
        addStyleTest("}", StyleClasses.TODO, StyleClasses.OPERATOR);
        testStyles(span);
    }
}
