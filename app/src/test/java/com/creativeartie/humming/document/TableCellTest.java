package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class TableCellTest extends SpanBranchTestBase {
    @Test
    void testCell() {
        TableCell test = TableCell.newTextCell(newParent(), "|abc");
        addStyleTest("|", SpanStyles.TEXTCELL, SpanStyles.OPERATOR);
        addStyleTest("abc", SpanStyles.TEXTCELL, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertEquals(1, test.getColsSpan(), "ColsSpan");
        Assertions.assertEquals(1, test.getRowsSpan(), "RowSpan");
        Assertions.assertEquals(false, test.isHead(), "isHead");
    }

    @Test
    void testSpanedFormatedCell() {
        TableCell test = TableCell.newTextCell(newParent(), "||Hello*WORLD*!!");
        addStyleTest("|", SpanStyles.TEXTCELL, SpanStyles.OPERATOR);
        addStyleTest("|", SpanStyles.TEXTCELL, SpanStyles.OPERATOR);
        addStyleTest("Hello", SpanStyles.TEXTCELL, SpanStyles.TEXT);
        addStyleTest("*", SpanStyles.TEXTCELL, SpanStyles.OPERATOR);
        addStyleTest("WORLD", SpanStyles.TEXTCELL, SpanStyles.BOLD, SpanStyles.TEXT);
        addStyleTest("*", SpanStyles.TEXTCELL, SpanStyles.OPERATOR);
        addStyleTest("!!", SpanStyles.TEXTCELL, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertEquals(2, test.getColsSpan(), "ColsSpan");
        Assertions.assertEquals(1, test.getRowsSpan(), "RowSpan");
        Assertions.assertEquals(false, test.isHead(), "isHead");
    }

    @Test
    void testSpanedTableHeader() {
        TableCell test = TableCell.newHeadingCell(newParent(), "||Hello*WORLD*!!");
        addStyleTest("|", SpanStyles.HEADCELL, SpanStyles.OPERATOR);
        addStyleTest("|", SpanStyles.HEADCELL, SpanStyles.OPERATOR);
        addStyleTest("Hello", SpanStyles.HEADCELL, SpanStyles.TEXT);
        addStyleTest("*", SpanStyles.HEADCELL, SpanStyles.OPERATOR);
        addStyleTest("WORLD", SpanStyles.HEADCELL, SpanStyles.BOLD, SpanStyles.TEXT);
        addStyleTest("*", SpanStyles.HEADCELL, SpanStyles.OPERATOR);
        addStyleTest("!!", SpanStyles.HEADCELL, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertEquals(2, test.getColsSpan(), "ColsSpan");
        Assertions.assertEquals(1, test.getRowsSpan(), "RowSpan");
        Assertions.assertEquals(true, test.isHead(), "isHead");
    }

    @Test
    void testTableHeader() {
        TableCell test = TableCell.newHeadingCell(newParent(), "|=abc");
        addStyleTest("|", SpanStyles.HEADCELL, SpanStyles.OPERATOR);
        addStyleTest("abc", SpanStyles.HEADCELL, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertEquals(1, test.getColsSpan(), "ColsSpan");
        Assertions.assertEquals(1, test.getRowsSpan(), "RowSpan");
        Assertions.assertEquals(true, test.isHead(), "isHead");
    }
}
