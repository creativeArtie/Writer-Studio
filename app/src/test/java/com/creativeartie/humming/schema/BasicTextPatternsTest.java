package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class BasicTextPatternsTest extends PatternTestBase<BasicTextPart> {

    @BeforeAll
    static void displayPattern() {
        String raw = "abc";
        for (BasicTextPatterns pattern : BasicTextPatterns.values()) {
            System.out.printf(
                "%10s: %s\n", pattern.getPatternName(),
                pattern.matcher(raw).pattern().pattern()
            );
        }
    }

    @ParameterizedTest
    @ValueSource(
        strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123",
            "Hello World Fun", "  Hello" }
    )
    void testBasicId(String raw) {
        Matcher matcher = BasicTextPatterns.ID.matcher(raw);
        assertGroup(raw, matcher, BasicTextPart.TEXT, 1);
        assertEnd(matcher);
    }

    @Test
    void testEscapedId() {
        Matcher matcher = BasicTextPatterns.ID.matcher("avdd\\-ade");
        assertGroup("avdd", matcher, BasicTextPart.TEXT, 1);
        assertGroup("\\-", matcher, BasicTextPart.ESCAPE, 2);
        assertGroup("ade", matcher, BasicTextPart.TEXT, 3);
        assertEnd(matcher);
    }

    @ParameterizedTest
    @CsvSource(
        { "n,ID", "-,ID", "|,LINK", "},LINK", "n,LINK", "},SPECIAL",
            "n,SPECIAL" }
    )
    void testWithLeftover(String ender, String type) {
        if (ender.equals("n")) {
            ender = "\n";
        }
        BasicTextPatterns tester = BasicTextPatterns.valueOf(type);
        String raw = "text" + ender;
        Matcher matcher = tester.matcher(raw);
        assertGroup("text", matcher, BasicTextPart.TEXT, 1);
        assertEnd(matcher);
    }

}
