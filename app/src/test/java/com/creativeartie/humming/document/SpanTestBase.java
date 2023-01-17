package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;
import org.opentest4j.*;

public abstract class SpanTestBase<T extends SpanBranch> {
    private static ArrayList<String> expectedText;
    private static ArrayList<StyleClass[]> expectedStyles;
    private static Document rootDoc;
    private static boolean testIndex;

    public SpanTestBase() {
        this(false);
    }

    public SpanTestBase(boolean testIdx) {
        testIndex = testIdx;
    }

    @BeforeAll
    private static void setup() {
        expectedStyles = new ArrayList<>();
        expectedText = new ArrayList<>();
        testIndex = false;
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
        testStyles(parent.getLeafs());
    }

    protected void testStyles() {
        testStyles(rootDoc.convertLeaves((leaf) -> leaf));
    }

    private void testStyles(List<SpanLeaf> testLeaves) throws MultipleFailuresError {
        int i = 0;
        int startIdx = 0;
        Assertions.assertEquals(expectedStyles.size(), testLeaves.size(), "Leaves sizes");
        for (SpanLeaf leaf : testLeaves) {
            String format = "For %s at leaf index %d, text \"%s\"";

            StyleClass[] expectedStyle = expectedStyles.get(i);
            String styleText = String.format(format, "styles", i, expectedText.get(i));

            int expectedLength = expectedText.get(i).length();
            String styleLength = String.format(format, "styles", i, expectedText.get(i));

            int expectedStart = startIdx;
            String startIndex = String.format(format, "startIdx", i, expectedStart);

            int expectedEnd = startIdx + expectedLength;
            String endIndex = String.format(format, "endIdx", i, expectedEnd);

            Assertions.assertAll(
                    () -> Assertions.assertArrayEquals(expectedStyle, leaf.getClassStyles().toArray(), styleText),
                    () -> Assertions.assertEquals(expectedLength, leaf.getLength(), styleLength), () -> {
                        if (testIndex) Assertions.assertEquals(expectedStart, leaf.getStartIndex(), startIndex);
                    }, () -> {
                        if (testIndex) Assertions.assertEquals(expectedEnd, leaf.getEndIndex(), endIndex);
                    }
            );
            i++;
            startIdx = expectedEnd;
        }
    }

    protected void printSpan(T span, String input) {
        input = input.replaceAll("\n", "␤");

        int start = 0;
        System.out.println(input);
        for (SpanLeaf leaf : span.getLeafs()) {
            int end = start + leaf.getLength();
            System.out.println(input.substring(start, end) + leaf.getClassStyles().toString());

            start = end;
        }
        System.out.println();
    }
}
