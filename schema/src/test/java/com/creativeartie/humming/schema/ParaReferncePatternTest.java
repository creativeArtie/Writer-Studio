package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.ParaReferencePatterns.*;

class ParaReferncePatternTest extends PatternTestBase<RefLineParts> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern("Footnote", ParaReferencePatterns.FOOTNOTE.matcher("!^id:note"));
        splitPrintPattern("Endnote", ParaReferencePatterns.ENDNOTE.matcher("!*id:endnot"));
    }

    @Test
    void testAgenda() {
        Assertions.assertNull(ParaReferencePatterns.ENDNOTE.matcher("!hello"));
        Assertions.assertNull(ParaReferencePatterns.FOOTNOTE.matcher("!hello"));
    }

    @Test
    void testFootnote() {
        final Matcher matcher = ParaReferencePatterns.FOOTNOTE.matcher("!^test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("^", matcher, RefLineParts.FOOTNOTE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testFootnoteError() {
        final Matcher matcher = ParaReferencePatterns.FOOTNOTE.matcher("!^test-note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("^", matcher, RefLineParts.FOOTNOTE, 2);
        assertGroup("test-note", matcher, RefLineParts.ERROR, 3);
        assertEnd(matcher);
    }

    @Test
    void testNonFootnote() {
        Assertions.assertNull(ParaReferencePatterns.FOOTNOTE.matcher("!*"));
    }

    @Test
    void testEndnote() {
        final Matcher matcher = ParaReferencePatterns.ENDNOTE.matcher("!*test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("*", matcher, RefLineParts.ENDNOTE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testEndnoteError() {
        final Matcher matcher = ParaReferencePatterns.ENDNOTE.matcher("!*test++note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("*", matcher, RefLineParts.ENDNOTE, 2);
        assertGroup("test++note", matcher, RefLineParts.ERROR, 5);
        assertEnd(matcher);
    }
}