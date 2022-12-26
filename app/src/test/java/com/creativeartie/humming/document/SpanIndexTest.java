package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class SpanIndexTest {
    private static String span1 = "{^cat\\:more:id}";
    private static String span2 = "{*id}";
    private static String spans = span1 + span2;
    private static int testIndex;
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
        String indexText = Integer.toString(index1);
        for (int index : indexes) {
            indexText += ", ";
            indexText += Integer.toString(index);
        }
        indexText = "[" + indexText + "]";

        String displayName = String.format("%2d:%-16s %s", testIndex, extractText, indexText);
        testIndex++;
        return Arguments.of(start, end, index1, indexes, displayName);
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
        testIndex = 0;

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

    @ParameterizedTest(name = "{4}")
    @MethodSource("provideParameters")
    void testWithArgumentsSource(int start, int end, int index1, int[] index, String displayName) {
        System.out.println(displayName);

        Span test = doc.get(index1);
        int i = 0;
        for (int idx : index) {
            Assertions.assertInstanceOf(SpanBranch.class, test);
            System.out.printf("At index (%d): %d\n", i, idx);
            test = ((SpanBranch) test).get(idx);
            i++;
        }
        Span testing = test;
        Assertions.assertAll(
                () -> Assertions.assertEquals(start, testing.getStartIndex(), "Start index"),
                () -> Assertions.assertEquals(end, testing.getEndIndex(), "End index")
        );
        System.out.println("\t\tPASSED");
    }
}
