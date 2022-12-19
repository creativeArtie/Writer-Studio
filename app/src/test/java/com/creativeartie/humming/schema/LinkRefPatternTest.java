package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

class LinkRefPatternTest extends PatternTestBase<LinkRefPattern> {
    @BeforeAll
    static void diplayPattern() {
        splitPrintPattern(LinkRefPattern.matcher("{@acd}"));
    }

    @Test
    void testFull() {
        Matcher match = LinkRefPattern.matcher("{@cat:id|title}");
        assertGroup("{@", match, LinkRefPattern.START, 0);
        assertGroup("cat:id", match, LinkRefPattern.ID, 1);
        assertGroup("|", match, LinkRefPattern.SEP, 2);
        assertGroup("title", match, LinkRefPattern.TEXT, 3);
        assertGroup("}", match, LinkRefPattern.END, 4);
        assertEnd(match);
    }

    @Test
    void testNoEnd() {
        Matcher match = LinkRefPattern.matcher("{@cat:id|title");
        assertGroup("{@", match, LinkRefPattern.START, 0);
        assertGroup("cat:id", match, LinkRefPattern.ID, 1);
        assertGroup("|", match, LinkRefPattern.SEP, 2);
        assertGroup("title", match, LinkRefPattern.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testSepOnly() {
        Matcher match = LinkRefPattern.matcher("{@cat:id|}");
        assertGroup("{@", match, LinkRefPattern.START, 0);
        assertGroup("cat:id", match, LinkRefPattern.ID, 1);
        assertGroup("|", match, LinkRefPattern.SEP, 2);
        assertGroup("}", match, LinkRefPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testNoTitle() {
        Matcher match = LinkRefPattern.matcher("{@cat:id}");
        assertGroup("{@", match, LinkRefPattern.START, 0);
        assertGroup("cat:id", match, LinkRefPattern.ID, 1);
        assertGroup("}", match, LinkRefPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testError() {
        Matcher match = LinkRefPattern.matcher("{@cat-id}");
        assertGroup("{@", match, LinkRefPattern.START, 0);
        assertGroup("cat-id", match, LinkRefPattern.ERROR, 1);
        assertGroup("}", match, LinkRefPattern.END, 3);
        assertEnd(match);
    }
}
