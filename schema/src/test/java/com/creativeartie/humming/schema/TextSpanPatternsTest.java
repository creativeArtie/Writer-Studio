package com.creativeartie.humming.schema;

import java.util.regex.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.TextSpanPatterns.*;

final class TextSpanPatternsTest extends PatternTestBase<TextSpanParts> {
    @BeforeAll
    static void displayPattern() {
        final String raw = "abc";
        for (final TextSpanPatterns pattern : TextSpanPatterns.values())
            splitPrintPattern(pattern.getPatternName(), pattern.matcher(raw));
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123", "Hello World Fun", "  Hello" })
    void testBasicId(String raw) {
        final Matcher matcher = TextSpanPatterns.ID.matcher(raw);
        matcher.find();
        assertGroup(raw, matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource(TextSpanPatterns.class)
    void testEscapedText(TextSpanPatterns pattern) {
        final Matcher matcher = pattern.matcher("avdd\\-ade");
        matcher.find();
        assertGroup("avdd", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        matcher.find();
        assertGroup("\\-", matcher, TextSpanPatterns.TextSpanParts.ESCAPE, 2);
        matcher.find();
        assertGroup("ade", matcher, TextSpanPatterns.TextSpanParts.TEXT, 3);
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource(TextSpanPatterns.class)
    void testEscapeEndless(TextSpanPatterns pattern) {
        final Matcher matcher = pattern.matcher("\\");
        matcher.find();
        assertGroup("\\", matcher, TextSpanPatterns.TextSpanParts.ESCAPE, 1);
        assertEnd(matcher);
    }

    static Stream<? extends Arguments> getLeftOvers() {
        return Stream.<Arguments>of(

                Arguments.of(" ", TextSpanPatterns.KEY), Arguments.of(":", TextSpanPatterns.KEY),

                Arguments.of("\n", TextSpanPatterns.HEADING), Arguments.of("#", TextSpanPatterns.HEADING),
                Arguments.of("{", TextSpanPatterns.HEADING),

                Arguments.of("\n", TextSpanPatterns.ID), Arguments.of("-", TextSpanPatterns.ID),
                Arguments.of("*", TextSpanPatterns.ID),

                Arguments.of("}", TextSpanPatterns.SPECIAL), Arguments.of("\n", TextSpanPatterns.SPECIAL),
                Arguments.of("*", TextSpanPatterns.SPECIAL),

                Arguments.of("\n", TextSpanPatterns.TEXT), Arguments.of("{", TextSpanPatterns.TEXT),

                Arguments.of("\n", TextSpanPatterns.SIMPLE),

                Arguments.of("\n", TextSpanPatterns.NOTE), Arguments.of("*", TextSpanPatterns.NOTE)
        );
    }

    @ParameterizedTest
    @MethodSource("getLeftOvers")
    void testWithLeftover(String ender, TextSpanPatterns tester) {
        final String raw = "text" + ender;
        final Matcher matcher = tester.matcher(raw);
        matcher.find();
        assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        assertEnd(matcher);
    }
}
