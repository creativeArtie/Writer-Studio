package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

public class PatternTestBase<T extends PatternEnum> {

    private int expectedLength;
    private Class<? extends PatternEnum> patternClass;

    protected String
        assertGroup(String expect, Matcher match, T pattern, int group) {
        String message = "Match " + Integer.toString(group);
        String matched = pattern.group(match);
        Assertions.assertEquals(expect, matched, message);
        expectedLength += expect.length();
        patternClass = pattern.getClass();
        return matched;

    }

    @BeforeEach
    protected void resetCounter() {
        expectedLength = 0;
    }

    protected void assertEnd(Matcher match) {
        if (patternClass.getEnumConstants()[0].runFind()) {
            Assertions.assertFalse(match.find(), "Search end");
        } else {
            Assertions.assertEquals(expectedLength, match.end(), "Search end");
        }
    }

    protected void assertFail(Executable test) {
        Assertions.assertThrows(IllegalArgumentException.class, test);

    }
}
