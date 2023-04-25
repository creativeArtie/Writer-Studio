package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.TextFormattedPatterns.*;

@SuppressWarnings("nls")
final class TextFormattedPatternTest extends PatternTestBase<TextFormattedPatterns.TextFormattedParts> {
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        for (TextFormattedPatterns pattern : TextFormattedPatterns.values()) {
            splitPrintPattern(pattern.name(), pattern.matcher("abc"));
        }
    }

    @ParameterizedTest
    @EnumSource
    void testFormated(TextFormattedPatterns pattern) {
        final Matcher match = pattern.matcher("*`abc`*avd");
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

    @ParameterizedTest
    @EnumSource
    void testRef(TextFormattedPatterns pattern) {
        final Matcher match = pattern.matcher("{!avd}{^add}{dadd}add");

        match.find();
        if (pattern == TextFormattedPatterns.NOTE) {
            assertGroup("{!avd}", match, TextFormattedParts.TODO, 1);
            match.find();
            assertGroup("{^add}{dadd}add", match, TextFormattedParts.TEXT, 2);
        } else {
            assertGroup("{!avd}", match, TextFormattedParts.TODO, 1);
            match.find();
            assertGroup("{^add}", match, TextFormattedParts.REFER, 2);
            match.find();
            assertGroup("{dadd}", match, TextFormattedParts.REFER, 3);
            match.find();
            assertGroup("add", match, TextFormattedParts.TEXT, 4);
        }
        assertEnd(match);
    }
}
