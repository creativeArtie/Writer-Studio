package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.TextFormattedPatterns.*;

class TextFormattedPatternTest extends PatternTestBase<TextFormattedPatterns.TextFormattedParts> {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        splitPrintPattern(TextFormattedPatterns.BASIC.matcher("abc"));
    }

    @Test
    void testFormated() {
        final Matcher match = TextFormattedPatterns.BASIC.matcher("*`abc`*avd");
        match.find();
        assertGroup("*", match, TextFormattedParts.BOLD, 1);
        match.find();
        assertGroup("`", match, TextFormattedParts.ITALICS, 2);
        match.find();
        assertGroup("abc", match, TextFormattedParts.TEXT, 3);
        match.find();
        assertGroup("`", match, TextFormattedParts.ITALICS, 4);
        match.find();
        assertGroup("*", match, TextFormattedParts.BOLD, 5);
        match.find();
        assertGroup("avd", match, TextFormattedParts.TEXT, 1);
        assertEnd(match);
    }

    @Test
    void testRef() {
        final Matcher match = TextFormattedPatterns.BASIC.matcher("{!avd}{^add}{dadd}add");
        match.find();
        assertGroup("{!avd}", match, TextFormattedParts.TODO, 1);
        match.find();
        assertGroup("{^add}", match, TextFormattedParts.REFER, 2);
        match.find();
        assertGroup("{dadd}", match, TextFormattedParts.REFER, 3);
        match.find();
        assertGroup("add", match, TextFormattedParts.TEXT, 4);
        assertEnd(match);
    }
}
