package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

final class ParaReferenceTest extends SpanBranchTestBase<ParaReference> {
    private static Stream<Arguments> provideParameters() {
        return // @formatter:off
            Stream.of(
                Arguments.of("^", StyleLines.FOOTNOTE),
                Arguments.of("*", StyleLines.ENDNOTE),
                Arguments.of("+", StyleLines.IMAGE)
            );//@formatter:on
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNoteFull(String type, StyleLines style) {
        ParaReference span = newSpan("!" + type + "cat:id=data text\n");
        addStyleTest("!", style, StylesSpans.OPERATOR);
        addStyleTest(type, style, StylesSpans.OPERATOR);
        addStyleTest("cat", style, StylesSpans.ID, StylesSpans.TEXT);
        addStyleTest(":", style, StylesSpans.ID, StylesSpans.OPERATOR);
        addStyleTest("id", style, StylesSpans.ID, StylesSpans.TEXT);
        addStyleTest("=", style, StylesSpans.OPERATOR);
        addStyleTest("data text", style, StylesSpans.TEXT);
        addStyleTest("\n", style, StylesSpans.OPERATOR);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNote(String type, StyleLines style) {
        ParaReference span = newSpan("!" + type + "cat:id=data text");
        addStyleTest("!", style, StylesSpans.OPERATOR);
        addStyleTest(type, style, StylesSpans.OPERATOR);
        addStyleTest("cat", style, StylesSpans.ID, StylesSpans.TEXT);
        addStyleTest(":", style, StylesSpans.ID, StylesSpans.OPERATOR);
        addStyleTest("id", style, StylesSpans.ID, StylesSpans.TEXT);
        addStyleTest("=", style, StylesSpans.OPERATOR);
        addStyleTest("data text", style, StylesSpans.TEXT);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNoteError(String type, StyleLines style) {
        ParaReference span = newSpan("!" + type + "cat-did text");
        addStyleTest("!", style, StylesSpans.ERROR, StylesSpans.OPERATOR);
        addStyleTest(type, style, StylesSpans.ERROR, StylesSpans.OPERATOR);
        addStyleTest("cat-did text", style, StylesSpans.ERROR, StylesSpans.TEXT);
        testStyles(span);
    }

    @Override
    protected ParaReference initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaReference.class, span);
        return (ParaReference) span;
    }
}
