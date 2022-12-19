package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.ReferenceLinePatterns.*;

class RefernceLinePatternTest extends PatternTestBase<RefLineParts> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern("Link", ReferenceLinePatterns.LINK.matcher("!@test"));
        splitPrintPattern("Footnote", ReferenceLinePatterns.FOOTNOTE.matcher("!^id:note"));
        splitPrintPattern("Endnote", ReferenceLinePatterns.ENDNOTE.matcher("!*id:endnot"));
    }

    @Test
    void testLink() {
        Matcher matcher = ReferenceLinePatterns.LINK.matcher("!@test=www.example.com");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("@", matcher, RefLineParts.LINK, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("www.example.com", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testLinkError() {
        Matcher matcher = ReferenceLinePatterns.LINK.matcher("!@www.example.com");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("@", matcher, RefLineParts.LINK, 2);
        assertGroup("www.example.com", matcher, RefLineParts.ERROR, 3);
        assertEnd(matcher);
    }

    @Test
    void testNonLink() {
        Assertions.assertNull(ReferenceLinePatterns.LINK.matcher("!!"));
    }

    @Test
    void testFootnote() {
        Matcher matcher = ReferenceLinePatterns.FOOTNOTE.matcher("!^test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("^", matcher, RefLineParts.FOOTNOTE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testFootnoteError() {
        Matcher matcher = ReferenceLinePatterns.FOOTNOTE.matcher("!^test-note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("^", matcher, RefLineParts.FOOTNOTE, 2);
        assertGroup("test-note", matcher, RefLineParts.ERROR, 3);
        assertEnd(matcher);
    }

    @Test
    void testNonFootnote() {
        Assertions.assertNull(ReferenceLinePatterns.FOOTNOTE.matcher("!*"));
    }

    @Test
    void testEndnote() {
        Matcher matcher = ReferenceLinePatterns.ENDNOTE.matcher("!*test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("*", matcher, RefLineParts.ENDNOTE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testEndnoteError() {
        Matcher matcher = ReferenceLinePatterns.ENDNOTE.matcher("!*test++note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("*", matcher, RefLineParts.ENDNOTE, 2);
        assertGroup("test++note", matcher, RefLineParts.ERROR, 5);
        assertEnd(matcher);
    }
}
