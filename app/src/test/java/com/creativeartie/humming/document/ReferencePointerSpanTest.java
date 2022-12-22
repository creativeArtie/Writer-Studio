package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class ReferencePointerSpanTest extends SpanBranchTestBase {
    @Test
    void testFull() {
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
    void testPart() {
        ReferencePointerSpan span = ReferencePointerSpan.createSpan(newParent(), "{*cat:id");
        Assertions.assertNotNull(span);
        addStyleTest("{", StyleClasses.ENDNOTE, StyleClasses.OPERATOR);
        addStyleTest("*", StyleClasses.ENDNOTE, StyleClasses.OPERATOR);
        addStyleTest("cat", StyleClasses.ENDNOTE, StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest("-", StyleClasses.ENDNOTE, StyleClasses.ID, StyleClasses.OPERATOR);
        addStyleTest("id", StyleClasses.ENDNOTE, StyleClasses.ID, StyleClasses.TEXT);
        testStyles(span);
    }
}
