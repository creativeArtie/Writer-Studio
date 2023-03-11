package com.creativeartie.humming.document;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class IdentityReferenceTest extends SpanBranchTestBase<IdentityReference> {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of("*", StylesSpans.ENDNOTE), Arguments.of("^", StylesSpans.FOOTNOTE),
                Arguments.of("%", StylesSpans.METADATA), Arguments.of(">", StylesSpans.NOTE),
                Arguments.of("+", StylesSpans.IMAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testFull(String type, StylesSpans style) {
        IdentityReference test = newSpan("{" + type + "id}");

        addStyleTest("{", style, StylesSpans.OPERATOR);
        addStyleTest(type, style, StylesSpans.OPERATOR);
        addStyleTest("id", style, StylesSpans.ID, StylesSpans.TEXT);
        addStyleTest("}", style, StylesSpans.OPERATOR);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertTrue(id.isPresent(), "ID isPresent");
    }

    @Test
    void testNoEnd() {
        IdentityReference test = newSpan("{*id");

        addStyleTest("{", StylesSpans.ENDNOTE, StylesSpans.OPERATOR);
        addStyleTest("*", StylesSpans.ENDNOTE, StylesSpans.OPERATOR);
        addStyleTest("id", StylesSpans.ENDNOTE, StylesSpans.ID, StylesSpans.TEXT);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertTrue(id.isPresent(), "ID isPresent");
    }

    @Test
    void testNoRef() {
        IdentityReference test = newSpan("{id}");

        addStyleTest("{", StylesSpans.ERROR, StylesSpans.OPERATOR);
        addStyleTest("id", StylesSpans.ERROR, StylesSpans.TEXT);
        addStyleTest("}", StylesSpans.ERROR, StylesSpans.OPERATOR);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertFalse(id.isPresent(), "ID isPresent");
    }

    @Override
    protected IdentityReference initSpan(SpanBranch parent, String input) {
        return IdentityReference.newSpan(parent, input);
    }
}
