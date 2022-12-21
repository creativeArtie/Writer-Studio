package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;

public class SpanBranchTestBase {
    private static ArrayList<String> expectedText;
    private static ArrayList<StyleClasses[]> expectedStyles;
    private static Document rootDoc;

    @BeforeAll
    private static void setup() {
        expectedStyles = new ArrayList<>();
        expectedText = new ArrayList<>();
    }

    @BeforeEach
    protected void refreshList() {
        expectedStyles.clear();
        expectedText.clear();
        rootDoc = new Document();
    }

    protected static SpanBranch newParent() {
        return new SpanBranch(rootDoc);
    }

    protected void addStyleTest(String text, StyleClasses... styles) {
        expectedText.add(text);
        expectedStyles.add(styles);
    }

    protected void runCleanUp() {
        rootDoc.runCleanup();
    }

    protected void testStyles(SpanBranch parent) {
        List<SpanLeaf> testLeaves = parent.getLeafs();
        int i = 0;
        Assertions.assertEquals(expectedStyles.size(), testLeaves.size(), "Leaves sizes");
        for (SpanLeaf leaf : testLeaves) {
            StyleClasses[] expectedStyle = expectedStyles.get(i);
            int expectedLength = expectedText.get(i).length();
            String messagePost = String.format("For %%s at leaf index %d, text \"%s\"", i, expectedText.get(i));
            Assertions.assertAll(
                    () -> Assertions.assertArrayEquals(
                            expectedStyle, leaf.getClassStyles().toArray(), String.format(messagePost, "styles")
                    ),
                    () -> Assertions
                            .assertEquals(expectedLength, leaf.getLength(), String.format(messagePost, "length"))
            );
            i++;
        }
    }
}
