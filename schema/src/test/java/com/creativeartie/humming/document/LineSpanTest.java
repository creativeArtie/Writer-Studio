package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class LineSpanTest extends SpanBranchTestBase<LineSpan> {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(">", LineStyles.QUOTE), Arguments.of("", LineStyles.NORMAL),
                Arguments.of("!", LineStyles.AGENDA), Arguments.of("=", LineStyles.HEADING)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyLine(String starter, LineStyles style) {
        LineSpan span = newSpan(starter);
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyEndLine(String starter, LineStyles style) {
        LineSpan span = newSpan(starter + "\n");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        addStyleTest("\n", style, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testLine(String starter, LineStyles style) {
        LineSpan span = newSpan(starter + "abc");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        addStyleTest("abc", style, SpanStyles.TEXT);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEndLine(String starter, LineStyles style) {
        LineSpan span = newSpan(starter + "abc\n");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, SpanStyles.OPERATOR);
        addStyleTest("abc", style, SpanStyles.TEXT);
        addStyleTest("\n", style, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testShortBreak() {
        LineSpan span = newSpan("**");
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("**", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testLongBreak() {
        LineSpan span = newSpan("*******");
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("*******", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testSingleBreak() {
        LineSpan span = newSpan("*");
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("*", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testBreak() {
        LineSpan span = newSpan("***");
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("***", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testFullBreak() {
        LineSpan span = newSpan("***\n");
        Assertions.assertEquals(LineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("***", LineStyles.BREAK, SpanStyles.OPERATOR);
        addStyleTest("\n", LineStyles.BREAK, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testAgenda() {
        LineSpan span = newSpan("!Hello*\n");
        Assertions.assertEquals(LineStyles.AGENDA, span.getLineStyle(), "Line style");
        addStyleTest("!", LineStyles.AGENDA, SpanStyles.OPERATOR);
        addStyleTest("Hello*", LineStyles.AGENDA, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.AGENDA, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertInstanceOf(AgendaLine.class, span);
        Assertions.assertEquals("Hello*", ((AgendaLine) span).getAgenda());
    }

    @Test
    void testHeadingNoStatus() {
        LineSpan span = newSpan("=abc");
        Assertions.assertEquals(LineStyles.HEADING, span.getLineStyle(), "Line style");
        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("abc", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertInstanceOf(HeadingLine.class, span);
        HeadingLine heading = (HeadingLine) span;
        Assertions.assertEquals(HeadingLine.DraftStatus.NONE, heading.getStatus());
        Assertions.assertEquals("", heading.getDetail());
        Assertions.assertEquals(1, heading.getLevel());
    }

    @Test
    void testHeadingDraftStatus() {
        LineSpan span = newSpan("==abc#Draft 1");
        Assertions.assertEquals(LineStyles.HEADING, span.getLineStyle(), "Line style");
        addStyleTest("==", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("abc", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("#Draft", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest(" 1", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertInstanceOf(HeadingLine.class, span);
        HeadingLine heading = (HeadingLine) span;
        Assertions.assertEquals(HeadingLine.DraftStatus.DRAFT, heading.getStatus());
        Assertions.assertEquals("1", heading.getDetail());
        Assertions.assertEquals(2, heading.getLevel());
    }

    @Override
    protected LineSpan initSpan(SpanBranch parent, String input) {
        return LineSpan.newLine(parent, input);
    }
}
