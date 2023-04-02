package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

final class IdentityTodoPatternTest extends PatternTestBase<IdentityTodoPattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(IdentityTodoPattern.matcher("{!ad}"));
    }

    @Test
    void testFullPattern() {
        final String raw = "{!Test}";
        final Matcher match = IdentityTodoPattern.matcher(raw);
        assertGroup("{!", match, IdentityTodoPattern.START, 1);
        assertGroup("Test", match, IdentityTodoPattern.TEXT, 2);
        assertGroup("}", match, IdentityTodoPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testNoEnd() {
        final String raw = "{!Test";
        final Matcher match = IdentityTodoPattern.matcher(raw);
        assertGroup("{!", match, IdentityTodoPattern.START, 1);
        assertGroup("Test", match, IdentityTodoPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testNoText() {
        final String raw = "{!}";
        final Matcher match = IdentityTodoPattern.matcher(raw);
        assertGroup("{!", match, IdentityTodoPattern.START, 1);
        assertGroup("}", match, IdentityTodoPattern.END, 2);
        assertEnd(match);
    }

    @Test
    void testStartOnly() {
        final String raw = "{!";
        final Matcher match = IdentityTodoPattern.matcher(raw);
        assertGroup("{!", match, IdentityTodoPattern.START, 1);
        assertEnd(match);
    }
}
