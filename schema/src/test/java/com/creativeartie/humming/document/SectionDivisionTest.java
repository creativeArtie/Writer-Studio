package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class SectionDivisionTest extends SpanTestBase<SpanBranch> {
    public SectionDivisionTest() {
        super(true);
    }

    @Test
    void testNoHeading() {
        newDoc("No Heading {!help} #STUB\n>Some content: *\\*Hello World*\\*.");
        Assertions.assertEquals(1, getDocument().size(), "Doc size");

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

        Assertions.assertEquals(1, getDocument().size(), "Doc size");

        SpanBranch branch = getDocument().get(0);
        Assertions.assertInstanceOf(SectionDivision.class, branch);
        SectionDivision test = (SectionDivision) branch;
        Assertions.assertEquals(1, test.getLevel(), "Level");
        Assertions.assertEquals(2, test.size(), "child size");
    }

    @Test
    void testWithHeading() {
        newDoc("=heading\nSome content.");
        Assertions.assertEquals(1, getDocument().size(), "Doc size");

        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("Some content.", LineStyles.NORMAL, SpanStyles.TEXT);
        testStyles();

        Assertions.assertEquals(1, getDocument().size(), "Doc size");

        SpanBranch branch = getDocument().get(0);
        Assertions.assertInstanceOf(SectionDivision.class, branch);
        SectionDivision test = (SectionDivision) branch;
        Assertions.assertEquals(1, test.getLevel(), "Level");
        Assertions.assertEquals(2, test.size(), "child size");
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

        Assertions.assertEquals(1, getDocument().size(), "Doc size");

        Span child = getDocument().get(0);
        Assertions.assertInstanceOf(SectionDivision.class, child);
        SectionDivision test = (SectionDivision) child;
        Assertions.assertEquals(1, test.getLevel(), "Parent level");
        Assertions.assertEquals(1, test.size(), "Parent size");

        child = test.get(0);
        Assertions.assertInstanceOf(SectionDivision.class, child);
        test = (SectionDivision) child;
        Assertions.assertEquals(2, test.getLevel(), "Child level");
        Assertions.assertEquals(2, test.size(), "Child size");
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

        Assertions.assertEquals(1, getDocument().size(), "Doc size");

        Span child = getDocument().get(0); // 1
        Assertions.assertInstanceOf(SectionDivision.class, child);
        SectionDivision section1 = (SectionDivision) child;
        Assertions.assertEquals(1, section1.getLevel(), "Child level");
        Assertions.assertEquals(3, section1.size(), "Child size");

        child = section1.get(1); // 1.1
        Assertions.assertInstanceOf(SectionDivision.class, child);
        SectionDivision test = (SectionDivision) child;
        Assertions.assertEquals(2, test.getLevel(), "Child level");
        Assertions.assertEquals(1, test.size(), "Child size");

        child = test.get(0); // 1.1.1
        Assertions.assertInstanceOf(SectionDivision.class, child);
        test = (SectionDivision) child;
        Assertions.assertEquals(3, test.getLevel(), "Child level");
        Assertions.assertEquals(1, test.size(), "Child size");

        child = section1.get(2); // 1.2
        Assertions.assertInstanceOf(SectionDivision.class, child);
        test = (SectionDivision) child;
        Assertions.assertEquals(2, test.getLevel(), "Child level");
        Assertions.assertEquals(1, test.size(), "Child size");
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

        Assertions.assertEquals(2, getDocument().size(), "Doc size");

        Span child = getDocument().get(0); // 2
        Assertions.assertInstanceOf(SectionDivision.class, child);
        SectionDivision test = (SectionDivision) child;
        Assertions.assertEquals(1, test.getLevel(), "Child level");
        Assertions.assertEquals(1, test.size(), "Child size");

        child = getDocument().get(1); // 3
        Assertions.assertInstanceOf(SectionDivision.class, child);
        test = (SectionDivision) child;
        Assertions.assertEquals(1, test.getLevel(), "Child level");
        Assertions.assertEquals(1, test.size(), "Child size");
    }

    protected void newDoc(String input) {
        getDocument().updateText(input);
        for (SpanBranch child : getDocument()) {
            printSpan(child, input);
        }
    }
}
