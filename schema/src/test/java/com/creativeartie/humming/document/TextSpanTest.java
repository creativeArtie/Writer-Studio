package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class TextSpanTest extends SpanBranchTestBase<TextSpan> {
    private enum SubTests {
        ID;
    }

    private SubTests subTest;

    @Test
    void testTextEsape() {
        subTest = SubTests.ID;
        TextSpan text = newSpan("abc\\:");
        addStyleTest("abc", SpanStyles.TEXT);
        addStyleTest("\\:", SpanStyles.ESCAPE);
        testStyles(text);
        assertEquals("abc:", text.getText());
    }

    @Test
    void testEsapeText() {
        subTest = SubTests.ID;
        TextSpan text = newSpan("\\:abc");
        addStyleTest("\\:", SpanStyles.ESCAPE);
        addStyleTest("abc", SpanStyles.TEXT);
        testStyles(text);
        assertEquals(":abc", text.getText());
    }

    @Test
    void testLong() {
        subTest = SubTests.ID;
        TextSpan text = newSpan("abc\\:efg\\topq");
        addStyleTest("abc", SpanStyles.TEXT);
        addStyleTest("\\:", SpanStyles.ESCAPE);
        addStyleTest("efg", SpanStyles.TEXT);
        addStyleTest("\\t", SpanStyles.ESCAPE);
        addStyleTest("opq", SpanStyles.TEXT);
        testStyles(text);
        assertEquals("abc:efgtopq", text.getText());
    }

    @Override
    protected TextSpan initSpan(SpanBranch parent, String input) {
        if (subTest == SubTests.ID) return TextSpan.newId(parent, input);
        fail("Unimplemented test for:" + subTest.toString());
        return null;
    }
}
