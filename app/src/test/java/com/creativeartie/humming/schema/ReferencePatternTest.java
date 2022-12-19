package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ReferencePatternTest extends PatternTestBase<ReferencePattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(ReferencePattern.matcher("{^ad}"));
    }

    @ParameterizedTest
    @CsvSource({ "^,FOOTNOTE", "*,ENDNOTE", ">,SOURCE", "%,REF" })
    void testFullRef(String test, String type) {
        final String raw = "{" + test + "cat:id}";
        final ReferencePattern pattern = ReferencePattern.valueOf(type);

        final Matcher match = ReferencePattern.matcher(raw);
        assertGroup("{", match, ReferencePattern.START, 1);
        assertGroup(test, match, pattern, 2);
        assertGroup("cat:id", match, ReferencePattern.ID, 3);
        assertGroup("}", match, ReferencePattern.END, 4);
        assertEnd(match);
    }

    @Test
    void testNoEndRef() {
        final String raw = "{^cat:id";

        final Matcher match = ReferencePattern.matcher(raw);
        assertGroup("{", match, ReferencePattern.START, 1);
        assertGroup("^", match, ReferencePattern.FOOTNOTE, 2);
        assertGroup("cat:id", match, ReferencePattern.ID, 3);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "{#avd}", "{^^abc}", "{^", "{^}" })
    void testError(String raw) {
        assertFail(() -> ReferencePattern.matcher(raw));
    }
}
