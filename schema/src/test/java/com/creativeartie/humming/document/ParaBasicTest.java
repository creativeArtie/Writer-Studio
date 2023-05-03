package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@SuppressWarnings("nls")
final class ParaBasicTest extends SpanBranchTestBase<Para> {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(">", CssLineStyles.QUOTE), Arguments.of("", CssLineStyles.NORMAL),
                Arguments.of("!", CssLineStyles.AGENDA), Arguments.of("=", CssLineStyles.HEADING)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyLine(String starter, CssLineStyles style) {
        Para span = newSpan(starter);
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEmptyEndLine(String starter, CssLineStyles style) {
        Para span = newSpan(starter + "\n");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, CssSpanStyles.OPERATOR);
        addStyleTest("\n", style, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testLine(String starter, CssLineStyles style) {
        Para span = newSpan(starter + "abc");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, CssSpanStyles.OPERATOR);
        addStyleTest("abc", style, CssSpanStyles.TEXT);
        testStyles(span);
        if (style == CssLineStyles.AGENDA) {
            Assertions.assertEquals(0, span.getWrittenCount(), "written");
            Assertions.assertEquals(1, span.getOutlineCount(), "outline");

        } else {
            Assertions.assertEquals(1, span.getWrittenCount(), "written");
            Assertions.assertEquals(0, span.getOutlineCount(), "outline");
        }
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testEndLine(String starter, CssLineStyles style) {
        Para span = newSpan(starter + "abc\n");
        Assertions.assertEquals(style, span.getLineStyle(), "Line style");
        if (starter != "") addStyleTest(starter, style, CssSpanStyles.OPERATOR);
        addStyleTest("abc", style, CssSpanStyles.TEXT);
        addStyleTest("\n", style, CssSpanStyles.OPERATOR);
        testStyles(span);
        if (style == CssLineStyles.AGENDA) {
            Assertions.assertEquals(0, span.getWrittenCount(), "written");
            Assertions.assertEquals(1, span.getOutlineCount(), "outline");

        } else {
            Assertions.assertEquals(1, span.getWrittenCount(), "written");
            Assertions.assertEquals(0, span.getOutlineCount(), "outline");
        }
    }

    @Test
    void testShortBreak() {
        Para span = newSpan("**");
        Assertions.assertEquals(CssLineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("**", CssLineStyles.BREAK, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Test
    void testLongBreak() {
        Para span = newSpan("*******");
        Assertions.assertEquals(CssLineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("*******", CssLineStyles.BREAK, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Test
    void testSingleBreak() {
        Para span = newSpan("*");
        Assertions.assertEquals(CssLineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("*", CssLineStyles.BREAK, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Test
    void testBreak() {
        Para span = newSpan("***");
        Assertions.assertEquals(CssLineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("***", CssLineStyles.BREAK, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Test
    void testFullBreak() {
        Para span = newSpan("***\n");
        Assertions.assertEquals(CssLineStyles.BREAK, span.getLineStyle(), "Line style");
        addStyleTest("***", CssLineStyles.BREAK, CssSpanStyles.OPERATOR);
        addStyleTest("\n", CssLineStyles.BREAK, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Test
    void testAgenda() {
        Para span = newSpan("!Hello*\n");
        Assertions.assertEquals(CssLineStyles.AGENDA, span.getLineStyle(), "Line style");
        addStyleTest("!", CssLineStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("Hello*", CssLineStyles.AGENDA, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.AGENDA, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertInstanceOf(ParaAgenda.class, span);
        Assertions.assertEquals("Hello*", ((ParaAgenda) span).getAgenda());
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(1, span.getOutlineCount(), "outline");
    }

    @Test
    void testHeadingNoId() {
        Para span = newSpan("=abc");
        Assertions.assertEquals(CssLineStyles.HEADING, span.getLineStyle(), "Line style");
        addStyleTest("=", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);
        addStyleTest("abc", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertInstanceOf(ParaHeading.class, span);
        ParaHeading heading = (ParaHeading) span;
        Assertions.assertFalse(heading.getPointer().isPresent());
        Assertions.assertEquals(1, heading.getLevel());
        Assertions.assertEquals(1, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Test
    void testHeadingWithID() {
        Para span = newSpan("==abc#chapter");
        Assertions.assertEquals(CssLineStyles.HEADING, span.getLineStyle(), "Line style");
        addStyleTest("==", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);
        addStyleTest("abc", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        addStyleTest("#", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);
        addStyleTest("chapter", CssLineStyles.HEADING, CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertInstanceOf(ParaHeading.class, span);
        ParaHeading heading = (ParaHeading) span;
        Assertions.assertTrue(heading.getPointer().isPresent());
        Assertions.assertEquals(2, heading.getLevel());
        Assertions.assertEquals(1, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Override
    protected Para initSpan(SpanBranch parent, String input) {
        return Para.newLine(parent, input);
    }
}
