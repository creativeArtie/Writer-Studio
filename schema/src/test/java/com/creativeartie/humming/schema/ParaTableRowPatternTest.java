package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

final class ParaTableRowPatternTest extends PatternTestBase<ParaTableRowPattern> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern(ParaTableRowPattern.matcher("|Hello"));
    }

    @Test
    void singleCellTest() {
        Matcher match = ParaTableRowPattern.matcher("|Col1");
        match.find();
        assertGroup("|", match, ParaTableRowPattern.SEP, 1);
        assertGroup("Col1", match, ParaTableRowPattern.TEXT, 2);
        match.find();
        assertEnd(match);
    }

    @Test
    void testStartOnly() {
        Matcher match = ParaTableRowPattern.matcher("|");
        match.find();
        assertGroup("|", match, ParaTableRowPattern.END, 1);
        match.find();
        assertEnd(match);
    }

    @Test
    void testStartMidOnly() {
        Matcher match = ParaTableRowPattern.matcher("|\n");
        match.find();
        assertGroup("|\n", match, ParaTableRowPattern.END, 1);
        match.find();
        assertEnd(match);
    }

    @Test
    void testNormalRow() {
        Matcher match = ParaTableRowPattern.matcher("|Col1|Co2|\n");
        match.find();
        assertGroup("|", match, ParaTableRowPattern.SEP, 1);
        assertGroup("Col1", match, ParaTableRowPattern.TEXT, 2);
        match.find();
        assertGroup("|", match, ParaTableRowPattern.SEP, 3);
        assertGroup("Co2", match, ParaTableRowPattern.TEXT, 4);
        match.find();
        assertGroup("|\n", match, ParaTableRowPattern.END, 5);
        match.find();
        assertEnd(match);
    }
}
