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
    void testWithSubheadings() {
        newDoc("=heading 1.1\n==Heading 2.1\nContent\n==Heading 2.2\n===heading 2.2.1\n=heading 2\n=heading 3");
        Assertions.assertEquals(3, getDocument().size(), "Doc size");
        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading 1.1", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("==", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("Heading 2.1", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("Content", LineStyles.NORMAL, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NORMAL, SpanStyles.OPERATOR);

        addStyleTest("==", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("Heading 2.2", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("===", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading 2.2.1", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading 2", LineStyles.HEADING, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.HEADING, SpanStyles.OPERATOR);

        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading 3", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles();
    }

    protected void newDoc(String input) {
        getDocument().updateText(input);
        for (SpanBranch child : getDocument()) {
            printSpan(child, input);
        }
    }
}
