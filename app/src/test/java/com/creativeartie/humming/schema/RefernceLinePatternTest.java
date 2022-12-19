package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.ReferenceLinePatterns.*;

class RefernceLinePatternTest extends PatternTestBase<RefLineParts> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern("Link", ReferenceLinePatterns.LINK.matcher("!@test"));
    }

    @Test
    void testLink() {
        Matcher matcher = ReferenceLinePatterns.LINK.matcher("!@test=www.example.com");
        assertGroup("!", matcher, RefLineParts.START, 1);
        assertGroup("@", matcher, RefLineParts.LINK, 2);
        assertGroup("test", matcher, RefLineParts.ID, 3);
        assertGroup("=", matcher, RefLineParts.SEP, 4);
        assertGroup("www.example.com", matcher, RefLineParts.TEXT, 5);
        assertEnd(matcher);
    }
}
