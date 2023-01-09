package com.creativeartie.humming.document;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ReferencePointerSpanTest extends SpanBranchTestBase {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of("*", SpanStyles.ENDREF), Arguments.of("^", SpanStyles.FOOTREF),
                Arguments.of("%", SpanStyles.METAREF), Arguments.of(">", SpanStyles.CITEREF),
                Arguments.of("+", SpanStyles.IMAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testFull(String type, SpanStyles style) {
        ReferencePointerSpan test = ReferencePointerSpan.newSpan(newParent(), "{" + type + "id}");

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
        ReferencePointerSpan test = ReferencePointerSpan.newSpan(newParent(), "{*id");

        addStyleTest("{", SpanStyles.ENDREF, SpanStyles.OPERATOR);
        addStyleTest("*", SpanStyles.ENDREF, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ENDREF, SpanStyles.ID, SpanStyles.TEXT);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertTrue(id.isPresent(), "ID isPresent");
    }

    @Test
    void testNoRef() {
        ReferencePointerSpan test = ReferencePointerSpan.newSpan(newParent(), "{id}");

        addStyleTest("{", SpanStyles.ERROR, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ERROR, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.ERROR, SpanStyles.OPERATOR);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertFalse(id.isPresent(), "ID isPresent");
    }
}
