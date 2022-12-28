package com.creativeartie.humming.document;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.google.common.collect.*;

class SpanIndexTest {
    private static String span1 = "{^cat\\:more:id}";
    private static String span2 = "{*id}";
    private static String spans = span1 + span2;
    private static Document doc;

    private static Arguments getArguments(int start, int end, int index1, int... indexes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < spans.length(); i++) {
            if (i < start || i >= end) {
                stringBuilder.append('_');
            } else {
                stringBuilder.append(spans.charAt(i));
            }
        }
        String extractText = stringBuilder.toString();
        ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        for (int index : indexes) {
            builder.add(index);
        }
        return Arguments.of(start, end, index1, builder.build(), extractText);
    }

    @BeforeAll
    public static void setup() {
        doc = new Document();
        doc.add(ReferencePointerSpan.createSpan(new SpanBranch(doc), span1));
        doc.add(ReferencePointerSpan.createSpan(new SpanBranch(doc), span2));
    }

    private static Stream<Arguments> provideParameters() {
        Stream.Builder<Arguments> builder = Stream.builder();
        int mainIndex = -1, spanIndex = -1, idIndex = -1, textIndex = -1;

        // ++++++++++++++++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        mainIndex++;
        builder.accept(getArguments(0, 15, mainIndex)); // 0

        // +
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        builder.accept(getArguments(0, 1, mainIndex, spanIndex)); // 1

        // .+
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        builder.accept(getArguments(1, 2, mainIndex, spanIndex));// 2

        // ..+++ +++++++++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        builder.accept(getArguments(2, 14, mainIndex, spanIndex));// 3

        // ..+++ ++++++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        idIndex++;
        builder.accept(getArguments(2, 11, mainIndex, spanIndex, idIndex));// 4

        // ..+++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        textIndex++;
        builder.accept(getArguments(2, 5, mainIndex, spanIndex, idIndex, textIndex));// 5

        // ..... ++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        textIndex++;
        builder.accept(getArguments(5, 7, mainIndex, spanIndex, idIndex, textIndex));// 6

        // ..... ..++++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        textIndex++;
        builder.accept(getArguments(7, 11, mainIndex, spanIndex, idIndex, textIndex));// 7

        // ..... ......+
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        idIndex++;
        textIndex = -1;
        builder.accept(getArguments(11, 12, mainIndex, spanIndex, idIndex));// 8

        // ..... .......++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        idIndex++;
        builder.accept(getArguments(12, 14, mainIndex, spanIndex, idIndex));// 9

        // ..... .......++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        textIndex++;
        builder.accept(getArguments(12, 14, mainIndex, spanIndex, idIndex, textIndex));// 10

        // ..... .........+
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        idIndex = -1;
        textIndex = -1;
        builder.accept(getArguments(14, 15, mainIndex, spanIndex));// 11

        // ..... ..........+++++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        mainIndex++;
        spanIndex = -1;
        builder.accept(getArguments(15, 20, mainIndex));// 12

        // ..... ..........+
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        builder.accept(getArguments(15, 16, mainIndex, spanIndex));// 13

        // ..... ...........+
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        builder.accept(getArguments(16, 17, mainIndex, spanIndex));// 14

        // ..... ............++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        builder.accept(getArguments(17, 19, mainIndex, spanIndex));// 15

        // ..... ............++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        idIndex++;
        builder.accept(getArguments(17, 19, mainIndex, spanIndex, idIndex));

        // ..... ............++
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        textIndex++;
        builder.accept(getArguments(17, 19, mainIndex, spanIndex, idIndex, textIndex));// 16

        // ..... ..............+
        // {^cat\\:more:id}{*id}
        // 01234 567890123456789
        // 00000 000001111111111
        spanIndex++;
        idIndex = -1;
        textIndex = -1;
        builder.accept(getArguments(19, 20, mainIndex, spanIndex));
        return builder.build();
    }

    private Span getChild(int start, List<Integer> indexes) {
        Span test = doc.get(start);
        for (int idx : indexes) {
            Assertions.assertInstanceOf(SpanBranch.class, test);
            test = ((SpanBranch) test).get(idx);
        }
        return test;
    }

    @ParameterizedTest(name = "Start {index} => {4} indexes: {2} {3}")
    @MethodSource("provideParameters")
    void testStartIndex(int start, int end, int index1, List<Integer> indexes, String displayName)
            throws ExecutionException {
        Span test = getChild(index1, indexes);
        Span testing = test;
        Assertions.assertEquals(start, testing.getStartIndex(), "Start index");
    }

    @ParameterizedTest(name = "End {index} => {4} indexes: {2} {3}")
    @MethodSource("provideParameters")
    void testEndIndex(int start, int end, int index1, List<Integer> indexes, String displayName)
            throws ExecutionException {
        Span test = getChild(index1, indexes);
        Span testing = test;
        Assertions.assertEquals(end, testing.getEndIndex(), "End index");
    }

    @ParameterizedTest(name = "Find {index} => {4} indexes: {2} {3}")
    @MethodSource("provideParameters")
    void testfindChild(int start, int end, int index1, List<Integer> indexes, String displayName) {
        ArrayList<Integer> expect = new ArrayList<>();
        expect.add(index1);
        expect.addAll(indexes);
        Span child = getChild(index1, indexes);
        Assertions.assertArrayEquals(expect.toArray(), doc.findChild(child).toArray());
    }

    @ParameterizedTest(name = "Length {index} => {4} indexes: {2} {3}")
    @MethodSource("provideParameters")
    void testLength(int start, int end, int index1, List<Integer> indexes, String displayName)
            throws ExecutionException {
        int length = end - start;
        Span child = getChild(index1, indexes);
        Assertions.assertEquals(length, child.getLength());
    }
}
