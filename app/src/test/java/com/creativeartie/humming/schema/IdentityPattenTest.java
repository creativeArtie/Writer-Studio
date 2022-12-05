package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class IdentityPattenTest {

    @ParameterizedTest
    @ValueSource(strings = { "{deadsa", "cat-", "category-id}" })
    void testFailedId(String test) {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> IdentityPattern.match(test)
        );
    }

    @Test
    void testPassedId() {
        Matcher match = IdentityPattern.match("category-id");

        Assertions.assertTrue(match.find(), "group 1");
        Assertions.assertEquals(
            "category", IdentityPattern.NAME.group(match), "group 1"
        );

        Assertions.assertTrue(match.find(), "group 2");
        Assertions
            .assertEquals("-", IdentityPattern.SEP.group(match), "group 2");

        Assertions.assertTrue(match.find(), "group 3");
        Assertions
            .assertEquals("id", IdentityPattern.NAME.group(match), "group 3");

        Assertions.assertFalse(match.find());
    }

}
