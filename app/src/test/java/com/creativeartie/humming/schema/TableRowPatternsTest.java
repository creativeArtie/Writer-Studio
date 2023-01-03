package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.TableRowPatterns.*;

class TableRowPatternsTest extends PatternTestBase<TableRowParts> {
    @BeforeAll
    public static void printPattern() {
        splitPrintPattern(TableRowPatterns.BASIC.matcher("|Hello|Heading1|"));
    }

    @Test
    void testHeaderRow() {
        Matcher match = TableRowPatterns.HEADER.matcher("|=Col1|Co2|\n");
        match.find();
        assertGroup("|=Col1", match, TableRowParts.SUBHEAD, 1);
        match.find();
        assertGroup("|Co2", match, TableRowParts.SUBHEAD, 2);
        match.find();
        assertGroup("|\n", match, TableRowParts.END, 3);
        match.find();
        assertGroup("", match, TableRowParts.END, 3);
        assertEnd(match);
    }

    @Test
    void testNormalRow() {
        Matcher match = TableRowPatterns.BASIC.matcher("|Col1|Co2|\n");
        match.find();
        assertGroup("|Col1", match, TableRowParts.TEXT, 1);
        match.find();
        assertGroup("|Co2", match, TableRowParts.TEXT, 2);
        match.find();
        assertGroup("|\n", match, TableRowParts.END, 3);
        match.find();
        assertGroup("", match, TableRowParts.END, 3);
        assertEnd(match);
    }
}
