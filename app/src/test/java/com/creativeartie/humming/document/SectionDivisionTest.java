package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class SectionDivisionTest extends SpanTestBase<SpanBranch> {
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
        SpanBranch branch = getDocument().get(0);
        testStyles(branch);
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
        SpanBranch branch = getDocument().get(0);
        testStyles(branch);
        Assertions.assertInstanceOf(SectionDivision.class, branch);
        SectionDivision test = (SectionDivision) branch;
        Assertions.assertEquals(1, test.getLevel(), "Level");
        Assertions.assertEquals(2, test.size(), "child size");
    }

    @Test
    void testWithSubheading() {}

    protected void newDoc(String input) {
        getDocument().updateText(input);
        for (SpanBranch child : getDocument()) {
            printSpan(child, input);
        }
    }
}
