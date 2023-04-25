package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.TextSpanPatterns.*;

@SuppressWarnings("nls")
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
    @ValueSource(strings = { "abc", "  Hello", "Hello_abc" })
    void testBasicKey(String raw) {
        final Matcher matcher = TextSpanPatterns.KEY.matcher(raw);
        matcher.find();
        assertGroup(raw, matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource
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

    @ParameterizedTest
    @EnumSource
    void testBoldEnd(TextSpanPatterns pattern) {
        final String raw = "text*";
        final Matcher matcher = pattern.matcher(raw);
        matcher.find();
        switch (pattern) {
            case ERROR:
                assertGroup(raw, matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
                break;
            case CELL:
            case HEADING:
            case ID:
            case KEY:
            case NOTE:
            case SPECIAL:
            case TEXT:
                assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        }
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource
    void testItalicsEnd(TextSpanPatterns pattern) {
        final String raw = "text`";
        final Matcher matcher = pattern.matcher(raw);
        matcher.find();
        switch (pattern) {
            case ERROR:
                assertGroup(raw, matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
                break;
            case CELL:
            case HEADING:
            case ID:
            case KEY:
            case NOTE:
            case SPECIAL:
            case TEXT:
                assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        }
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource
    void testUnderlineEnd(TextSpanPatterns pattern) {
        final String raw = "text_";
        final Matcher matcher = pattern.matcher(raw);
        matcher.find();
        switch (pattern) {
            case ERROR:
            case ID:
            case KEY:
                assertGroup(raw, matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
                break;
            case CELL:
            case HEADING:
            case NOTE:
            case SPECIAL:
            case TEXT:
                assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        }
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource
    void testLinend(TextSpanPatterns pattern) {
        final String raw = "text\n";
        final Matcher matcher = pattern.matcher(raw);
        matcher.find();
        assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource
    void testRefStart(TextSpanPatterns pattern) {
        final String raw = "text{";
        final Matcher matcher = pattern.matcher(raw);
        matcher.find();
        switch (pattern) {
            case ERROR:
            case SPECIAL:
                assertGroup(raw, matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
                break;
            case NOTE:
            case TEXT:
            case ID:
            case KEY:
            case CELL:
            case HEADING:
                assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
        }
        assertEnd(matcher);
    }

    @ParameterizedTest
    @EnumSource
    void testTodoStart(TextSpanPatterns pattern) {
        final String raw = "text{!";
        final Matcher matcher = pattern.matcher(raw);
        matcher.find();
        switch (pattern) {
            case ERROR:
            case SPECIAL:
                assertGroup(raw, matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
                break;
            case ID:
            case KEY:
                assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
                break;
            case NOTE:
            case TEXT:
            case CELL:
            case HEADING:
                assertGroup("text", matcher, TextSpanPatterns.TextSpanParts.TEXT, 1);
                matcher.find();
                // agenda pattern is not matched here, making "!" being matched as text
                assertGroup("!", matcher, TextSpanPatterns.TextSpanParts.TEXT, 2);
                return; // length missing "{"
        }
        assertEnd(matcher);
    }
}
