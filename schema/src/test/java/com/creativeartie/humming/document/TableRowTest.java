package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

public class TableRowTest extends SpanBranchTestBase<TableRow> {
    @Test
    public void testFullRow() {
        TableRow test = newSpan("|Col 1|Col2|\n");
        addStyleTest("|", LineStyles.ROW, SpanStyles.OPERATOR);
        addStyleTest("Col 1", LineStyles.ROW, SpanStyles.TEXT);
        addStyleTest("|", LineStyles.ROW, SpanStyles.OPERATOR);
        addStyleTest("Col2", LineStyles.ROW, SpanStyles.TEXT);
        addStyleTest("|\n", LineStyles.ROW, SpanStyles.OPERATOR);
        testStyles(test);
    }

    @Test
    public void testEndRow() {
        TableRow test = newSpan("|Col 1|Col2|");
        addStyleTest("|", LineStyles.ROW, SpanStyles.OPERATOR);
        addStyleTest("Col 1", LineStyles.ROW, SpanStyles.TEXT);
        addStyleTest("|", LineStyles.ROW, SpanStyles.OPERATOR);
        addStyleTest("Col2", LineStyles.ROW, SpanStyles.TEXT);
        addStyleTest("|", LineStyles.ROW, SpanStyles.OPERATOR);
        testStyles(test);
    }

    @Test
    public void testStartOnly() {
        TableRow test = newSpan("|");
        addStyleTest("|", LineStyles.ROW, SpanStyles.OPERATOR);
        testStyles(test);
    }

    @Override
    protected TableRow initSpan(SpanBranch parent, String input) {
        LineSpan span = LineSpan.newLine(parent, input);
        Assertions.assertInstanceOf(TableRow.class, span);
        return (TableRow) span;
    }
}
