package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class NoteDetailLineTest extends SpanBranchTestBase<NoteDetailLine> {
    @Test
    void testFull() {
        NoteDetailLine span = newSpan("!%abc\n");
        addStyleTest("!", LineStyles.NOTE, SpanStyles.OPERATOR);
        addStyleTest("%", LineStyles.NOTE, SpanStyles.OPERATOR);
        addStyleTest("abc", LineStyles.NOTE, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NOTE, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testNoEnd() {
        NoteDetailLine span = newSpan("!%abc");
        addStyleTest("!", LineStyles.NOTE, SpanStyles.OPERATOR);
        addStyleTest("%", LineStyles.NOTE, SpanStyles.OPERATOR);
        addStyleTest("abc", LineStyles.NOTE, SpanStyles.TEXT);
        testStyles(span);
    }

    @Test
    void testNoText() {
        NoteDetailLine span = newSpan("!%\n");
        addStyleTest("!", LineStyles.NOTE, SpanStyles.OPERATOR);
        addStyleTest("%", LineStyles.NOTE, SpanStyles.OPERATOR);
        addStyleTest("\n", LineStyles.NOTE, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testStartOnly() {
        NoteDetailLine span = newSpan("!%");
        addStyleTest("!", LineStyles.NOTE, SpanStyles.OPERATOR);
        addStyleTest("%", LineStyles.NOTE, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Override
    protected NoteDetailLine initSpan(SpanBranch parent, String input) {
        LineSpan span = LineSpan.newLine(parent, input);
        Assertions.assertInstanceOf(NoteDetailLine.class, span);
        return (NoteDetailLine) span;
    }
}
