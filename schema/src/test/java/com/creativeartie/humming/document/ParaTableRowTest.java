package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

@SuppressWarnings("nls")
final class ParaTableRowTest extends SpanBranchTestBase<ParaTableRow> {
    @Test
    public void testFullRow() {
        ParaTableRow test = newSpan("|Col 1|Col2|\n");
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.OPERATOR);
        addStyleTest("Col 1", CssLineStyles.ROW, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.OPERATOR);
        addStyleTest("Col2", CssLineStyles.ROW, CssSpanStyles.TEXT);
        addStyleTest("|\n", CssLineStyles.ROW, CssSpanStyles.OPERATOR);
        testStyles(test);
    }

    @Test
    public void testEndRow() {
        ParaTableRow test = newSpan("|Col 1|Col2|");
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.OPERATOR);
        addStyleTest("Col 1", CssLineStyles.ROW, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.OPERATOR);
        addStyleTest("Col2", CssLineStyles.ROW, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.OPERATOR);
        testStyles(test);
    }

    @Test
    public void testStartOnly() {
        ParaTableRow test = newSpan("|");
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.OPERATOR);
        testStyles(test);
    }

    @Override
    protected ParaTableRow initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaTableRow.class, span);
        return (ParaTableRow) span;
    }
}
