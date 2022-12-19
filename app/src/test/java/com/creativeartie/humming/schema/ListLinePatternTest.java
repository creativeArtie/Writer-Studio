package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ListLinePatternTest extends PatternTestBase<ListLinePattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(ListLinePattern.matcher("#"));
    }

    @Test
    void testNumbered() {
        final Matcher match = ListLinePattern.matcher("##adde");
        assertGroup("##", match, ListLinePattern.NUMBERED, 1);
        assertGroup("adde", match, ListLinePattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testBullet() {
        final Matcher match = ListLinePattern.matcher("--dde");
        assertGroup("--", match, ListLinePattern.BULLET, 1);
        assertGroup("dde", match, ListLinePattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testNumberedBullet() {
        final Matcher match = ListLinePattern.matcher("##--");
        assertGroup("##", match, ListLinePattern.NUMBERED, 1);
        assertGroup("--", match, ListLinePattern.TEXT, 2);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "=heading", "***", "Normal-text" })
    void testNotListLine(String text) {
        Assertions.assertNull(ListLinePattern.matcher(text));
    }
}
