package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.ParaNotePatterns.*;

class ParaNotePatternsTest extends PatternTestBase<NoteLineParts> {
    @BeforeAll
    static void displayPatterns() throws Exception {
        splitPrintPattern("Heading", ParaNotePatterns.SUMMARY.matcher("%=abc"));
        splitPrintPattern("Note", ParaNotePatterns.NOTE.matcher("%abc"));
        splitPrintPattern("Source", ParaNotePatterns.FIELD.matcher("%>author:john smith"));
    }

    @Test
    void testNoteHeadingWithGoodId() {
        final Matcher match = ParaNotePatterns.SUMMARY.matcher("%=avd #cat:id");
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertGroup("cat:id", match, NoteLineParts.ID, 5);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithBadId() {
        final Matcher match = ParaNotePatterns.SUMMARY.matcher("%=avd #cat?id");
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertGroup("cat?id", match, NoteLineParts.ERROR, 5);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithEmptyId() {
        final Matcher match = ParaNotePatterns.SUMMARY.matcher("%=avd #");
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithNoId() {
        final Matcher match = ParaNotePatterns.SUMMARY.matcher("%=avd");
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd", match, NoteLineParts.TITLE, 3);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithNoHeading() {
        final Matcher match = ParaNotePatterns.SUMMARY.matcher("%=#cat?id");
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertGroup("#", match, NoteLineParts.IDER, 3);
        assertGroup("cat?id", match, NoteLineParts.ERROR, 4);
        assertEnd(match);
    }

    @Test
    void testEmptyNoteHeading() {
        final Matcher match = ParaNotePatterns.SUMMARY.matcher("%=");
        assertGroup("%=", match, NoteLineParts.HEADING, 2);
        assertEnd(match);
    }

    @Test
    void testNote() {
        final Matcher match = ParaNotePatterns.NOTE.matcher("%details");
        assertGroup("%", match, NoteLineParts.NOTE, 2);
        assertGroup("details", match, NoteLineParts.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testEmptyNote() {
        final Matcher match = ParaNotePatterns.NOTE.matcher("%");
        assertGroup("%", match, NoteLineParts.NOTE, 2);
        assertEnd(match);
    }

    @Test
    void testCorrectSource() {
        final Matcher match = ParaNotePatterns.FIELD.matcher("%>author=john smith");
        assertGroup("%>", match, NoteLineParts.FIELD, 2);
        assertGroup("author", match, NoteLineParts.KEY, 3);
        assertGroup("=", match, NoteLineParts.FIELDER, 4);
        assertGroup("john smith", match, NoteLineParts.VALUE, 5);
        assertEnd(match);
    }

    @Test
    void testErrorSource() {
        final Matcher match = ParaNotePatterns.FIELD.matcher("%>author\\=john smith");
        assertGroup("%>", match, NoteLineParts.FIELD, 2);
        assertGroup("author\\=john smith", match, NoteLineParts.ERROR, 3);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "=text", "!! todo ", "!=outline" })
    void testNonNote(String text) {
        Assertions.assertAll(
                () -> Assertions.assertNull(ParaNotePatterns.SUMMARY.matcher(text), "Heading"),
                () -> Assertions.assertNull(ParaNotePatterns.NOTE.matcher(text), "Note"),
                () -> Assertions.assertNull(ParaNotePatterns.FIELD.matcher(text), "Source")
        );
    }
}
