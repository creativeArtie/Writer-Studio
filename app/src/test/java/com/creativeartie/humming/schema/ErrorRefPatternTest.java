package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

class ErrorRefPatternTest extends PatternTestBase<ErrorRefPattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(ErrorRefPattern.matcher("{dda}"));
    }

    @Test
    void testFull() {
        final Matcher match = ErrorRefPattern.matcher("{addd}");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertGroup("addd", match, ErrorRefPattern.TEXT, 2);
        assertGroup("}", match, ErrorRefPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testTextless() {
        final Matcher match = ErrorRefPattern.matcher("{}");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertGroup("}", match, ErrorRefPattern.END, 2);
        assertEnd(match);
    }

    @Test
    void testNoEnd() {
        final Matcher match = ErrorRefPattern.matcher("{addd");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertGroup("addd", match, ErrorRefPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testStartOnly() {
        final Matcher match = ErrorRefPattern.matcher("{");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertEnd(match);
    }
}
