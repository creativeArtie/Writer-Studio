package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class TableCellTest extends SpanBranchTestBase {
    @Test
    void testCell() {
        TableCell test = TableCell.newTextCell(newParent(), "|abc");
        addStyleTest("|", StyleClasses.OPERATOR);
        addStyleTest("abc", StyleClasses.TEXT);
        testStyles(test);
        Assertions.assertEquals(1, test.getColsSpan());
        Assertions.assertEquals(1, test.getRowsSpan());
        Assertions.assertEquals(false, test.isHead());
    }

    @Test
    void testSpanedFormatedCell() {
        TableCell test = TableCell.newTextCell(newParent(), "||Hello*WORLD*!!");
        addStyleTest("|", StyleClasses.OPERATOR);
        addStyleTest("|", StyleClasses.OPERATOR);
        addStyleTest("Hello", StyleClasses.TEXT);
        addStyleTest("*", StyleClasses.OPERATOR);
        addStyleTest("WORLD", StyleClasses.BOLD, StyleClasses.TEXT);
        addStyleTest("*", StyleClasses.OPERATOR);
        addStyleTest("!!", StyleClasses.TEXT);
        testStyles(test);
        Assertions.assertEquals(2, test.getColsSpan());
        Assertions.assertEquals(1, test.getRowsSpan());
        Assertions.assertEquals(false, test.isHead());
    }
}
