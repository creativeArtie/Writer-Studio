package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.document.IdentitySpan.*;

public class SpanBranchTestBase {
    private static ArrayList<String> expectedText;
    private static ArrayList<StyleClass[]> expectedStyles;
    private static Document rootDoc;

    private static class HolderSpan extends SpanBranch implements IdentityParent {
        protected HolderSpan(Document root, StyleClass... classes) {
            super(root, classes);
        }

        @Override
        public int getIdPosition() {
            return 0;
        }
    }

    @BeforeAll
    private static void setup() {
        expectedStyles = new ArrayList<>();
        expectedText = new ArrayList<>();
    }

    @BeforeEach
    protected void beforeEach() {
        refreshLists();
        rootDoc = new Document();
    }

    protected void refreshLists() {
        expectedStyles.clear();
        expectedText.clear();
    }

    protected static SpanBranch newParent() {
        return new HolderSpan(rootDoc);
    }

    protected void addStyleTest(String text, StyleClass... styles) {
        expectedText.add(text);
        expectedStyles.add(styles);
    }

    protected Document getDocument() {
        return rootDoc;
    }

    protected void testStyles(SpanBranch parent) {
        List<SpanLeaf> testLeaves = parent.getLeafs();
        int i = 0;
        Assertions.assertEquals(expectedStyles.size(), testLeaves.size(), "Leaves sizes");
        for (SpanLeaf leaf : testLeaves) {
            StyleClass[] expectedStyle = expectedStyles.get(i);
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
