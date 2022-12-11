package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/*
 * testing LinkPattern. All passed test is copy of {@link #testFull} with small
 * edits.
 */
class LinkPatternTest extends PatternTestBase<LinkPattern> {

    @BeforeAll
    static void displayTest() {
        System.out.println(LinkPattern.matcher("{@ad}").pattern().pattern());
    }

    @Test
    void testFull() {
        String raw = "{@http://example.com|Example}";
        Matcher match = LinkPattern.matcher(raw);

        assertGroup("{@", match, LinkPattern.START, 1);
        assertGroup("http://example.com", match, LinkPattern.LINK, 2);
        assertGroup("|", match, LinkPattern.SEP, 3);
        assertGroup("Example", match, LinkPattern.TEXT, 4);
        assertGroup("}", match, LinkPattern.END, 5);
        assertEnd(match);
    }

    @Test
    void testNoEnd() {
        String raw = "{@http://example.com|Example";
        Matcher match = LinkPattern.matcher(raw);

        assertGroup("{@", match, LinkPattern.START, 1);
        assertGroup("http://example.com", match, LinkPattern.LINK, 2);
        assertGroup("|", match, LinkPattern.SEP, 3);
        assertGroup("Example", match, LinkPattern.TEXT, 4);
        assertEnd(match);
    }

    @Test
    void testNoText() {
        String raw = "{@http://example.com|}";
        Matcher match = LinkPattern.matcher(raw);

        assertGroup("{@", match, LinkPattern.START, 1);
        assertGroup("http://example.com", match, LinkPattern.LINK, 2);
        assertGroup("|", match, LinkPattern.SEP, 3);
        assertGroup("}", match, LinkPattern.END, 4);
        assertEnd(match);
    }

    @Test
    void testLinkOnlyWithEnd() {
        String raw = "{@http://example.com}";
        Matcher match = LinkPattern.matcher(raw);

        assertGroup("{@", match, LinkPattern.START, 1);
        assertGroup("http://example.com", match, LinkPattern.LINK, 2);
        assertGroup("}", match, LinkPattern.END, 3);
        assertEnd(match);
    }

    @Test
    void testLinkOnlyAndNoEnd() {
        String raw = "{@http://example.com";
        Matcher match = LinkPattern.matcher(raw);

        assertGroup("{@", match, LinkPattern.START, 1);
        assertGroup("http://example.com", match, LinkPattern.LINK, 2);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "{@|adde}", "{}" })
    void testErrors(String raw) {
        assertFail(() -> LinkPattern.matcher(raw));
    }

}
