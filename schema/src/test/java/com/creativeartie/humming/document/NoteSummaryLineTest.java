package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class NoteSummaryLineTest extends SpanBranchTestBase<NoteSummaryLine> {
    @Test
    void testFull() {
        NoteSummaryLine span = newSpan("!%=title#id\n");
        addStyleTest("!", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.SUMMARY, SpanStyles.TEXT);
        addStyleTest("#", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("id", LineStyles.SUMMARY, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoEnder() {
        NoteSummaryLine span = newSpan("!%=title#id");
        addStyleTest("!", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.SUMMARY, SpanStyles.TEXT);
        addStyleTest("#", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("id", LineStyles.SUMMARY, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testMissingId() {
        NoteSummaryLine span = newSpan("!%=title#");
        addStyleTest("!", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.SUMMARY, SpanStyles.TEXT);
        addStyleTest("#", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoTitle() {
        NoteSummaryLine span = newSpan("!%=#id");
        addStyleTest("!", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("#", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("id", LineStyles.SUMMARY, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoId() {
        NoteSummaryLine span = newSpan("!%=title");
        addStyleTest("!", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.SUMMARY, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testStartOnly() {
        NoteSummaryLine span = newSpan("!%=");
        addStyleTest("!", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.SUMMARY, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Override
    protected NoteSummaryLine initSpan(SpanBranch parent, String input) {
        LineSpan span = LineSpan.newLine(parent, input);
        Assertions.assertInstanceOf(NoteSummaryLine.class, span);
        return (NoteSummaryLine) span;
    }
}
