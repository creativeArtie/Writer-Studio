package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class ParaNoteHeadTest extends SpanBranchTestBase<ParaNoteHead> {
    @Test
    void testFull() {
        ParaNoteHead span = newSpan("!%=title#id\n");
        addStyleTest("!", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("title", StyleLines.HEADER, StylesSpans.TEXT);
        addStyleTest("#", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("id", StyleLines.HEADER, StylesSpans.ID, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADER, StylesSpans.OPERATOR);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoEnder() {
        ParaNoteHead span = newSpan("!%=title#id");
        addStyleTest("!", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("title", StyleLines.HEADER, StylesSpans.TEXT);
        addStyleTest("#", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("id", StyleLines.HEADER, StylesSpans.ID, StylesSpans.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testMissingId() {
        ParaNoteHead span = newSpan("!%=title#");
        addStyleTest("!", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("title", StyleLines.HEADER, StylesSpans.TEXT);
        addStyleTest("#", StyleLines.HEADER, StylesSpans.OPERATOR);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoTitle() {
        ParaNoteHead span = newSpan("!%=#id");
        addStyleTest("!", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("#", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("id", StyleLines.HEADER, StylesSpans.ID, StylesSpans.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoId() {
        ParaNoteHead span = newSpan("!%=title");
        addStyleTest("!", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("title", StyleLines.HEADER, StylesSpans.TEXT);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testStartOnly() {
        ParaNoteHead span = newSpan("!%=");
        addStyleTest("!", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Override
    protected ParaNoteHead initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaNoteHead.class, span);
        return (ParaNoteHead) span;
    }
}
