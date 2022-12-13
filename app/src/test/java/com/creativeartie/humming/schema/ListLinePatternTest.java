package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.google.common.base.*;

class ListLinePatternTest extends PatternTestBase<ListLinePattern> {

    @BeforeAll
    static void displayPattern() throws Exception {
        String pattern = ListLinePattern.matcher("#").pattern().pattern();
        pattern = Joiner.on("\n(?").join(Splitter.on("(?").split(pattern));
        System.out.println(pattern);
    }

    @Test
    void testNumbered() {
        Matcher match = ListLinePattern.matcher("##adde");
        assertGroup("##", match, ListLinePattern.NUMBERED, 1);
        assertGroup("adde", match, ListLinePattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testBullet() {
        Matcher match = ListLinePattern.matcher("--dde");
        assertGroup("--", match, ListLinePattern.BULLET, 1);
        assertGroup("dde", match, ListLinePattern.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testNumberedBullet() {
        Matcher match = ListLinePattern.matcher("##--");
        assertGroup("##", match, ListLinePattern.NUMBERED, 1);
        assertGroup("--", match, ListLinePattern.TEXT, 2);
        assertEnd(match);
    }

}
