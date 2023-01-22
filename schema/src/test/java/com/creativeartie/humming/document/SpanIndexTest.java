package com.creativeartie.humming.document;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.google.common.collect.*;

class SpanIndexTest {
    private static String useText = "Hello World!{^note}\n!^note:*test* only\\!";
    private static Document doc;
    private static final String displayNamePost = " {4} indexes: {2}";
    private static int testIndex;

    private static Arguments getArguments(int start, int end, Class<?> spanClass, int... indexes) {
        StringBuilder stringBuilder = new StringBuilder(String.format(" %02d => ", testIndex));
        for (int i = 0; i < useText.length(); i++) {
            if (i < start || i >= end) {
                stringBuilder.append('_');
            } else {
                if (useText.charAt(i) == '\n') stringBuilder.append('␤');
                else stringBuilder.append(useText.charAt(i));
            }
        }
        String extractText = stringBuilder.toString();
        ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        for (int index : indexes) {
            builder.add(index);
        }
        testIndex++;
        return Arguments.of(start, end, builder.build(), spanClass, extractText);
    }

    @BeforeAll
    public static void setup() {
        doc = new Document();
        doc.updateText(useText);
        String display = useText.replace('\n', '␤');
        for (SpanBranch child : doc) {
            displaySpanBranch(child, display, 0);
        }
    }

    private static void printClass(SpanBranch branch, String text, int depth) {
        String spacing = "";
        for (int i = 0; i < depth; i++) {
            spacing += "+";
        }
        int start = branch.getStartIndex();
        int end = branch.getEndIndex();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i < start || i >= end) {
                out.append('_');
            } else {
                out.append(text.charAt(i));
            }
        }
        System.out.printf("%7s%15s:%s\n", spacing, branch.getClass().getSimpleName(), out);
    }

    private static void displaySpanBranch(SpanBranch branch, String text, int depth) {
        if (branch instanceof SectionDivision) System.out.println("Hello");
        for (Span child : branch) {
            printClass(branch, text, depth);
            if (child instanceof SpanBranch) {
                displaySpanBranch((SpanBranch) child, text, depth + 1);
            }
        }
    }

    private static Stream<Arguments> provideParameters() {
        testIndex = 1;
        Stream.Builder<Arguments> builder = Stream.builder();

        int divIdx = 0;
        // Hello World!{^note}␤!^note:*test* only\!
        // +++++++++++++++++++++++++++++++++++++++++
        // 01234567890123456789012345678901234567890
        // 00000000001111111111222222222233333333334
        builder.accept(getArguments(0, 40, Division.class, divIdx)); // 1

        int lineIdx = 0;
        // Hello World!{^note}␤!^note:*test* only\!
        // ++++++++++++++++++++
        // 01234567890123456789012345678901234567890
        // 00000000001111111111222222222233333333334
        builder.accept(getArguments(0, 20, LineSpan.class, divIdx, lineIdx)); // 2

        int ltxtIdx = 0;
        // Hello World!{^note}␤!^note:*test* only\!
        // +++++++++++++++++++
        // 01234567890123456789012345678901234567890
        // 00000000001111111111222222222233333333334
        builder.accept(getArguments(0, 19, LineText.class, divIdx, lineIdx, ltxtIdx)); // 3

        int textIdx = 0;
        // Hello World!{^note}␤!^note:*test* only\!
        // ++++++++++++
        // 01234567890123456789012345678901234567890
        // 00000000001111111111222222222233333333344
        builder.accept(getArguments(0, 12, TextSpan.class, divIdx, lineIdx, ltxtIdx, textIdx)); // 4
        textIdx = -1;

        return builder.build();
    }

    private Span getChild(List<Integer> indexes) {
        Span test = null;
        for (int idx : indexes) {
            if (test == null) {
                test = doc.get(idx);
            } else {
                Assertions.assertInstanceOf(SpanBranch.class, test);
                test = ((SpanBranch) test).get(idx);
            }
        }
        return test;
    }

    @ParameterizedTest(name = "Start" + displayNamePost)
    @MethodSource("provideParameters")
    void testStartIndex(int start, int end, List<Integer> indexes, Class<?> spanClass, String displayName) {
        Span test = getChild(indexes);
        Span testing = test;
        Assertions.assertEquals(start, testing.getStartIndex(), "Start index");
    }

    @ParameterizedTest(name = "End" + displayNamePost)
    @MethodSource("provideParameters")
    void testEndIndex(int start, int end, List<Integer> indexes, Class<?> spanClass, String displayName) {
        Span test = getChild(indexes);
        Span testing = test;
        Assertions.assertEquals(end, testing.getEndIndex(), "End index");
    }

    @ParameterizedTest(name = "Find" + displayNamePost)
    @MethodSource("provideParameters")
    void testfindChild(int start, int end, List<Integer> indexes, Class<?> spanClass, String displayName) {
        Span child = getChild(indexes);
        Assertions.assertArrayEquals(indexes.toArray(), doc.findChild(child).toArray());
    }

    @ParameterizedTest(name = "Length" + displayNamePost)
    @MethodSource("provideParameters")
    void testLength(int start, int end, List<Integer> indexes, Class<?> spanClass, String displayName) {
        int length = end - start;
        Span child = getChild(indexes);
        Assertions.assertEquals(length, child.getLength());
    }

    @ParameterizedTest(name = "Class" + displayNamePost)
    @MethodSource("provideParameters")
    void testClass(int start, int end, List<Integer> indexes, Class<?> spanClass, String displayName) {
        Span child = getChild(indexes);
        Assertions.assertInstanceOf(spanClass, child);
    }

    @Test
    void testDocIndexes() {
        Assertions.assertAll(
                () -> Assertions.assertEquals(0, doc.getStartIndex()),
                () -> Assertions.assertEquals(40, doc.getEndIndex()), () -> Assertions.assertEquals(40, doc.getLength())
        );
    }

    @Test
    void testDocSize() {
        Assertions.assertEquals(1, doc.size());
    }

    @AfterAll
    public static void printStats() {
        doc.printCacheStats();
    }
}
