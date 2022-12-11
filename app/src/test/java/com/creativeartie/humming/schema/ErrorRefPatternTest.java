package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

class ErrorRefPatternTest extends PatternTestBase<ErrorRefPattern> {

    @BeforeAll
    static void displayPattern() throws Exception {
        System.out
            .println(ErrorRefPattern.matcher("{dda}").pattern().pattern());
    }

    @Test
    void testFull() {
        Matcher match = ErrorRefPattern.matcher("{addd}");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertGroup("addd", match, ErrorRefPattern.TEXT, 2);
        assertGroup("}", match, ErrorRefPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testTextless() {
        Matcher match = ErrorRefPattern.matcher("{}");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertGroup("}", match, ErrorRefPattern.END, 2);
        assertEnd(match);
    }

    @Test
    void testNoEnd() {
        Matcher match = ErrorRefPattern.matcher("{addd");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertGroup("addd", match, ErrorRefPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testStartOnly() {
        Matcher match = ErrorRefPattern.matcher("{");
        assertGroup("{", match, ErrorRefPattern.START, 1);
        assertEnd(match);
    }

}
