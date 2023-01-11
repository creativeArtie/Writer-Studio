package com.creativeartie.humming.document;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ReferenceSpanTest extends SpanBranchTestBase<ReferenceSpan> {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of("*", SpanStyles.ENDNOTE), Arguments.of("^", SpanStyles.FOOTNOTE),
                Arguments.of("%", SpanStyles.METADATA), Arguments.of(">", SpanStyles.INFO),
                Arguments.of("+", SpanStyles.IMAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testFull(String type, SpanStyles style) {
        ReferenceSpan test = newSpan("{" + type + "id}");

        addStyleTest("{", style, SpanStyles.OPERATOR);
        addStyleTest(type, style, SpanStyles.OPERATOR);
        addStyleTest("id", style, SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("}", style, SpanStyles.OPERATOR);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertTrue(id.isPresent(), "ID isPresent");
    }

    @Test
    void testNoEnd() {
        ReferenceSpan test = newSpan("{*id");

        addStyleTest("{", SpanStyles.ENDNOTE, SpanStyles.OPERATOR);
        addStyleTest("*", SpanStyles.ENDNOTE, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ENDNOTE, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertTrue(id.isPresent(), "ID isPresent");
    }

    @Test
    void testNoRef() {
        ReferenceSpan test = newSpan("{id}");

        addStyleTest("{", SpanStyles.ERROR, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ERROR, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.ERROR, SpanStyles.OPERATOR);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertFalse(id.isPresent(), "ID isPresent");
    }

    @Override
    protected ReferenceSpan initSpan(SpanBranch parent, String input) {
        return ReferenceSpan.newSpan(parent, input);
    }
}
