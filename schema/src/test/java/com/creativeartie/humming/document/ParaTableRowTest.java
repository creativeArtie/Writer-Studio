package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

final class ParaTableRowTest extends SpanBranchTestBase<ParaTableRow> {
    @Test
    public void testFullRow() {
        ParaTableRow test = newSpan("|Col 1|Col2|\n");
        addStyleTest("|", StyleLines.ROW, StylesSpans.OPERATOR);
        addStyleTest("Col 1", StyleLines.ROW, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.OPERATOR);
        addStyleTest("Col2", StyleLines.ROW, StylesSpans.TEXT);
        addStyleTest("|\n", StyleLines.ROW, StylesSpans.OPERATOR);
        testStyles(test);
    }

    @Test
    public void testEndRow() {
        ParaTableRow test = newSpan("|Col 1|Col2|");
        addStyleTest("|", StyleLines.ROW, StylesSpans.OPERATOR);
        addStyleTest("Col 1", StyleLines.ROW, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.OPERATOR);
        addStyleTest("Col2", StyleLines.ROW, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.OPERATOR);
        testStyles(test);
    }

    @Test
    public void testStartOnly() {
        ParaTableRow test = newSpan("|");
        addStyleTest("|", StyleLines.ROW, StylesSpans.OPERATOR);
        testStyles(test);
    }

    @Override
    protected ParaTableRow initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaTableRow.class, span);
        return (ParaTableRow) span;
    }
}
