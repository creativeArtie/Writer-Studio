package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class NoteSummaryLineTest extends SpanBranchTestBase<NoteSummaryLine> {
    @Test
    void testFull() {
        NoteSummaryLine span = newSpan("!%=title#id\n");
        addStyleTest("!", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.HEADER, SpanStyles.TEXT);
        addStyleTest("#", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("id", LineStyles.HEADER, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADER, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoEnder() {
        NoteSummaryLine span = newSpan("!%=title#id");
        addStyleTest("!", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.HEADER, SpanStyles.TEXT);
        addStyleTest("#", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("id", LineStyles.HEADER, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testMissingId() {
        NoteSummaryLine span = newSpan("!%=title#");
        addStyleTest("!", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.HEADER, SpanStyles.TEXT);
        addStyleTest("#", LineStyles.HEADER, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoTitle() {
        NoteSummaryLine span = newSpan("!%=#id");
        addStyleTest("!", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("#", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("id", LineStyles.HEADER, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoId() {
        NoteSummaryLine span = newSpan("!%=title");
        addStyleTest("!", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("title", LineStyles.HEADER, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testStartOnly() {
        NoteSummaryLine span = newSpan("!%=");
        addStyleTest("!", LineStyles.HEADER, SpanStyles.OPERATOR);
        addStyleTest("%=", LineStyles.HEADER, SpanStyles.OPERATOR);
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
