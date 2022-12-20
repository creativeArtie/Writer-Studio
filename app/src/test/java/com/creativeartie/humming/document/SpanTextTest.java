package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.schema.*;

class SpanTextTest {
    private static ArrayList<StyleClasses[]> expectedStyles;
    private static ArrayList<Integer> expectedLengths;

    @BeforeAll
    private static void setup() {
        expectedStyles = new ArrayList<>();
        expectedLengths = new ArrayList<>();
    }

    @BeforeEach
    private void refreshList() {
        expectedStyles.clear();
        expectedLengths.clear();
    }

    protected SpanBranch newParent() {
        return new SpanBranch(new Document(true));
    }

    protected void addStyleTest(int length, StyleClasses... styles) {
        expectedLengths.add(length);
        expectedStyles.add(styles);
    }

    protected void testStyles(SpanBranch parent) {
        List<SpanLeaf> testLeaves = parent.getLeafs();
        int i = 0;
        Assertions.assertEquals(expectedStyles.size(), testLeaves.size(), "Leaves sizes");
        for (SpanLeaf leaf : testLeaves) {
            int idx = i;
            Assertions.assertAll(
                    () -> Assertions.assertArrayEquals(
                            expectedStyles.get(idx), leaf.getClassStyles().toArray(),
                            "For style at leaf index: " + Integer.toString(idx)
                    ),
                    () -> Assertions.assertEquals(
                            expectedLengths.get(idx), leaf.getLength(),
                            "For length at leaf index: " + Integer.toString(idx)
                    )
            );
            i++;
        }
    }

    @Test
    void testTextEsape() {
        SpanText text = new SpanText(newParent(), BasicTextPatterns.ID, "abc\\:");
        addStyleTest(3, StyleClasses.TEXT);
        addStyleTest(2, StyleClasses.ESCAPE);
        testStyles(text);
        assertEquals("abc:", text.getText());
    }

    @Test
    void testEsapeText() {
        SpanText text = new SpanText(newParent(), BasicTextPatterns.ID, "\\:abc");
        addStyleTest(2, StyleClasses.ESCAPE);
        addStyleTest(3, StyleClasses.TEXT);
        testStyles(text);
        assertEquals(":abc", text.getText());
    }

    @Test
    void testLong() {
        SpanText text = new SpanText(newParent(), BasicTextPatterns.ID, "abc\\:efg\\topq");
        addStyleTest(3, StyleClasses.TEXT); // abc
        addStyleTest(2, StyleClasses.ESCAPE); // \\:
        addStyleTest(3, StyleClasses.TEXT); // ef
        addStyleTest(2, StyleClasses.ESCAPE); // \\t
        addStyleTest(3, StyleClasses.TEXT); // opq
        testStyles(text);
        assertEquals("abc:efgtopq", text.getText());
    }
}
