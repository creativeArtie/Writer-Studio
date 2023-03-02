package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class ParaNoteDetailTest extends SpanBranchTestBase<ParaNoteDetail> {
    @Test
    void testFull() {
        ParaNoteDetail span = newSpan("!%abc\n");
        addStyleTest("!", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("%", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("abc", StyleLines.NOTE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NOTE, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testNoEnd() {
        ParaNoteDetail span = newSpan("!%abc");
        addStyleTest("!", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("%", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("abc", StyleLines.NOTE, StylesSpans.TEXT);
        testStyles(span);
    }

    @Test
    void testNoText() {
        ParaNoteDetail span = newSpan("!%\n");
        addStyleTest("!", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("%", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("\n", StyleLines.NOTE, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testStartOnly() {
        ParaNoteDetail span = newSpan("!%");
        addStyleTest("!", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("%", StyleLines.NOTE, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Override
    protected ParaNoteDetail initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaNoteDetail.class, span);
        return (ParaNoteDetail) span;
    }
}
