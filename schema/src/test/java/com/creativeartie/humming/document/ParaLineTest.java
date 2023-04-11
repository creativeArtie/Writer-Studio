package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

@SuppressWarnings("nls")
final class ParaLineTest extends SpanBranchTestBase<ParaList> {
    @Test
    void testBullet() {
        Para test = newSpan("--bullet\n");
        addStyleTest("--", CssLineStyles.BULLET, CssSpanStyles.OPERATOR);
        addStyleTest("bullet", CssLineStyles.BULLET, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.BULLET, CssSpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(2, ((SpanList) test).getLevel());
    }

    @Test
    void testNumbered() {
        Para test = newSpan("#numbered\n");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("numbered", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(1, ((SpanList) test).getLevel());
    }

    @Test
    void testEndOnly() {
        Para test = newSpan("#\n");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(1, ((SpanList) test).getLevel());
    }

    @Test
    void testNoEnder() {
        Para test = newSpan("#numbered");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("numbered", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(1, ((SpanList) test).getLevel());
    }

    @Test
    void testStartOnly() {
        Para test = newSpan("#");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertEquals(1, ((SpanList) test).getLevel());
    }

    @Override
    protected ParaList initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);

        Assertions.assertInstanceOf(ParaList.class, span);
        return (ParaList) span;
    }
}
