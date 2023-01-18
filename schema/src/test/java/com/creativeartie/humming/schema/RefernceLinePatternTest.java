package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.ReferenceLinePatterns.*;

class RefernceLinePatternTest extends PatternTestBase<RefLineParts> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern("Footnote", ReferenceLinePatterns.FOOTNOTE.matcher("!^id:note"));
        splitPrintPattern("Endnote", ReferenceLinePatterns.ENDNOTE.matcher("!*id:endnot"));
    }

    @Test
    void testAgenda() {
        Assertions.assertNull(ReferenceLinePatterns.ENDNOTE.matcher("!hello"));
        Assertions.assertNull(ReferenceLinePatterns.FOOTNOTE.matcher("!hello"));
    }

    @Test
    void testFootnote() {
        final Matcher matcher = ReferenceLinePatterns.FOOTNOTE.matcher("!^test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("^", matcher, RefLineParts.FOOTNOTE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testFootnoteError() {
        final Matcher matcher = ReferenceLinePatterns.FOOTNOTE.matcher("!^test-note");
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
        final Matcher matcher = ReferenceLinePatterns.ENDNOTE.matcher("!*test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("*", matcher, RefLineParts.ENDNOTE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testEndnoteError() {
        final Matcher matcher = ReferenceLinePatterns.ENDNOTE.matcher("!*test++note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("*", matcher, RefLineParts.ENDNOTE, 2);
        assertGroup("test++note", matcher, RefLineParts.ERROR, 5);
        assertEnd(matcher);
    }
}
