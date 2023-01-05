package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class TextSpanTest extends SpanBranchTestBase {
    @Test
    void testTextEsape() {
        TextSpan text = TextSpan.newId(newParent(), "abc\\:");
        addStyleTest("abc", SpanStyles.TEXT);
        addStyleTest("\\:", SpanStyles.ESCAPE);
        testStyles(text);
        assertEquals("abc:", text.getText());
    }

    @Test
    void testEsapeText() {
        TextSpan text = TextSpan.newId(newParent(), "\\:abc");
        addStyleTest("\\:", SpanStyles.ESCAPE);
        addStyleTest("abc", SpanStyles.TEXT);
        testStyles(text);
        assertEquals(":abc", text.getText());
    }

    @Test
    void testLong() {
        TextSpan text = TextSpan.newId(newParent(), "abc\\:efg\\topq");
        addStyleTest("abc", SpanStyles.TEXT);
        addStyleTest("\\:", SpanStyles.ESCAPE);
        addStyleTest("efg", SpanStyles.TEXT);
        addStyleTest("\\t", SpanStyles.ESCAPE);
        addStyleTest("opq", SpanStyles.TEXT);
        testStyles(text);
        assertEquals("abc:efgtopq", text.getText());
    }
}
