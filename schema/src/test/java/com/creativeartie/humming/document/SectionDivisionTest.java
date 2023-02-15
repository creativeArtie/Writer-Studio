package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class SectionDivisionTest extends DivisionTestBase<SectionDivision> {
    public SectionDivisionTest() {
        super(true, SectionDivision.class);
    }

    @Test
    void testNoHeading() {
        newDoc("No Heading {!help} #STUB\n>Some content: *\\*Hello World*\\*.");

        addStyleTest("No Heading ", LineStyles.NORMAL, SpanStyles.TEXT);
        addStyleTest("{!", LineStyles.NORMAL, SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("help", LineStyles.NORMAL, SpanStyles.TODO, SpanStyles.TEXT);
        addStyleTest("}", LineStyles.NORMAL, SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest(" #STUB", LineStyles.NORMAL, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NORMAL, SpanStyles.OPERATOR);

        addStyleTest(">", LineStyles.QUOTE, SpanStyles.OPERATOR);
        addStyleTest("Some content: ", LineStyles.QUOTE, SpanStyles.TEXT);
        addStyleTest("*", LineStyles.QUOTE, SpanStyles.OPERATOR);
        addStyleTest("\\*", LineStyles.QUOTE, SpanStyles.BOLD, SpanStyles.ESCAPE);
        addStyleTest("Hello World", LineStyles.QUOTE, SpanStyles.BOLD, SpanStyles.TEXT);
        addStyleTest("*", LineStyles.QUOTE, SpanStyles.OPERATOR);
        addStyleTest("\\*", LineStyles.QUOTE, SpanStyles.ESCAPE);
        addStyleTest(".", LineStyles.QUOTE, SpanStyles.TEXT);
        testStyles();

        addChild(0, SectionDivision.class, 2, "No heading", 1);
        testChildren();
    }

    @Test
    void testWithHeading() {
        newDoc("=heading\nSome content.");

        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("Some content.", LineStyles.NORMAL, SpanStyles.TEXT);
        testStyles();

        addChild(0, SectionDivision.class, 2, "WithHeading", 1);
        testChildren();
    }

    @Test
    void testStraightToSub() {
        newDoc("==heading\nSome content.");
        Assertions.assertEquals(1, getDocument().size(), "Doc size");
        addStyleTest("==", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("Some content.", LineStyles.NORMAL, SpanStyles.TEXT);
        testStyles();

        TestChild child = addChild(0, SectionDivision.class, 1, "No heading", 1);
        child.newChild(0, SectionDivision.class, 2, "Child", 2);
        testChildren();
    }

    @Test
    void testWithSub() {
        newDoc("=heading\n===heading 3\n==heading");
        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR); // 1
        addStyleTest("heading", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("===", LineStyles.HEADING, SpanStyles.OPERATOR); // 1.1.1
        addStyleTest("heading 3", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("==", LineStyles.HEADING, SpanStyles.OPERATOR); // 1.2
        addStyleTest("heading", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles();

        TestChild h1 = addChild(0, SectionDivision.class, 3, "Parent", 1);
        TestChild h1_1 = h1.newChild(1, SectionDivision.class, 1, "First child", 2);
        h1_1.newChild(0, SectionDivision.class, 1, "grandchild", 3);
        h1.newChild(2, SectionDivision.class, 1, "Second child", 2);
        testChildren();
    }

    @Test
    void testDouble() {
        newDoc("=heading\n=heading");
        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR); // 1
        addStyleTest("heading", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR); // 2
        addStyleTest("heading", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles();

        addChild(0, SectionDivision.class, 1, "First child", 1);
        addChild(1, SectionDivision.class, 1, "Second child", 1);
        testChildren();
    }

    @Override
    protected Executable testChild(int index, Object expect, SectionDivision child) {
        switch (index) {
            case 0:
                return () -> Assertions.assertEquals(expect, child.getLevel());
        }
        return null;
    }
}
