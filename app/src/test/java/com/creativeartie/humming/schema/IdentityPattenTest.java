package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class IdentityPattenTest extends PatternTestBase<IdentityPattern> {

    @BeforeAll
    static void displayPattern() {
        System.out.println(IdentityPattern.matcher("abc").pattern().pattern());
        System.out.println(IdentityPattern.getFullPattern());
    }

    @ParameterizedTest
    @ValueSource(strings = { "{deadsa", "cat:", "category:id}" })
    void testFailedId(String test) {
        assertFail(() -> IdentityPattern.matcher(test));
    }

    @Test
    void testPassedId() {
        Matcher match = IdentityPattern.matcher("category:id");

        assertGroup("category", match, IdentityPattern.NAME, 1);
        assertGroup(":", match, IdentityPattern.SEP, 2);
        assertGroup("id", match, IdentityPattern.NAME, 3);
        assertEnd(match);
    }

}
