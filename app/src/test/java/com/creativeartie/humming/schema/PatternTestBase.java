package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import com.google.common.base.*;

public class PatternTestBase<T extends PatternEnum> {
    private int expectedLength;

    /**
     * Asserts a PatternEnum against an expected text
     *
     * @param expect
     * @param match
     *        the Matcher, will call {@linkplain Matcher#find()} as needed
     * @param pattern
     * @param group
     *
     * @return
     */
    protected String assertGroup(String expect, Matcher match, T pattern, int group) {
        final String message = "Matching " + pattern.getPatternName() + " group " + Integer.toString(group);
        final String matched = pattern.group(match);
        Assertions.assertEquals(expect, matched, message);
        expectedLength += expect.length();
        return matched;
    }

    @BeforeEach
    protected void resetCounter() {
        expectedLength = 0;
    }

    public static void splitPrintPattern(String name, Matcher match) {
        System.out.println(name);
        splitPrintPattern(match);
        System.out.println();
    }

    public static void splitPrintPattern(Matcher match) {
        System.out.println(
                Joiner.on("\n(?").join(Splitter.on("(?").split(match.pattern().pattern())).replaceFirst("\n", "")
        );
    }

    protected void assertEnd(Matcher match) {
        Assertions.assertEquals(expectedLength, match.end(), "Search end");
        Assertions.assertFalse(match.find(), "Search end");
    }

    protected void assertFail(Executable test) {
        Assertions.assertThrows(IllegalArgumentException.class, test);
    }
}
