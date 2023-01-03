package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.LineTextPatterns.*;

class LineTextPatternTest extends PatternTestBase<LineTextPatterns.LineTextPart> {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        splitPrintPattern(LineTextPatterns.BASIC.matcher("abc"));
    }

    @Test
    void testFormated() {
        final Matcher match = LineTextPatterns.BASIC.matcher("*`abc`*avd");
        match.find();
        assertGroup("*", match, LineTextPart.BOLD, 1);
        match.find();
        assertGroup("`", match, LineTextPart.ITALICS, 2);
        match.find();
        assertGroup("abc", match, LineTextPart.TEXT, 3);
        match.find();
        assertGroup("`", match, LineTextPart.ITALICS, 4);
        match.find();
        assertGroup("*", match, LineTextPart.BOLD, 5);
        match.find();
        assertGroup("avd", match, LineTextPart.TEXT, 1);
        assertEnd(match);
    }

    @Test
    void testRef() {
        final Matcher match = LineTextPatterns.BASIC.matcher("{!avd}{^add}{dadd}add");
        match.find();
        assertGroup("{!avd}", match, LineTextPart.TODO, 1);
        match.find();
        assertGroup("{^add}", match, LineTextPart.REFER, 2);
        match.find();
        assertGroup("{dadd}", match, LineTextPart.REFER, 3);
        match.find();
        assertGroup("add", match, LineTextPart.TEXT, 4);
        assertEnd(match);
    }
}
