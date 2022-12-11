package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

public class PatternTestBase {
    protected String assertGroup(
        String expect, Matcher match, PatternEnum pattern, int group
    ) {
        String message = "Match " + Integer.toString(group);
        String matched = pattern.group(match);
        Assertions.assertEquals(expect, matched, message);
        return matched;

    }

    protected void assertEnd(Matcher match) {
        Assertions.assertFalse(match.find(), "Search end");
    }

    protected void assertFail(Executable test) {
        Assertions.assertThrows(IllegalArgumentException.class, test);

    }
}
