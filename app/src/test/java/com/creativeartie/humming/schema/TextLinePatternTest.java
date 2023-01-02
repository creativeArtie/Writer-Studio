package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.TextLinePatterns.*;

class TextLinePatternTest extends PatternTestBase<TextLinePatterns.TextLinePart> {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        splitPrintPattern(TextLinePatterns.BASIC.matcher("abc"));
    }

    @Test
    void testFormated() {
        final Matcher match = TextLinePatterns.BASIC.matcher("*`abc`*avd");
        match.find();
        assertGroup("*", match, TextLinePart.BOLD, 1);
        match.find();
        assertGroup("`", match, TextLinePart.ITALICS, 2);
        match.find();
        assertGroup("abc", match, TextLinePart.TEXT, 3);
        match.find();
        assertGroup("`", match, TextLinePart.ITALICS, 4);
        match.find();
        assertGroup("*", match, TextLinePart.BOLD, 5);
        match.find();
        assertGroup("avd", match, TextLinePart.TEXT, 1);
        assertEnd(match);
    }

    @Test
    void testRef() {
        final Matcher match = TextLinePatterns.BASIC.matcher("{!avd}{^add}{dadd}add");
        match.find();
        assertGroup("{!avd}", match, TextLinePart.TODO, 1);
        match.find();
        assertGroup("{^add}", match, TextLinePart.REFER, 2);
        match.find();
        assertGroup("{dadd}", match, TextLinePart.REFER, 3);
        match.find();
        assertGroup("add", match, TextLinePart.TEXT, 4);
        assertEnd(match);
    }
}
