package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.NoteLinePatterns.*;

class NoteLinePatternsTest extends PatternTestBase<NoteLineParts> {

    @BeforeAll
    static void displayPatterns() throws Exception {
        System.out.println(
            NoteLinePatterns.HEADING.matcher("!=abc").pattern().pattern()
        );
        System.out.println(
            NoteLinePatterns.NOTE.matcher("!%abc").pattern().pattern()
        );
        System.out.println(
            NoteLinePatterns.SOURCE.matcher("!>author:john smith").pattern()
                .pattern()
        );
    }

    @Test
    void testNoteHeadingWithGoodId() {
        Matcher match = NoteLinePatterns.HEADING.matcher("!=avd #cat:id");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertGroup("cat:id", match, NoteLineParts.ID, 5);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithBadId() {
        Matcher match = NoteLinePatterns.HEADING.matcher("!=avd #cat?id");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertGroup("cat?id", match, NoteLineParts.ERROR, 5);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithEmptyId() {
        Matcher match = NoteLinePatterns.HEADING.matcher("!=avd #");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd ", match, NoteLineParts.TITLE, 3);
        assertGroup("#", match, NoteLineParts.IDER, 4);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithNoId() {
        Matcher match = NoteLinePatterns.HEADING.matcher("!=avd");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("=", match, NoteLineParts.HEADING, 2);
        assertGroup("avd", match, NoteLineParts.TITLE, 3);
        assertEnd(match);
    }

    @Test
    void testNoteHeadingWithNoHeading() {
        Matcher match = NoteLinePatterns.HEADING.matcher("!=#cat?id");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("=", match, NoteLineParts.HEADING, 2);
        assertGroup("#", match, NoteLineParts.IDER, 3);
        assertGroup("cat?id", match, NoteLineParts.ERROR, 4);
        assertEnd(match);
    }

    @Test
    void testEmptyNoteHeading() {
        Matcher match = NoteLinePatterns.HEADING.matcher("!=");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("=", match, NoteLineParts.HEADING, 2);
        assertEnd(match);
    }

    @Test
    void testNote() {
        Matcher match = NoteLinePatterns.NOTE.matcher("!%details");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%", match, NoteLineParts.NOTE, 2);
        assertGroup("details", match, NoteLineParts.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testEmptyNote() {
        Matcher match = NoteLinePatterns.NOTE.matcher("!%");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup("%", match, NoteLineParts.NOTE, 2);
        assertEnd(match);
    }

    @Test
    void testCorrectSource() {
        Matcher match = NoteLinePatterns.SOURCE.matcher("!>author:john smith");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup(">", match, NoteLineParts.SOURCE, 2);
        assertGroup("author", match, NoteLineParts.FIELD, 3);
        assertGroup(":", match, NoteLineParts.SOURCER, 4);
        assertGroup("john smith", match, NoteLineParts.VALUE, 5);
        assertEnd(match);
    }

    @Test
    void testErrorSource() {
        Matcher match =
            NoteLinePatterns.SOURCE.matcher("!>author\\:john smith");
        assertGroup("!", match, NoteLineParts.STARTER, 1);
        assertGroup(">", match, NoteLineParts.SOURCE, 2);
        assertGroup("author\\:john smith", match, NoteLineParts.ERROR, 3);
        assertEnd(match);
    }

}
