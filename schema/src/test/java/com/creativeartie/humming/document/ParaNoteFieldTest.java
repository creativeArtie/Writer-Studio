package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class ParaNoteFieldTest extends SpanBranchTestBase<ParaNoteField> {
    @Test
    void testFull() {
        ParaNoteField span = newSpan("!>first_name=John\n");
        addStyleTest("!", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest(">", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest("first_name", StyleLines.FIELD, StylesSpans.TEXT);
        addStyleTest("=", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest("John", StyleLines.FIELD, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.FIELD, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testNoEnd() {
        ParaNoteField span = newSpan("!>first_name=John");
        addStyleTest("!", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest(">", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest("first_name", StyleLines.FIELD, StylesSpans.TEXT);
        addStyleTest("=", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest("John", StyleLines.FIELD, StylesSpans.TEXT);
        testStyles(span);
    }

    @Test
    void testErrorFull() {
        ParaNoteField span = newSpan("!>first_name\\=John\n");
        addStyleTest("!", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.OPERATOR);
        addStyleTest(">", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.OPERATOR);
        addStyleTest("first_name", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.TEXT);
        addStyleTest("\\=", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.ESCAPE);
        addStyleTest("John", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Override
    protected ParaNoteField initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaNoteField.class, span);
        return (ParaNoteField) span;
    }
}
