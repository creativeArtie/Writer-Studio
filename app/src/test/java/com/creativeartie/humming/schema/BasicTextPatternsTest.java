package com.creativeartie.humming.schema;

import java.util.regex.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.BasicTextPatterns.*;

class BasicTextPatternsTest extends PatternTestBase<BasicTextPart> {
    @BeforeAll
    static void displayPattern() {
        final String raw = "abc";
        for (final BasicTextPatterns pattern : BasicTextPatterns.values())
            splitPrintPattern(pattern.getPatternName(), pattern.matcher(raw));
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123", "Hello World Fun", "  Hello" })
    void testBasicId(String raw) {
        final Matcher matcher = BasicTextPatterns.ID.matcher(raw);
        matcher.find();
        assertGroup(raw, matcher, BasicTextPatterns.BasicTextPart.TEXT, 1);
        assertEnd(matcher);
    }

    @Test
    void testEscapedId() {
        final Matcher matcher = BasicTextPatterns.ID.matcher("avdd\\-ade");
        matcher.find();
        assertGroup("avdd", matcher, BasicTextPatterns.BasicTextPart.TEXT, 1);
        matcher.find();
        assertGroup("\\-", matcher, BasicTextPatterns.BasicTextPart.ESCAPE, 2);
        matcher.find();
        assertGroup("ade", matcher, BasicTextPatterns.BasicTextPart.TEXT, 3);
        assertEnd(matcher);
    }

    static Stream<? extends Arguments> getLeftOvers() {
        return Stream.<Arguments>of(

                Arguments.of(" ", BasicTextPatterns.KEY), Arguments.of(":", BasicTextPatterns.KEY),
                Arguments.of("\\", BasicTextPatterns.KEY),

                Arguments.of("\n", BasicTextPatterns.HEADING), Arguments.of("#", BasicTextPatterns.HEADING),
                Arguments.of("{", BasicTextPatterns.HEADING), Arguments.of("\\", BasicTextPatterns.HEADING),

                Arguments.of("\n", BasicTextPatterns.ID), Arguments.of("-", BasicTextPatterns.ID),
                Arguments.of("*", BasicTextPatterns.ID), Arguments.of("\\", BasicTextPatterns.ID),

                Arguments.of("}", BasicTextPatterns.SPECIAL), Arguments.of("\n", BasicTextPatterns.SPECIAL),
                Arguments.of("*", BasicTextPatterns.SPECIAL), Arguments.of("\\", BasicTextPatterns.SPECIAL),

                Arguments.of("\n", BasicTextPatterns.TEXT), Arguments.of("{", BasicTextPatterns.TEXT),
                Arguments.of("\\", BasicTextPatterns.TEXT),

                Arguments.of("\n", BasicTextPatterns.SIMPLE), Arguments.of("\\", BasicTextPatterns.SIMPLE),

                Arguments.of("\n", BasicTextPatterns.NOTE), Arguments.of("*", BasicTextPatterns.NOTE),
                Arguments.of("\\", BasicTextPatterns.NOTE)
        );
    }

    @ParameterizedTest
    @MethodSource("getLeftOvers")
    void testWithLeftover(String ender, BasicTextPatterns tester) {
        final String raw = "text" + ender;
        final Matcher matcher = tester.matcher(raw);
        matcher.find();
        assertGroup("text", matcher, BasicTextPatterns.BasicTextPart.TEXT, 1);
        assertEnd(matcher);
    }
}
