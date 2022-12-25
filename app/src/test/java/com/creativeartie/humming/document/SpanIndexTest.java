package com.creativeartie.humming.document;

import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class SpanIndexTest {
    private static SpanBranch rootBranch;
    private static String text = "{^cat\\:more:id}";
    private static int testIndex;

    private static Arguments getArguements(int start, int end, int... indexes) {
        String extractText = text.substring(start, end);
        String indexText = "";
        for (int index : indexes) {
            if (indexText != "") {
                indexText += ", ";
            }
            indexText += Integer.toString(index);
        }
        indexText = " " + indexText + " ";

        String displayName = String.format("%2d:%-16s %s", testIndex, extractText, indexText);
        testIndex++;
        return Arguments.of(start, end, indexes, displayName);
    }

    @BeforeAll
    public static void setup() {
        Document doc = new Document();
        rootBranch = ReferencePointerSpan.createSpan(new SpanBranch(doc), text);
        doc.addChild(rootBranch);
    }

    private static Stream<Arguments> provideParameters() {
        Stream.Builder<Arguments> builder = Stream.builder();
        int mainIndex = 0;
        testIndex = 0;

        // +
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(0, 1, mainIndex++));

        // .+
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(1, 2, mainIndex++));

        // ..+++ +++++++++
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(2, 14, mainIndex));

        int idIndex = 0;
        // ..+++ ++++++
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(2, 11, mainIndex, idIndex));

        int text = 0;
        // ..+++
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(2, 5, mainIndex, idIndex, text++));

        // ..... ++
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(5, 7, mainIndex, idIndex, text++));

        // ........++++
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(7, 11, mainIndex, idIndex, text++));

        // ............+
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(11, 12, mainIndex, ++idIndex));

        // .............++
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(12, 14, mainIndex, ++idIndex));

        // .............++
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(12, 14, mainIndex, idIndex, 0));

        // ...............+
        // {^cat\\:more:id}
        // 01234 5678901234
        // 00000 0000011111
        builder.accept(getArguements(14, 15, ++mainIndex));
        return builder.build();
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource("provideParameters")
    void testWithArgumentsSource(int start, int end, int[] index, String displayName) {
        System.out.println(displayName);

        Span test = rootBranch;
        for (int i : index) {
            Assertions.assertInstanceOf(SpanBranch.class, test);
            test = ((SpanBranch) test).get(i);
        }
        Span testing = test;
        Assertions.assertAll(
                () -> Assertions.assertEquals(start, testing.getStartIndex(), "Start index"),
                () -> Assertions.assertEquals(end, testing.getEndIndex(), "End index")
        );
    }
}
