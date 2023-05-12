package com.creativeartie.humming.document;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@SuppressWarnings("nls")
final class IdentityReferenceTest extends SpanBranchTestBase<IdentityReference> {
    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of("^", CssSpanStyles.FOOTNOTE), Arguments.of("%", CssSpanStyles.METADATA),
                Arguments.of(">", CssSpanStyles.NOTE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testFull(String type, CssSpanStyles style) {
        IdentityReference test = newSpan("{" + type + "id}");

        addStyleTest("{", style, CssSpanStyles.OPERATOR);
        addStyleTest(type, style, CssSpanStyles.OPERATOR);
        addStyleTest("id", style, CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest("}", style, CssSpanStyles.OPERATOR);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertTrue(id.isPresent(), "ID isPresent");
    }

    @Test
    void testNoEnd() {
        IdentityReference test = newSpan("{^id");

        addStyleTest("{", CssSpanStyles.FOOTNOTE, CssSpanStyles.OPERATOR);
        addStyleTest("^", CssSpanStyles.FOOTNOTE, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssSpanStyles.FOOTNOTE, CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertTrue(id.isPresent(), "ID isPresent");
    }

    @Test
    void testNoRef() {
        IdentityReference test = newSpan("{id}");

        addStyleTest("{", CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssSpanStyles.ERROR, CssSpanStyles.TEXT);
        addStyleTest("}", CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);
        testStyles(test);

        Optional<IdentitySpan> id = test.getPointer();
        Assertions.assertFalse(id.isPresent(), "ID isPresent");
    }

    @Override
    protected IdentityReference initSpan(SpanBranch parent, String input) {
        return IdentityReference.newSpan(parent, input);
    }
}
