package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.BasicTextPatterns.*;

class BasicTextPatternsTest extends PatternTestBase<BasicTextPart> {
    @BeforeAll
    static void displayPattern() {
        final String raw = "abc";
        for (final BasicTextPatterns pattern : BasicTextPatterns.values()) {
            splitPrintPattern(pattern.getPatternName(), pattern.matcher(raw));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123", "Hello World Fun", "  Hello" })
    void testBasicId(String raw) {
        final Matcher matcher = BasicTextPatterns.ID.matcher(raw);
        assertGroup(raw, matcher, BasicTextPatterns.BasicTextPart.TEXT, 1);
        assertEnd(matcher);
    }

    @Test
    void testEscapedId() {
        final Matcher matcher = BasicTextPatterns.ID.matcher("avdd\\-ade");
        assertGroup("avdd", matcher, BasicTextPatterns.BasicTextPart.TEXT, 1);
        assertGroup("\\-", matcher, BasicTextPatterns.BasicTextPart.ESCAPE, 2);
        assertGroup("ade", matcher, BasicTextPatterns.BasicTextPart.TEXT, 3);
        assertEnd(matcher);
    }

    @ParameterizedTest
    @CsvSource({ "n,ID", "-,ID", "|,LINK", "},LINK", "n,LINK", "},SPECIAL", "n,SPECIAL" })
    void testWithLeftover(String ender, String type) {
        if (ender.equals("n")) {
            ender = "\n";
        }
        final BasicTextPatterns tester = BasicTextPatterns.valueOf(type);
        final String raw = "text" + ender;
        final Matcher matcher = tester.matcher(raw);
        assertGroup("text", matcher, BasicTextPatterns.BasicTextPart.TEXT, 1);
        assertEnd(matcher);
    }
}
