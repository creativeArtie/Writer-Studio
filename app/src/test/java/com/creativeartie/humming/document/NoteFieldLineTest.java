package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class NoteFieldLineTest extends SpanBranchTestBase<NoteFieldLine> {
    @Test
    void testFull() {
        NoteFieldLine span = newSpan("!>first_name=John\n");
        addStyleTest("!", LineStyles.FIELD, SpanStyles.OPERATOR);
        addStyleTest(">", LineStyles.FIELD, SpanStyles.OPERATOR);
        addStyleTest("first_name", LineStyles.FIELD, SpanStyles.TEXT);
        addStyleTest("=", LineStyles.FIELD, SpanStyles.OPERATOR);
        addStyleTest("John", LineStyles.FIELD, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.FIELD, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testNoEnd() {
        NoteFieldLine span = newSpan("!>first_name=John");
        addStyleTest("!", LineStyles.FIELD, SpanStyles.OPERATOR);
        addStyleTest(">", LineStyles.FIELD, SpanStyles.OPERATOR);
        addStyleTest("first_name", LineStyles.FIELD, SpanStyles.TEXT);
        addStyleTest("=", LineStyles.FIELD, SpanStyles.OPERATOR);
        addStyleTest("John", LineStyles.FIELD, SpanStyles.TEXT);
        testStyles(span);
    }

    @Test
    void testErrorFull() {
        NoteFieldLine span = newSpan("!>first_name\\=John\n");
        addStyleTest("!", LineStyles.FIELD, SpanStyles.ERROR, SpanStyles.OPERATOR);
        addStyleTest(">", LineStyles.FIELD, SpanStyles.ERROR, SpanStyles.OPERATOR);
        addStyleTest("first_name", LineStyles.FIELD, SpanStyles.ERROR, SpanStyles.TEXT);
        addStyleTest("\\=", LineStyles.FIELD, SpanStyles.ERROR, SpanStyles.ESCAPE);
        addStyleTest("John", LineStyles.FIELD, SpanStyles.ERROR, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.FIELD, SpanStyles.ERROR, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Override
    protected NoteFieldLine initSpan(SpanBranch parent, String input) {
        LineSpan span = LineSpan.newLine(parent, input);
        Assertions.assertInstanceOf(NoteFieldLine.class, span);
        return (NoteFieldLine) span;
    }
}
