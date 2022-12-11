package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ReferencePatternTest extends PatternTestBase<ReferencePattern> {

    @BeforeAll
    static void displayPattern() throws Exception {
        System.out
            .println(ReferencePattern.matcher("{^ad}").pattern().pattern());
    }

    @ParameterizedTest
    @CsvSource({ "^,FOOTNOTE", "*,ENDNOTE", ">,SOURCE", "%,REF" })
    void testFullRef(String test, String type) {
        String raw = "{" + test + "cat:id}";
        ReferencePattern pattern = ReferencePattern.valueOf(type);

        Matcher match = ReferencePattern.matcher(raw);
        assertGroup("{", match, ReferencePattern.START, 1);
        assertGroup(test, match, pattern, 2);
        assertGroup("cat:id", match, ReferencePattern.ID, 3);
        assertGroup("}", match, ReferencePattern.END, 4);
        assertEnd(match);
    }

    @Test
    void testNoEndRef() {
        String raw = "{^cat:id";

        Matcher match = ReferencePattern.matcher(raw);
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
