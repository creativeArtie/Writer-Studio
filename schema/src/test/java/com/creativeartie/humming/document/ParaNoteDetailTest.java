package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

@SuppressWarnings("nls")
final class ParaNoteDetailTest extends SpanBranchTestBase<ParaNoteDetail> {
    @Test
    void testFull() {
        ParaNoteDetail span = newSpan("%abc\n");
        addStyleTest("%", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        addStyleTest("abc", CssLineStyles.NOTE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testNoEnd() {
        ParaNoteDetail span = newSpan("%abc");
        addStyleTest("%", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        addStyleTest("abc", CssLineStyles.NOTE, CssSpanStyles.TEXT);
        testStyles(span);
    }

    @Test
    void testNoText() {
        ParaNoteDetail span = newSpan("%\n");
        addStyleTest("%", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        addStyleTest("\n", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testStartOnly() {
        ParaNoteDetail span = newSpan("%");
        addStyleTest("%", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        testStyles(span);
    }

    @Override
    protected ParaNoteDetail initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaNoteDetail.class, span);
        return (ParaNoteDetail) span;
    }
}
