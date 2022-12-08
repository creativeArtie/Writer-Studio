package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.google.common.base.*;

class FormattedPatternTest extends PatternTestBase {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        String pattern = FormattedPattern.matcher("abc", BasicTextPatterns.ID)
            .pattern().pattern();
        pattern = Joiner.on(")|\n(").join(Splitter.on(")|(").split(pattern));
        System.out.println(pattern);
    }

    @Test
    void testFormated() {
        Matcher match =
            FormattedPattern.matcher("*`abc`*avd", BasicTextPatterns.ID);
        assertGroup("*", match, FormattedPattern.BOLD, 1);
        assertGroup("`", match, FormattedPattern.ITALICS, 2);
        assertGroup("abc", match, FormattedPattern.TEXT, 3);
        assertGroup("`", match, FormattedPattern.ITALICS, 4);
        assertGroup("*", match, FormattedPattern.BOLD, 5);
        assertGroup("avd", match, FormattedPattern.TEXT, 1);
        assertEnd(match);
    }

    @Test
    void testRef() {
        Matcher match = FormattedPattern
            .matcher("{!avd}{^add}{dadd}add", BasicTextPatterns.TEXT);
        assertGroup("{!avd}", match, FormattedPattern.TODO, 1);
        assertGroup("{^add}", match, FormattedPattern.REFER, 2);
        assertGroup("{dadd}", match, FormattedPattern.ERR, 3);
        assertGroup("add", match, FormattedPattern.TEXT, 4);
        assertEnd(match);

    }

}
