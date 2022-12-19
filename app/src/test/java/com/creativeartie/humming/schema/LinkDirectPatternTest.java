package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/*
 * testing LinkPattern. All passed test is copy of {@@link #testFull} with small
 * edits.
 */
class LinkDirectPatternTest extends PatternTestBase<LinkDirectPattern> {
    @BeforeAll
    static void displayTest() {
        splitPrintPattern(LinkDirectPattern.matcher("{@@ad}"));
    }

    @Test
    void testFull() {
        final String raw = "{@@http://example.com|Example}";
        final Matcher match = LinkDirectPattern.matcher(raw);

        assertGroup("{@@", match, LinkDirectPattern.START, 1);
        assertGroup("http://example.com", match, LinkDirectPattern.LINK, 2);
        assertGroup("|", match, LinkDirectPattern.SEP, 3);
        assertGroup("Example", match, LinkDirectPattern.TEXT, 4);
        assertGroup("}", match, LinkDirectPattern.END, 5);
        assertEnd(match);
    }

    @Test
    void testNoEnd() {
        final String raw = "{@@http://example.com|Example";
        final Matcher match = LinkDirectPattern.matcher(raw);

        assertGroup("{@@", match, LinkDirectPattern.START, 1);
        assertGroup("http://example.com", match, LinkDirectPattern.LINK, 2);
        assertGroup("|", match, LinkDirectPattern.SEP, 3);
        assertGroup("Example", match, LinkDirectPattern.TEXT, 4);
        assertEnd(match);
    }

    @Test
    void testNoText() {
        final String raw = "{@@http://example.com|}";
        final Matcher match = LinkDirectPattern.matcher(raw);

        assertGroup("{@@", match, LinkDirectPattern.START, 1);
        assertGroup("http://example.com", match, LinkDirectPattern.LINK, 2);
        assertGroup("|", match, LinkDirectPattern.SEP, 3);
        assertGroup("}", match, LinkDirectPattern.END, 4);
        assertEnd(match);
    }

    @Test
    void testLinkOnlyWithEnd() {
        final String raw = "{@@http://example.com}";
        final Matcher match = LinkDirectPattern.matcher(raw);

        assertGroup("{@@", match, LinkDirectPattern.START, 1);
        assertGroup("http://example.com", match, LinkDirectPattern.LINK, 2);
        assertGroup("}", match, LinkDirectPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testLinkOnlyAndNoEnd() {
        final String raw = "{@@http://example.com";
        final Matcher match = LinkDirectPattern.matcher(raw);

        assertGroup("{@@", match, LinkDirectPattern.START, 1);
        assertGroup("http://example.com", match, LinkDirectPattern.LINK, 2);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "{@@|adde}", "{}" })
    void testErrors(String raw) {
        assertFail(() -> LinkDirectPattern.matcher(raw));
    }
}
