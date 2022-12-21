package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.*;

class SpanTextTest extends SpanBranchTestBase {
    @Test
    void testTextEsape() {
        SpanText text = new SpanText(newParent(), BasicTextPatterns.ID, "abc\\:");
        addStyleTest("abc", StyleClasses.TEXT);
        addStyleTest("\\:", StyleClasses.ESCAPE);
        testStyles(text);
        assertEquals("abc:", text.getText());
    }

    @Test
    void testEsapeText() {
        SpanText text = new SpanText(newParent(), BasicTextPatterns.ID, "\\:abc");
        addStyleTest("\\:", StyleClasses.ESCAPE);
        addStyleTest("abc", StyleClasses.TEXT);
        testStyles(text);
        assertEquals(":abc", text.getText());
    }

    @Test
    void testLong() {
        SpanText text = new SpanText(newParent(), BasicTextPatterns.ID, "abc\\:efg\\topq");
        addStyleTest("abc", StyleClasses.TEXT);
        addStyleTest("\\:", StyleClasses.ESCAPE);
        addStyleTest("efg", StyleClasses.TEXT);
        addStyleTest("\\t", StyleClasses.ESCAPE);
        addStyleTest("opq", StyleClasses.TEXT);
        testStyles(text);
        assertEquals("abc:efgtopq", text.getText());
    }
}
