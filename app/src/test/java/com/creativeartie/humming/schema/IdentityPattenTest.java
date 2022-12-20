package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class IdentityPattenTest extends PatternTestBase<IdentityPattern> {
    @BeforeAll
    static void displayPattern() {
        splitPrintPattern("Matcher", IdentityPattern.matcher("abc"));
        System.out.println(IdentityPattern.getFullPattern());
    }

    @ParameterizedTest
    @ValueSource(strings = { "{deadsa", "cat:", "category:id}" })
    void testFailedId(String test) {
        Assertions.assertNull(IdentityPattern.matcher(test));
    }

    @Test
    void testPassedId() {
        final Matcher match = IdentityPattern.matcher("category:id");

        assertGroup("category", match, IdentityPattern.NAME, 1);
        assertGroup(":", match, IdentityPattern.SEP, 2);
        assertGroup("id", match, IdentityPattern.NAME, 3);
        assertEnd(match);
    }
}
