package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.ParaReferencePatterns.*;

@SuppressWarnings("nls")
final class ParaReferncePatternTest extends PatternTestBase<RefLineParts> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern("Footnote", ParaReferencePatterns.FOOTNOTE.matcher("!^id:note"));
        splitPrintPattern("Image", ParaReferencePatterns.IMAGE.matcher("!+id:endnot"));
    }

    @Test
    void testAgenda() {
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
        Assertions.assertNull(ParaReferencePatterns.FOOTNOTE.matcher("!^"));
    }

    @Test
    void testEndnote() {
        final Matcher matcher = ParaReferencePatterns.FOOTNOTE.matcher("!^test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("^", matcher, RefLineParts.FOOTNOTE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testEndnoteError() {
        final Matcher matcher = ParaReferencePatterns.FOOTNOTE.matcher("!^test++note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("^", matcher, RefLineParts.FOOTNOTE, 2);
        assertGroup("test++note", matcher, RefLineParts.ERROR, 5);
        assertEnd(matcher);
    }

    @Test
    void testImageFull() {
        final Matcher matcher = ParaReferencePatterns.IMAGE.matcher("!+test=note");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("+", matcher, RefLineParts.IMAGE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("note", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }

    @Test
    void testImageNoCaption() {
        final Matcher matcher = ParaReferencePatterns.IMAGE.matcher("!+test");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("+", matcher, RefLineParts.IMAGE, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertEnd(matcher);
    }

    @Test
    void testImageError() {
        final Matcher matcher = ParaReferencePatterns.IMAGE.matcher("!+test++note=Hello");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("+", matcher, RefLineParts.IMAGE, 2);
        assertGroup("test++note=Hello", matcher, RefLineParts.ERROR, 5);
        assertEnd(matcher);
    }
}
