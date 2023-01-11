package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class TableCellTest extends SpanBranchTestBase<TableCell> {
    private enum SubTests {
        TEXT, HEADING;
    }

    private SubTests subTest;

    @Test
    void testCell() {
        subTest = SubTests.TEXT;
        TableCell test = newSpan("|abc");
        addStyleTest("|", SpanStyles.TEXTCELL, SpanStyles.OPERATOR);
        addStyleTest("abc", SpanStyles.TEXTCELL, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertEquals(1, test.getColsSpan(), "ColsSpan");
        Assertions.assertEquals(1, test.getRowsSpan(), "RowSpan");
        Assertions.assertEquals(false, test.isHead(), "isHead");
    }

    @Test
    void testSpanedFormatedCell() {
        subTest = SubTests.TEXT;
        TableCell test = newSpan("||Hello*WORLD*!!");
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
        subTest = SubTests.HEADING;
        TableCell test = newSpan("||Hello*WORLD*!!");
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
        subTest = SubTests.HEADING;
        TableCell test = newSpan("|=abc");
        addStyleTest("|", SpanStyles.HEADCELL, SpanStyles.OPERATOR);
        addStyleTest("abc", SpanStyles.HEADCELL, SpanStyles.TEXT);
        testStyles(test);
        Assertions.assertEquals(1, test.getColsSpan(), "ColsSpan");
        Assertions.assertEquals(1, test.getRowsSpan(), "RowSpan");
        Assertions.assertEquals(true, test.isHead(), "isHead");
    }

    @Override
    protected TableCell initSpan(SpanBranch parent, String input) {
        switch (subTest) {
            case TEXT:
                return TableCell.newTextCell(parent, input);
            case HEADING:
                return TableCell.newHeadingCell(parent, input);
        }
        fail("Unimplemented test for:" + subTest.toString());
        return null;
    }
}
