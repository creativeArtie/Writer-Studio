package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

final class TextSpanTest extends SpanBranchTestBase<TextSpan> {
    private enum SubTests {
        ID, SIMPLE;
    }

    private SubTests subTest;

    @Test
    void testTextEsape() {
        subTest = SubTests.ID;
        TextSpan text = newSpan("abc\\:");
        addStyleTest("abc", CssSpanStyles.TEXT);
        addStyleTest("\\:", CssSpanStyles.ESCAPE);
        testStyles(text);
        assertEquals("abc:", text.getText());
    }

    @Test
    void testEsapeText() {
        subTest = SubTests.ID;
        TextSpan text = newSpan("\\:abc");
        addStyleTest("\\:", CssSpanStyles.ESCAPE);
        addStyleTest("abc", CssSpanStyles.TEXT);
        testStyles(text);
        assertEquals(":abc", text.getText());
    }

    @Test
    void testLong() {
        subTest = SubTests.ID;
        TextSpan text = newSpan("abc\\:efg\\topq");
        addStyleTest("abc", CssSpanStyles.TEXT);
        addStyleTest("\\:", CssSpanStyles.ESCAPE);
        addStyleTest("efg", CssSpanStyles.TEXT);
        addStyleTest("\\t", CssSpanStyles.ESCAPE);
        addStyleTest("opq", CssSpanStyles.TEXT);
        testStyles(text);
        assertEquals("abc:efgtopq", text.getText());
    }

    @Test
    void testEscapeEnd() {
        subTest = SubTests.SIMPLE;
        TextSpan text = newSpan("\\");
        addStyleTest("\\", CssSpanStyles.ESCAPE);
        testStyles(text);
        assertEquals("", text.getText());
    }

    @Override
    protected TextSpan initSpan(SpanBranch parent, String input) {
        if (subTest == SubTests.ID) return TextSpan.newId(parent, input);
        // else if (subTest == SubTests.SIMPLE)
        return TextSpan.newSimple(parent, input);
    }
}
