package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class DivisionSecChapterTest extends DivisionTestBase<DivisionSecChapter> {
    public DivisionSecChapterTest() {
        super(true, DivisionSecChapter.class);
    }

    @Test
    void testNoHeading() {
        newDoc("No Heading {!help} #STUB\n>Some content: *\\*Hello World*\\*.");

        addStyleTest("No Heading ", StyleLines.NORMAL, StylesSpans.TEXT);
        addStyleTest("{!", StyleLines.NORMAL, StylesSpans.AGENDA, StylesSpans.OPERATOR);
        addStyleTest("help", StyleLines.NORMAL, StylesSpans.AGENDA, StylesSpans.TEXT);
        addStyleTest("}", StyleLines.NORMAL, StylesSpans.AGENDA, StylesSpans.OPERATOR);
        addStyleTest(" #STUB", StyleLines.NORMAL, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NORMAL, StylesSpans.OPERATOR);

        addStyleTest(">", StyleLines.QUOTE, StylesSpans.OPERATOR);
        addStyleTest("Some content: ", StyleLines.QUOTE, StylesSpans.TEXT);
        addStyleTest("*", StyleLines.QUOTE, StylesSpans.OPERATOR);
        addStyleTest("\\*", StyleLines.QUOTE, StylesSpans.BOLD, StylesSpans.ESCAPE);
        addStyleTest("Hello World", StyleLines.QUOTE, StylesSpans.BOLD, StylesSpans.TEXT);
        addStyleTest("*", StyleLines.QUOTE, StylesSpans.OPERATOR);
        addStyleTest("\\*", StyleLines.QUOTE, StylesSpans.ESCAPE);
        addStyleTest(".", StyleLines.QUOTE, StylesSpans.TEXT);
        testStyles();

        newChildAtIndex("No heading", 0).setSize(2).setClass(DivisionSecChapter.class).setData(1, "1");
        testChildren();
    }

    @Test
    void testWithHeading() {
        newDoc("=heading\nSome content.");

        addStyleTest("=", StyleLines.HEADING, StylesSpans.OPERATOR);
        addStyleTest("heading", StyleLines.HEADING, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADING, StylesSpans.OPERATOR);

        addStyleTest("Some content.", StyleLines.NORMAL, StylesSpans.TEXT);
        testStyles();

        newChildAtIndex("With Heading", 0).setSize(2).setClass(DivisionSecChapter.class).setData(1, "1");
        testChildren();
    }

    @Test
    void testStraightToSub() {
        newDoc("==heading\nSome content.");
        Assertions.assertEquals(1, getDocument().size(), "Doc size");
        addStyleTest("==", StyleLines.HEADING, StylesSpans.OPERATOR);
        addStyleTest("heading", StyleLines.HEADING, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADING, StylesSpans.OPERATOR);

        addStyleTest("Some content.", StyleLines.NORMAL, StylesSpans.TEXT);
        testStyles();

        newChildAtIndex("No Heading", 0).setSize(1).setClass(DivisionSecChapter.class).setData(1, "1")
                .newChildAtIndex("child", 0).setSize(2).setClass(DivisionSecChapter.class).setData(2, "1:1");
        testChildren();
    }

    @Test
    void testWithSub() {
        newDoc("=heading\n===heading 3\n==heading");
        addStyleTest("=", StyleLines.HEADING, StylesSpans.OPERATOR); // 1
        addStyleTest("heading", StyleLines.HEADING, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADING, StylesSpans.OPERATOR);

        addStyleTest("===", StyleLines.HEADING, StylesSpans.OPERATOR); // 1.1.1
        addStyleTest("heading 3", StyleLines.HEADING, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADING, StylesSpans.OPERATOR);

        addStyleTest("==", StyleLines.HEADING, StylesSpans.OPERATOR); // 1.2
        addStyleTest("heading", StyleLines.HEADING, StylesSpans.TEXT);
        testStyles();

        TestChild h1 = newChildAtIndex("Parent", 0).setClass(DivisionSecChapter.class).setSize(3).setData(1, "1");
        TestChild h1_1 =
                h1.newChildAtIndex("First child", 1).setClass(DivisionSecChapter.class).setSize(1).setData(2, "1:1");
        h1_1.newChildAtIndex("grandchild", 0).setClass(DivisionSecChapter.class).setSize(1).setData(3, "1:1:1");
        h1.newChildAtIndex("second child", 2).setClass(DivisionSecChapter.class).setSize(1).setData(2, "1:2");
        testChildren();
    }

    @Test
    void testDouble() {
        newDoc("=heading\n=heading");
        addStyleTest("=", StyleLines.HEADING, StylesSpans.OPERATOR); // 1
        addStyleTest("heading", StyleLines.HEADING, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADING, StylesSpans.OPERATOR);

        addStyleTest("=", StyleLines.HEADING, StylesSpans.OPERATOR); // 2
        addStyleTest("heading", StyleLines.HEADING, StylesSpans.TEXT);
        testStyles();

        newChildAtIndex("First Child", 0).setClass(DivisionSecChapter.class).setSize(1).setData(1, "1");
        newChildAtIndex("Second Child", 1).setClass(DivisionSecChapter.class).setSize(1).setData(1, "2");
        testChildren();
    }

    @Override
    protected Executable testChild(int index, Object expect, DivisionSecChapter child) {
        if (index == 0) return () -> Assertions.assertEquals(expect, child.getLevel(), "getLevel");
        return () -> Assertions.assertEquals(expect, child.getLocation(), "getLocation");
    }
}
