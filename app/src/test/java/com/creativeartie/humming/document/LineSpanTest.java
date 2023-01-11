package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class LineSpanTest extends SpanBranchTestBase {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(">", LineStyles.QUOTE), Arguments.of("", LineStyles.NORMAL),
                Arguments.of("!", LineStyles.AGENDA), Arguments.of("=", LineStyles.HEADING)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyLine(String starter, LineStyles style) {
        LineSpan span = LineSpan.newLine(newParent(), starter);
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyEndLine(String starter, LineStyles style) {
        LineSpan span = LineSpan.newLine(newParent(), starter + "\n");
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        addStyleTest("\n", style, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testLine(String starter, LineStyles style) {
        LineSpan span = LineSpan.newLine(newParent(), starter + "abc");
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        addStyleTest("abc", style, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEndLine(String starter, LineStyles style) {
        LineSpan span = LineSpan.newLine(newParent(), starter + "abc\n");
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        addStyleTest("abc", style, SpanStyles.TEXT);
        addStyleTest("\n", style, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
    }

    @Test
    void testShortBreak() {
        LineSpan span = LineSpan.newLine(newParent(), "**");
        addStyleTest("**", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
    }

    @Test
    void testLongBreak() {
        LineSpan span = LineSpan.newLine(newParent(), "*******");
        addStyleTest("*******", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
    }

    @Test
    void testSingleBreak() {
        LineSpan span = LineSpan.newLine(newParent(), "*");
        addStyleTest("*", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
    }

    @Test
    void testBreak() {
        LineSpan span = LineSpan.newLine(newParent(), "***");
        addStyleTest("***", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
    }

    @Test
    void testFullBreak() {
        LineSpan span = LineSpan.newLine(newParent(), "***\n");
        addStyleTest("***", LineStyles.BREAK, SpanStyles.OPERATOR);
        addStyleTest("\n", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
    }

    @Test
    void testAgenda() {
        LineSpan span = LineSpan.newLine(newParent(), "!Hello*\n");
        addStyleTest("!", LineStyles.AGENDA, SpanStyles.OPERATOR);
        addStyleTest("Hello*", LineStyles.AGENDA, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.AGENDA, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(LineStyles.AGENDA, span.getLineStyle(), "Line style");
        Assertions.assertInstanceOf(AgendaLine.class, span);
        Assertions.assertEquals("Hello*", ((AgendaLine) span).getAgenda());
    }

    @Test
    void testHeadingNoStatus() {
        LineSpan span = LineSpan.newLine(newParent(), "=abc");
        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("abc", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals(LineStyles.HEADING, span.getLineStyle(), "Line style");
        Assertions.assertInstanceOf(HeadingLine.class, span);
        HeadingLine heading = (HeadingLine) span;
        Assertions.assertEquals(HeadingLine.DraftStatus.NONE, heading.getStatus());
        Assertions.assertEquals("", heading.getDetail());
        Assertions.assertEquals(1, heading.getLevel());
    }

    @Test
    void testHeadingDraftStatus() {
        LineSpan span = LineSpan.newLine(newParent(), "==abc#Draft 1");
        addStyleTest("==", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("abc", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("#Draft", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest(" 1", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals(LineStyles.HEADING, span.getLineStyle(), "Line style");
        Assertions.assertInstanceOf(HeadingLine.class, span);
        HeadingLine heading = (HeadingLine) span;
        Assertions.assertEquals(HeadingLine.DraftStatus.DRAFT, heading.getStatus());
        Assertions.assertEquals("1", heading.getDetail());
        Assertions.assertEquals(2, heading.getLevel());
    }
}
