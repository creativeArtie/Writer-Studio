package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.TableCellPatterns.*;

class TableCellPatternsTest extends PatternTestBase<TableCellParts> {
    @BeforeAll
    static void printPatterns() {
        splitPrintPattern("Heading", TableCellPatterns.HEADING.matcher("|=title"));
        splitPrintPattern("Text", TableCellPatterns.TEXT.matcher("|title"));
    }

    @Test
    void testHeading() {
        Matcher match = TableCellPatterns.HEADING.matcher("|=title");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("=", match, TableCellParts.HEADER, 2);
        assertGroup("title", match, TableCellParts.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testSubHeading1() {
        Matcher match = TableCellPatterns.SUBHEAD.matcher("|=title");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("=", match, TableCellParts.HEADER, 2);
        assertGroup("title", match, TableCellParts.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testSubHeading2() {
        Matcher match = TableCellPatterns.SUBHEAD.matcher("|title");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("title", match, TableCellParts.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testCell() {
        Matcher match = TableCellPatterns.TEXT.matcher("|text");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("text", match, TableCellParts.TEXT, 2);
        assertEnd(match);
    }

    @Test
    void testCellCols() {
        Matcher match = TableCellPatterns.TEXT.matcher("|^^^^text\\*Hello");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("^^^^", match, TableCellParts.ROWS, 2);
        assertGroup("text\\*Hello", match, TableCellParts.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testHeadingRows() {
        Matcher match = TableCellPatterns.HEADING.matcher("|^=text");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("^", match, TableCellParts.ROWS, 2);
        assertGroup("=", match, TableCellParts.HEADER, 3);
        assertGroup("text", match, TableCellParts.TEXT, 4);
        assertEnd(match);
    }

    @Test
    void testHeadingCols() {
        Matcher match = TableCellPatterns.HEADING.matcher("||||=text");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("|||", match, TableCellParts.COLS, 2);
        assertGroup("=", match, TableCellParts.HEADER, 3);
        assertGroup("text", match, TableCellParts.TEXT, 4);
        assertEnd(match);
    }

    @Test
    void testCellMixed() {
        Matcher match = TableCellPatterns.TEXT.matcher("|||^Hello");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("||", match, TableCellParts.COLS, 2);
        assertGroup("^Hello", match, TableCellParts.TEXT, 3);
        assertEnd(match);
    }

    @Test
    void testCellSpace() {
        Matcher match = TableCellPatterns.TEXT.matcher("|| ");
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("|", match, TableCellParts.COLS, 2);
        assertGroup(" ", match, TableCellParts.TEXT, 3);
        assertEnd(match);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "|^|abc", "|abc|", "||" })
    void testFails(String test) {
        Assertions.assertNull(TableCellPatterns.TEXT.matcher(test), "Text matcher");
        Assertions.assertNull(TableCellPatterns.HEADING.matcher(test), "heading matcher");
    }

    @Test
    void testHeadingFailed() {
        String text = "|123";
        Assertions.assertNull(TableCellPatterns.HEADING.matcher(text), "heading matcher");
        Matcher match = TableCellPatterns.TEXT.matcher(text);
        assertGroup("|", match, TableCellParts.START, 1);
        assertGroup("123", match, TableCellParts.TEXT, 2);
        assertEnd(match);
    }
}
