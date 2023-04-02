package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

final class ParaLineTest extends SpanBranchTestBase<ParaList> {
    @Test
    void testBullet() {
        Para test = newSpan("--bullet\n");
        addStyleTest("--", StyleLines.BULLET, StylesSpans.OPERATOR);
        addStyleTest("bullet", StyleLines.BULLET, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.BULLET, StylesSpans.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(2, ((SpanList) test).getLevel());
    }

    @Test
    void testNumbered() {
        Para test = newSpan("#numbered\n");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("numbered", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(1, ((SpanList) test).getLevel());
    }

    @Test
    void testEndOnly() {
        Para test = newSpan("#\n");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(1, ((SpanList) test).getLevel());
    }

    @Test
    void testNoEnder() {
        Para test = newSpan("#numbered");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("numbered", StyleLines.NUMBERED, StylesSpans.TEXT);
        testStyles(test);
        Assertions.assertInstanceOf(ParaList.class, test);
        Assertions.assertEquals(1, ((SpanList) test).getLevel());
    }

    @Test
    void testStartOnly() {
        Para test = newSpan("#");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
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
