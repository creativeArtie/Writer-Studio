package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

@SuppressWarnings("nls")
final class DivisionTableTest extends DivisionTestBase<DivisionTable> {
    public DivisionTableTest() {
        super(true, DivisionTable.class);
    }

    @Test
    void testBasic() {
        newDoc("|Heading 1|Heading 2|\n|cell 1|cell 2|\n|cell 3|cell 4|");
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);
        addStyleTest("Heading 1", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);
        addStyleTest("Heading 2", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.TEXT);
        addStyleTest("|\n", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);

        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 1", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 2", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|\n", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);

        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 3", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 4", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        testStyles();

        setCounter(12, 0);
        // @formatter:off
        newChildAtIndex("head", 0).setSize(1).setClass(DivisionSecChapter.class).setCounter(12, 0)
            .newChildAtIndex("table", 0).setSize(3).setClass(DivisionTable.class).setData(2).setCounter(12, 0);
        // @formatter:on
        testChildren();
    }

    @Test
    void testExtraColumn() {
        newDoc("|Heading 1|Heading 2|\n|cell 1|cell 2|cell 3|\n|cell 3|cell 4|");
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);
        addStyleTest("Heading 1", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);
        addStyleTest("Heading 2", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.TEXT);
        addStyleTest("|\n", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);

        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 1", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 2", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 3", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|\n", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);

        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 3", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 4", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.TEXTCELL, CssSpanStyles.OPERATOR);
        testStyles();

        setCounter(14, 0);
        // @formatter:off
        newChildAtIndex("head", 0).setSize(1).setClass(DivisionSecChapter.class).setCounter(14, 0)
            .newChildAtIndex("table", 0).setSize(3).setClass(DivisionTable.class).setData(3).setCounter(14, 0);
        // @formatter:on
        testChildren();
    }

    @Test
    void testMidTable() {
        newDoc("abc\n|cell 1|cell 2|cell 3|\nabc");

        addStyleTest("abc", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NORMAL, CssSpanStyles.OPERATOR);

        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 1", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 2", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.TEXT);
        addStyleTest("|", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);
        addStyleTest("cell 3", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.TEXT);
        addStyleTest("|\n", CssLineStyles.ROW, CssSpanStyles.HEADCELL, CssSpanStyles.OPERATOR);

        addStyleTest("abc", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        testStyles();

        setCounter(8, 0);
        // @formatter:off
        newChildAtIndex("head", 0).setSize(3).setClass(DivisionSecChapter.class).setCounter(8, 0)
            .newChildAtIndex("table", 1).setSize(1).setClass(DivisionTable.class).setData(3).setCounter(6, 0);
        // @formatter:on
        testChildren();
    }

    @Override
    protected Executable testChild(int index, Object expect, DivisionTable child) {
        return () -> Assertions.assertEquals(expect, child.getColumnSize());
    }
}
