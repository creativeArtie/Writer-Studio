package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

public class PatternTestBase {
    protected void assertGroup(
        String expect, Matcher match, PatternEnum pattern, int group
    ) {
        String message = "Match " + Integer.toString(group);
        Assertions.assertEquals(expect, pattern.group(match), message);

    }

    protected void assertEnd(Matcher match) {
        Assertions.assertFalse(match.find(), "Search end");
    }

    protected void assertFail(Executable test) {
        Assertions.assertThrows(IllegalArgumentException.class, test);

    }
}
