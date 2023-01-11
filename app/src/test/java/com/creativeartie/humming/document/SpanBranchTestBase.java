package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;

import com.creativeartie.humming.document.IdentitySpan.*;

public abstract class SpanBranchTestBase<T extends SpanBranch> {
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

        @Override
        public Optional<IdentitySpan> getPointer() {
            // TODO Auto-generated method stub
            return null;
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
            String format = "For %s at leaf index %d, text \"%s\"";
            String styleText = String.format(format, "styles", i, expectedText.get(i));
            String styleLength = String.format(format, "styles", i, expectedText.get(i));
            Assertions.assertAll(
                    () -> Assertions.assertArrayEquals(expectedStyle, leaf.getClassStyles().toArray(), styleText),
                    () -> Assertions.assertEquals(expectedLength, leaf.getLength(), styleLength)
            );
            i++;
        }
    }

    protected T newSpan(String input) {
        T span = initSpan(new HolderSpan(rootDoc), input);
        input = input.replaceAll("\n", "␤");

        int start = 0;
        System.out.println(input);
        for (SpanLeaf leaf : span.getLeafs()) {
            int end = start + leaf.getLength();
            System.out.println(input.substring(start, end) + leaf.getClassStyles().toString());

            start = end;
        }
        System.out.println();
        return span;
    }

    protected abstract T initSpan(SpanBranch parent, String input);
}
