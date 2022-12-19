package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

class TodoPatternTest extends PatternTestBase<TodoPattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(TodoPattern.matcher("{!ad}"));
    }

    @Test
    void testFullPattern() {
        String raw = "{!Test}";
        Matcher match = TodoPattern.matcher(raw);
        assertGroup("{!", match, TodoPattern.START, 1);
        assertGroup("Test", match, TodoPattern.TEXT, 2);
        assertGroup("}", match, TodoPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testNoEnd() {
        String raw = "{!Test";
        Matcher match = TodoPattern.matcher(raw);
        assertGroup("{!", match, TodoPattern.START, 1);
        assertGroup("Test", match, TodoPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testNoText() {
        String raw = "{!}";
        Matcher match = TodoPattern.matcher(raw);
        assertGroup("{!", match, TodoPattern.START, 1);
        assertGroup("}", match, TodoPattern.END, 2);
        assertEnd(match);
    }

    @Test
    void testStartOnly() {
        String raw = "{!";
        Matcher match = TodoPattern.matcher(raw);
        assertGroup("{!", match, TodoPattern.START, 1);
        assertEnd(match);
    }
}
