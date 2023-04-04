package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

final class ParaNoteHeadTest extends SpanBranchTestBase<ParaNoteHead> {
    @Test
    void testFull() {
        ParaNoteHead span = newSpan("%=title#id\n");
        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("title", CssLineStyles.HEADER, CssSpanStyles.TEXT);
        addStyleTest("#", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssLineStyles.HEADER, CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoEnder() {
        ParaNoteHead span = newSpan("%=title#id");
        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("title", CssLineStyles.HEADER, CssSpanStyles.TEXT);
        addStyleTest("#", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssLineStyles.HEADER, CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testMissingId() {
        ParaNoteHead span = newSpan("%=title#");
        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("title", CssLineStyles.HEADER, CssSpanStyles.TEXT);
        addStyleTest("#", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoTitle() {
        ParaNoteHead span = newSpan("%=#id");
        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("#", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssLineStyles.HEADER, CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertTrue(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testNoId() {
        ParaNoteHead span = newSpan("%=title");
        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("title", CssLineStyles.HEADER, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertFalse(span.getPointer().isPresent(), "Id");
    }

    @Test
    void testStartOnly() {
        ParaNoteHead span = newSpan("%=");
        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
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
