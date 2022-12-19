package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.NoteLinePatterns.*;

class NoteLinePatternsTest extends PatternTestBase<NoteLineParts> {
    @BeforeAll
    static void displayPatterns() throws Exception {
        splitPrintPattern("Heading", NoteLinePatterns.HEADING.matcher("!%=abc"));
        splitPrintPattern("Note", NoteLinePatterns.NOTE.matcher("!%abc"));
        splitPrintPattern("Source", NoteLinePatterns.SOURCE.matcher("!>author:john smith"));
    }

    @Test
    void testNoteHeadingWithGoodId() {
        final Matcher match = NoteLinePatterns.HEADING.matcher("!%=avd #cat:id");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertGroup("cat:id", match, NoteLineParts.ID, 5);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithBadId() {
        final Matcher match = NoteLinePatterns.HEADING.matcher("!%=avd #cat?id");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertGroup("cat?id", match, NoteLineParts.ERROR, 5);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithEmptyId() {
        final Matcher match = NoteLinePatterns.HEADING.matcher("!%=avd #");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithNoId() {
        final Matcher match = NoteLinePatterns.HEADING.matcher("!%=avd");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd", match, NoteLineParts.TITLE, 3);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithNoHeading() {
        final Matcher match = NoteLinePatterns.HEADING.matcher("!%=#cat?id");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("#", match, NoteLineParts.IDER, 3);
        assertGroup("cat?id", match, NoteLineParts.ERROR, 4);
        assertEnd(match);
    }

    @Test
    void testEmptyNoteHeading() {
        final Matcher match = NoteLinePatterns.HEADING.matcher("!%=");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertEnd(match);
    }

    @Test
    void testNote() {
        final Matcher match = NoteLinePatterns.NOTE.matcher("!%details");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%", match, NoteLineParts.NOTE, 2);
        assertGroup("details", match, NoteLineParts.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testEmptyNote() {
        final Matcher match = NoteLinePatterns.NOTE.matcher("!%");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%", match, NoteLineParts.NOTE, 2);
        assertEnd(match);
    }

    @Test
    void testCorrectSource() {
        final Matcher match = NoteLinePatterns.SOURCE.matcher("!>author:john smith");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup(">", match, NoteLineParts.SOURCE, 2);
        assertGroup("author", match, NoteLineParts.FIELD, 3);
        assertGroup(":", match, NoteLineParts.SOURCER, 4);
        assertGroup("john smith", match, NoteLineParts.VALUE, 5);
        assertEnd(match);
    }

    @Test
    void testErrorSource() {
        final Matcher match = NoteLinePatterns.SOURCE.matcher("!>author\\:john smith");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup(">", match, NoteLineParts.SOURCE, 2);
        assertGroup("author\\:john smith", match, NoteLineParts.ERROR, 3);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "=text", "!! todo ", "!=outline" })
    void testNonNote(String text) {
        Assertions.assertAll(
                () -> Assertions.assertNull(NoteLinePatterns.HEADING.matcher(text), "Heading"),
                () -> Assertions.assertNull(NoteLinePatterns.NOTE.matcher(text), "Note"),
                () -> Assertions.assertNull(NoteLinePatterns.SOURCE.matcher(text), "Source")
        );
    }
}
