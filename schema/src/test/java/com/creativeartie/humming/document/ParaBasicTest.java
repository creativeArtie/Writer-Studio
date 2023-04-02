package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ParaBasicTest extends SpanBranchTestBase<Para> {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(">", StyleLines.QUOTE), Arguments.of("", StyleLines.NORMAL),
                Arguments.of("!", StyleLines.AGENDA), Arguments.of("=", StyleLines.HEADING)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyLine(String starter, StyleLines style) {
        Para span = newSpan(starter);
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyEndLine(String starter, StyleLines style) {
        Para span = newSpan(starter + "\n");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, StylesSpans.OPERATOR);
        addStyleTest("\n", style, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testLine(String starter, StyleLines style) {
        Para span = newSpan(starter + "abc");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, StylesSpans.OPERATOR);
        addStyleTest("abc", style, StylesSpans.TEXT);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEndLine(String starter, StyleLines style) {
        Para span = newSpan(starter + "abc\n");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, StylesSpans.OPERATOR);
        addStyleTest("abc", style, StylesSpans.TEXT);
        addStyleTest("\n", style, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testShortBreak() {
        Para span = newSpan("**");
        Assertions.assertEquals(StyleLines.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("**", StyleLines.BREAK, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testLongBreak() {
        Para span = newSpan("*******");
        Assertions.assertEquals(StyleLines.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("*******", StyleLines.BREAK, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testSingleBreak() {
        Para span = newSpan("*");
        Assertions.assertEquals(StyleLines.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("*", StyleLines.BREAK, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testBreak() {
        Para span = newSpan("***");
        Assertions.assertEquals(StyleLines.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("***", StyleLines.BREAK, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testFullBreak() {
        Para span = newSpan("***\n");
        Assertions.assertEquals(StyleLines.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("***", StyleLines.BREAK, StylesSpans.OPERATOR);
        addStyleTest("\n", StyleLines.BREAK, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @Test
    void testAgenda() {
        Para span = newSpan("!Hello*\n");
        Assertions.assertEquals(StyleLines.AGENDA, span.getLineStyle(), "Line style");
        addStyleTest("!", StyleLines.AGENDA, StylesSpans.OPERATOR);
        addStyleTest("Hello*", StyleLines.AGENDA, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.AGENDA, StylesSpans.OPERATOR);
        testStyles(span);
        Assertions.assertInstanceOf(ParaAgenda.class, span);
        Assertions.assertEquals("Hello*", ((ParaAgenda) span).getAgenda());
    }

    @Test
    void testHeadingNoId() {
        Para span = newSpan("=abc");
        Assertions.assertEquals(StyleLines.HEADING, span.getLineStyle(), "Line style");
        addStyleTest("=", StyleLines.HEADING, StylesSpans.OPERATOR);
        addStyleTest("abc", StyleLines.HEADING, StylesSpans.TEXT);
        testStyles(span);
        Assertions.assertInstanceOf(ParaHeading.class, span);
        ParaHeading heading = (ParaHeading) span;
        Assertions.assertFalse(heading.getPointer().isPresent());
        Assertions.assertEquals(1, heading.getLevel());
    }

    @Test
    void testHeadingWithID() {
        Para span = newSpan("==abc#chapter");
        Assertions.assertEquals(StyleLines.HEADING, span.getLineStyle(), "Line style");
        addStyleTest("==", StyleLines.HEADING, StylesSpans.OPERATOR);
        addStyleTest("abc", StyleLines.HEADING, StylesSpans.TEXT);
        addStyleTest("#", StyleLines.HEADING, StylesSpans.OPERATOR);
        addStyleTest("chapter", StyleLines.HEADING, StylesSpans.ID, StylesSpans.TEXT);
        testStyles(span);
        Assertions.assertInstanceOf(ParaHeading.class, span);
        ParaHeading heading = (ParaHeading) span;
        Assertions.assertTrue(heading.getPointer().isPresent());
        Assertions.assertEquals(2, heading.getLevel());
    }

    @Override
    protected Para initSpan(SpanBranch parent, String input) {
        return Para.newLine(parent, input);
    }
}
