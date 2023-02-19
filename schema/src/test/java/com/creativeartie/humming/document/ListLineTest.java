package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class ListLineTest extends SpanBranchTestBase<ListLine> {
    @Test
    void testBullet() {
        LineSpan test = newSpan("--bullet\n");
        addStyleTest("--", LineStyles.BULLET, SpanStyles.OPERATOR);
        addStyleTest("bullet", LineStyles.BULLET, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.BULLET, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(2, ((ListSpan) test).getLevel());
    }

    @Test
    void testNumbered() {
        LineSpan test = newSpan("#numbered\n");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("numbered", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(1, ((ListSpan) test).getLevel());
    }

    @Test
    void testEndOnly() {
        LineSpan test = newSpan("#\n");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(1, ((ListSpan) test).getLevel());
    }

    @Test
    void testNoEnder() {
        LineSpan test = newSpan("#numbered");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("numbered", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(1, ((ListSpan) test).getLevel());
    }

    @Test
    void testStartOnly() {
        LineSpan test = newSpan("#");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertEquals(1, ((ListSpan) test).getLevel());
    }

    @Override
    protected ListLine initSpan(SpanBranch parent, String input) {
        LineSpan span = LineSpan.newLine(parent, input);

        Assertions.assertInstanceOf(ListLine.class, span);
        return (ListLine) span;
    }
}
