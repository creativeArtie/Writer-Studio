package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class ListLineTest extends SpanBranchTestBase {
    @Test
    void testBullet() {
        LineSpan test = LineSpan.newLine(newParent(), "--bullet\n");
        addStyleTest("--", LineStyles.BULLET, SpanStyles.OPERATOR);
        addStyleTest("bullet", LineStyles.BULLET, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.BULLET, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(2, ((ListLine) test).getLevel());
    }

    @Test
    void testNumbered() {
        LineSpan test = LineSpan.newLine(newParent(), "#numbered\n");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("numbered", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(1, ((ListLine) test).getLevel());
    }

    @Test
    void testEndOnly() {
        LineSpan test = LineSpan.newLine(newParent(), "#\n");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(1, ((ListLine) test).getLevel());
    }

    @Test
    void testNoEnder() {
        LineSpan test = LineSpan.newLine(newParent(), "#numbered");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("numbered", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(1, ((ListLine) test).getLevel());
    }

    @Test
    void testStartOnly() {
        LineSpan test = LineSpan.newLine(newParent(), "#");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ListLine.class, test);
        Assertions.assertEquals(1, ((ListLine) test).getLevel());
    }
}
