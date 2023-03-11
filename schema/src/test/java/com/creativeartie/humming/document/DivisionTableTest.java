package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class DivisionTableTest extends DivisionTestBase<DivisionTable> {
    public DivisionTableTest() {
        super(true, DivisionTable.class);
    }

    @Test
    void testBasic() {
        newDoc("|Heading 1|Heading 2|\n|cell 1|cell 2|\n|cell 3|cell 4|");
        addStyleTest("|", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.OPERATOR);
        addStyleTest("Heading 1", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.OPERATOR);
        addStyleTest("Heading 2", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.TEXT);
        addStyleTest("|\n", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.OPERATOR);

        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 1", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 2", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|\n", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);

        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 3", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 4", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        testStyles();

        newChildAtIndex("head", 0).setSize(1).setClass(DivisionSecChapter.class).newChildAtIndex("table", 0).setSize(3)
                .setClass(DivisionTable.class).setData(2);
    }

    @Test
    void testExtraColumn() {
        newDoc("|Heading 1|Heading 2|\n|cell 1|cell 2|cell 3|\n|cell 3|cell 4|");
        addStyleTest("|", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.OPERATOR);
        addStyleTest("Heading 1", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.OPERATOR);
        addStyleTest("Heading 2", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.TEXT);
        addStyleTest("|\n", StyleLines.ROW, StylesSpans.HEADCELL, StylesSpans.OPERATOR);

        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 1", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 2", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 3", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|\n", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);

        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 3", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        addStyleTest("cell 4", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.TEXT);
        addStyleTest("|", StyleLines.ROW, StylesSpans.TEXTCELL, StylesSpans.OPERATOR);
        testStyles();

        newChildAtIndex("head", 0).setSize(1).setClass(DivisionSecChapter.class).newChildAtIndex("table", 0).setSize(3)
                .setClass(DivisionTable.class).setData(3);
    }

    @Override
    protected Executable testChild(int index, Object expect, DivisionTable child) {
        return () -> Assertions.assertEquals(expect, child.getColumnSize());
    }
}
