package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ParaListPatternTest extends PatternTestBase<ParaListPattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(ParaListPattern.matcher("#"));
    }

    @Test
    void testNumbered() {
        final Matcher match = ParaListPattern.matcher("##adde");
        assertGroup("##", match, ParaListPattern.NUMBERED, 1);
        assertGroup("adde", match, ParaListPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testBullet() {
        final Matcher match = ParaListPattern.matcher("--dde");
        assertGroup("--", match, ParaListPattern.BULLET, 1);
        assertGroup("dde", match, ParaListPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testNumberedBullet() {
        final Matcher match = ParaListPattern.matcher("##--");
        assertGroup("##", match, ParaListPattern.NUMBERED, 1);
        assertGroup("--", match, ParaListPattern.TEXT, 2);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "=heading", "***", "Normal-text" })
    void testNotListLine(String text) {
        Assertions.assertNull(ParaListPattern.matcher(text));
    }
}
