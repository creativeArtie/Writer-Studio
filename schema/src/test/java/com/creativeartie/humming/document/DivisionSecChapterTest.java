package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

final class DivisionSecChapterTest extends DivisionTestBase<DivisionSecChapter> {
    public DivisionSecChapterTest() {
        super(true, DivisionSecChapter.class);
    }

    @Test
    void testNoHeading() {
        newDoc("No Heading {!help} #STUB\n>Some content: *\\*Hello World*\\*.");

        addStyleTest("No Heading ", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        addStyleTest("{!", CssLineStyles.NORMAL, CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("help", CssLineStyles.NORMAL, CssSpanStyles.AGENDA, CssSpanStyles.TEXT);
        addStyleTest("}", CssLineStyles.NORMAL, CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest(" #STUB", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NORMAL, CssSpanStyles.OPERATOR);

        addStyleTest(">", CssLineStyles.QUOTE, CssSpanStyles.OPERATOR);
        addStyleTest("Some content: ", CssLineStyles.QUOTE, CssSpanStyles.TEXT);
        addStyleTest("*", CssLineStyles.QUOTE, CssSpanStyles.OPERATOR);
        addStyleTest("\\*", CssLineStyles.QUOTE, CssSpanStyles.BOLD, CssSpanStyles.ESCAPE);
        addStyleTest("Hello World", CssLineStyles.QUOTE, CssSpanStyles.BOLD, CssSpanStyles.TEXT);
        addStyleTest("*", CssLineStyles.QUOTE, CssSpanStyles.OPERATOR);
        addStyleTest("\\*", CssLineStyles.QUOTE, CssSpanStyles.ESCAPE);
        addStyleTest(".", CssLineStyles.QUOTE, CssSpanStyles.TEXT);
        testStyles();

        newChildAtIndex("No heading", 0).setSize(2).setClass(DivisionSecChapter.class).setData(1, "1");
        testChildren();
    }

    @Test
    void testWithHeading() {
        newDoc("=heading\nSome content.");

        addStyleTest("=", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);
        addStyleTest("heading", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);

        addStyleTest("Some content.", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        testStyles();

        newChildAtIndex("With Heading", 0).setSize(2).setClass(DivisionSecChapter.class).setData(1, "1");
        testChildren();
    }

    @Test
    void testStraightToSub() {
        newDoc("==heading\nSome content.");
        Assertions.assertEquals(1, getDocument().size(), "Doc size");
        addStyleTest("==", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);
        addStyleTest("heading", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);

        addStyleTest("Some content.", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        testStyles();

        newChildAtIndex("No Heading", 0).setSize(1).setClass(DivisionSecChapter.class).setData(1, "1")
                .newChildAtIndex("child", 0).setSize(2).setClass(DivisionSecChapter.class).setData(2, "1:1");
        testChildren();
    }

    @Test
    void testWithSub() {
        newDoc("=heading\n===heading 3\n==heading");
        addStyleTest("=", CssLineStyles.HEADING, CssSpanStyles.OPERATOR); // 1
        addStyleTest("heading", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);

        addStyleTest("===", CssLineStyles.HEADING, CssSpanStyles.OPERATOR); // 1.1.1
        addStyleTest("heading 3", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);

        addStyleTest("==", CssLineStyles.HEADING, CssSpanStyles.OPERATOR); // 1.2
        addStyleTest("heading", CssLineStyles.HEADING, CssSpanStyles.TEXT);
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
        addStyleTest("=", CssLineStyles.HEADING, CssSpanStyles.OPERATOR); // 1
        addStyleTest("heading", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);

        addStyleTest("=", CssLineStyles.HEADING, CssSpanStyles.OPERATOR); // 2
        addStyleTest("heading", CssLineStyles.HEADING, CssSpanStyles.TEXT);
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
