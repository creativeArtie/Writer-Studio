package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class LineSpanTest extends SpanBranchTestBase {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(">", LineStyles.QUOTE), Arguments.of("", LineStyles.NORMAL),
                Arguments.of("!", LineStyles.AGENDA)
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
}
