package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@SuppressWarnings("nls")
final class ParaReferenceTest extends SpanBranchTestBase<ParaReference> {
    private static Stream<Arguments> provideParameters() {
        return // @formatter:off
            Stream.of(
                Arguments.of("^", CssLineStyles.FOOTNOTE),
                Arguments.of("+", CssLineStyles.IMAGE)
            );//@formatter:on
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNoteFull(String type, CssLineStyles style) {
        ParaReference span = newSpan("!" + type + "cat:id=data text\n");
        addStyleTest("!", style, CssSpanStyles.OPERATOR);
        addStyleTest(type, style, CssSpanStyles.OPERATOR);
        addStyleTest("cat", style, CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest(":", style, CssSpanStyles.ID, CssSpanStyles.OPERATOR);
        addStyleTest("id", style, CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest("=", style, CssSpanStyles.OPERATOR);
        addStyleTest("data text", style, CssSpanStyles.TEXT);
        addStyleTest("\n", style, CssSpanStyles.OPERATOR);
        testStyles(span);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNote(String type, CssLineStyles style) {
        ParaReference span = newSpan("!" + type + "cat:id=data text");
        addStyleTest("!", style, CssSpanStyles.OPERATOR);
        addStyleTest(type, style, CssSpanStyles.OPERATOR);
        addStyleTest("cat", style, CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest(":", style, CssSpanStyles.ID, CssSpanStyles.OPERATOR);
        addStyleTest("id", style, CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest("=", style, CssSpanStyles.OPERATOR);
        addStyleTest("data text", style, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals(2, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testNoteError(String type, CssLineStyles style) {
        ParaReference span = newSpan("!" + type + "cat-did text");
        addStyleTest("!", style, CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);
        addStyleTest(type, style, CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);
        addStyleTest("cat-did text", style, CssSpanStyles.ERROR, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals(0, span.getWrittenCount(), "written");
        Assertions.assertEquals(0, span.getOutlineCount(), "outline");
    }

    @Override
    protected ParaReference initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaReference.class, span);
        return (ParaReference) span;
    }
}
