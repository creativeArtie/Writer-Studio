package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ReferenceLineTest extends SpanBranchTestBase<ReferenceLine> {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(Arguments.of("^", LineStyles.FOOTNOTE), Arguments.of("*", LineStyles.ENDNOTE));
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNoteFull(String type, LineStyles style) {
        ReferenceLine span = newSpan("!" + type + "cat:id=data text\n");
        addStyleTest("!", style, SpanStyles.OPERATOR);
        addStyleTest(type, style, SpanStyles.OPERATOR);
        addStyleTest("cat", style, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", style, SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", style, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("=", style, SpanStyles.OPERATOR);
        addStyleTest("data text", style, SpanStyles.TEXT);
        addStyleTest("\n", style, SpanStyles.OPERATOR);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNote(String type, LineStyles style) {
        ReferenceLine span = newSpan("!" + type + "cat:id=data text");
        addStyleTest("!", style, SpanStyles.OPERATOR);
        addStyleTest(type, style, SpanStyles.OPERATOR);
        addStyleTest("cat", style, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", style, SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", style, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("=", style, SpanStyles.OPERATOR);
        addStyleTest("data text", style, SpanStyles.TEXT);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNoteError(String type, LineStyles style) {
        ReferenceLine span = newSpan("!" + type + "cat-did text");
        addStyleTest("!", style, SpanStyles.ERROR, SpanStyles.OPERATOR);
        addStyleTest(type, style, SpanStyles.ERROR, SpanStyles.OPERATOR);
        addStyleTest("cat-did text", style, SpanStyles.ERROR, SpanStyles.TEXT);
        testStyles(span);
    }

    @Override
    protected ReferenceLine initSpan(SpanBranch parent, String input) {
        LineSpan span = LineSpan.newLine(parent, input);
        Assertions.assertInstanceOf(ReferenceLine.class, span);
        return (ReferenceLine) span;
    }
}
