package com.creativeartie.humming.schema;

import java.util.*;
import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.google.common.base.*;
import com.google.common.collect.*;

class LinkPatternTest {

    private List<String> correctParts;

    @BeforeAll
    void setUp() throws Exception {
        correctParts =
            ImmutableList.of("<", "http://example.com", "|", "Example", ">");
    }

    @Test
    void testFull() {
        String raw = Joiner.on(' ').join(correctParts);
        Matcher match = LinkPattern.match(raw);
        Assertions.assertTrue(match.find(), "group 1");
        Assertions.assertEquals("<", LinkPattern.START.group(match), "group 1");

        Assertions.assertTrue(match.find(), "group 2");
        Assertions.assertEquals(
            " http://example.com ", LinkPattern.LINK.group(match), "group 2"
        );

        Assertions.assertTrue(match.find(), "group 3");
        Assertions.assertEquals("|", LinkPattern.SEP.group(match), "group 3");

        Assertions.assertTrue(match.find(), "group 4");
        Assertions.assertEquals(
            " Example ", LinkPattern.TEXT.group(match), "group 4"
        );

        Assertions.assertTrue(match.find(), "group 5");
        Assertions.assertEquals(">", LinkPattern.TEXT.group(match), "group 5");
    }

}
