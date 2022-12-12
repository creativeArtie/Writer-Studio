package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.google.common.base.*;

class ListPatternTest extends PatternTestBase<ListPattern> {

    @BeforeAll
    static void displayPattern() throws Exception {
        String pattern = ListPattern.matcher("#").pattern().pattern();
        pattern = Joiner.on("\n(?").join(Splitter.on("(?").split(pattern));
        System.out.println(pattern);
    }

    @Test
    void testNumbered() {
        Matcher match = ListPattern.matcher("##adde");
        assertGroup("##", match, ListPattern.NUMBERED, 1);
        assertGroup("adde", match, ListPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testBullet() {
        Matcher match = ListPattern.matcher("--dde");
        assertGroup("--", match, ListPattern.BULLET, 1);
        assertGroup("dde", match, ListPattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testNumberedBullet() {
        Matcher match = ListPattern.matcher("##--");
        assertGroup("##", match, ListPattern.NUMBERED, 1);
        assertGroup("--", match, ListPattern.TEXT, 2);
        assertEnd(match);
    }

}
