package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

public class TableRowPatternTest extends PatternTestBase<TableRowPattern> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern(TableRowPattern.matcher("|Hello"));
    }

    @Test
    void singleCellTest() {
        Matcher match = TableRowPattern.matcher("|Col1");
        match.find();
        assertGroup("|", match, TableRowPattern.SEP, 1);
        assertGroup("Col1", match, TableRowPattern.TEXT, 2);
        match.find();
        assertEnd(match);
    }

    @Test
    void testStartOnly() {
        Matcher match = TableRowPattern.matcher("|");
        match.find();
        assertGroup("|", match, TableRowPattern.END, 1);
        match.find();
        assertEnd(match);
    }

    @Test
    void testStartMidOnly() {
        Matcher match = TableRowPattern.matcher("|\n");
        match.find();
        assertGroup("|\n", match, TableRowPattern.END, 1);
        match.find();
        assertEnd(match);
    }

    @Test
    void testNormalRow() {
        Matcher match = TableRowPattern.matcher("|Col1|Co2|\n");
        match.find();
        assertGroup("|", match, TableRowPattern.SEP, 1);
        assertGroup("Col1", match, TableRowPattern.TEXT, 2);
        match.find();
        assertGroup("|", match, TableRowPattern.SEP, 3);
        assertGroup("Co2", match, TableRowPattern.TEXT, 4);
        match.find();
        assertGroup("|\n", match, TableRowPattern.END, 5);
        match.find();
        assertEnd(match);
    }
}
