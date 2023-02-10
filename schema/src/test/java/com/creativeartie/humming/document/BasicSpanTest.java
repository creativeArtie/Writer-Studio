package com.creativeartie.humming.document;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.opentest4j.*;

import com.google.common.collect.*;

public class BasicSpanTest {
    private static class TestData {
        private String textPrefix;
        private String spanText;
        private String textPostfix;
        private Class<?> spanClass;
        private List<TestData> dataTree;
        private Optional<TestData> spanParent;
        private int childIndex;
        private List<Integer> indexes;

        private TestData(String prefix, String text, String postfix, Class<?> clazz) {
            textPrefix = prefix;
            spanText = text;
            textPostfix = postfix;
            spanClass = clazz;
            dataTree = new ArrayList<>();
            spanParent = Optional.empty();
        }

        private TestData add(String prefix, String text, String postfix, Class<?> clazz) {
            TestData data = new TestData(prefix, text, postfix, clazz);
            data.childIndex = dataTree.size();
            data.spanParent = Optional.of(this);
            dataTree.add(data);
            testData.add(data);
            return data;
        }

        private Span getSpan(boolean isPrint) {
            List<Integer> indexes = getIndexes();
            assert useDoc != null;
            Span parent = useDoc;
            try {
                for (int i : indexes) {
                    if (isPrint) System.out.print(getSimpleName(parent) + ">");
                    if (parent instanceof Document) {
                        parent = ((Document) parent).get(i);
                    } else if (parent instanceof SpanParent) {
                        parent = ((SpanBranch) parent).get(i);
                    }
                }

                if (isPrint) System.out.println(getSimpleName(parent) + "=PASSED");
            } catch (Exception ex) {
                if (isPrint) System.out.println("=FAILED");

                Assertions.fail(ex);
            }
            return parent;
        }

        private Optional<Span> getSpanTry() {
            try {
                return Optional.of(getSpan(false));
            } catch (AssertionFailedError ex) {
                ex.printStackTrace();
                return Optional.empty();
            }
        }

        private List<Integer> getIndexes() {
            ImmutableList.Builder<Integer> builder = ImmutableList.builder();
            if (indexes == null) {
                if (spanParent.isEmpty()) {
                    indexes = builder.build();
                } else {
                    builder.addAll(spanParent.get().getIndexes());
                    builder.add(childIndex);
                    indexes = builder.build();
                }
            }
            return indexes;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < textPrefix.length(); i++) {
                builder.append("_");
            }
            builder.append(spanText.replace('\n', '␤'));
            for (int i = 0; i < textPostfix.length(); i++) {
                builder.append("_");
            }
            return builder.toString();
        }

        public String toString(int i) {
            return "[" + Integer.toString(i) + "]" + toString();
        }
    }

    private static void provideData() {
        // @formatter:off
        TestData doc = new TestData("", "Hello World!{^note}\n!^note=*test* only\\!\n=Chapter 1", "", Document.class);
        testData.add(doc);
        TestData sec = doc.add(     "", "Hello World!{^note}\n!^note=*test* only\\!\n", "=Chapter 1", SectionDivision.class);

        TestData lin = sec.add("", "Hello World!{^note}\n", "!^note=*test* only\\!\n=Chapter 1", LineSpan.class);
        TestData lxt = lin.add("", "Hello World!{^note}", "\n!^note=*test* only\\!\n=Chapter 1", LineText.class);
        TestData txt = lxt.add("", "Hello World!", "{^note}\n!^note=*test* only\\!\n=Chapter 1", TextSpan.class);
        txt.add(               "", "Hello World!", "{^note}\n!^note=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        TestData ref = lxt.add("Hello World!", "{^note}", "\n!^note=*test* only\\!\n=Chapter 1", ReferenceSpan.class);
        ref.add(               "Hello World!", "{", "^note}\n!^note=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        ref.add(               "Hello World!{", "^", "note}\n!^note=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        TestData id = ref.add( "Hello World!{^", "note", "}\n!^note=*test* only\\!\n=Chapter 1", IdentitySpan.class);
        txt = id.add(          "Hello World!{^", "note", "}\n!^note=*test* only\\!\n=Chapter 1", TextSpan.class);
        txt.add(               "Hello World!{^", "note", "}\n!^note=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        ref.add(               "Hello World!{^note", "}", "\n!^note=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        lin.add(               "Hello World!{^note}", "\n", "!^note=*test* only\\!\n=Chapter 1", SpanLeaf.class);

        lin = sec.add("Hello World!{^note}\n", "!^note=*test* only\\!\n", "=Chapter 1", ReferenceLine.class);
        lin.add(      "Hello World!{^note}\n", "!", "^note=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        lin.add(      "Hello World!{^note}\n!", "^", "note=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        id = lin.add( "Hello World!{^note}\n!^", "note", "=*test* only\\!\n=Chapter 1", IdentitySpan.class);
        txt = id.add( "Hello World!{^note}\n!^", "note", "=*test* only\\!\n=Chapter 1", TextSpan.class);
        txt.add(      "Hello World!{^note}\n!^", "note", "=*test* only\\!\n=Chapter 1", SpanLeaf.class);
        lin.add(      "Hello World!{^note}\n!^note", "=", "*test* only\\!\n=Chapter 1", SpanLeaf.class);
        lxt = lin.add("Hello World!{^note}\n!^note=", "*test* only\\!", "\n=Chapter 1", LineText.class);
        lxt.add(      "Hello World!{^note}\n!^note=", "*", "test* only\\!\n=Chapter 1", SpanLeaf.class);
        txt = lxt.add("Hello World!{^note}\n!^note=*", "test", "* only\\!\n=Chapter 1", TextSpan.class);
        txt.add(      "Hello World!{^note}\n!^note=*", "test", "* only\\!\n=Chapter 1", SpanLeaf.class);
        lxt.add(      "Hello World!{^note}\n!^note=*test", "*", " only\\!\n=Chapter 1", SpanLeaf.class);
        txt = lxt.add("Hello World!{^note}\n!^note=*test*", " only\\!", "\n=Chapter 1", TextSpan.class);
        txt.add(      "Hello World!{^note}\n!^note=*test*", " only", "\\!\n=Chapter 1", SpanLeaf.class);
        txt.add(      "Hello World!{^note}\n!^note=*test* only", "\\!", "\n=Chapter 1", SpanLeaf.class);
        lin.add(      "Hello World!{^note}\n!^note=*test* only\\!", "\n", "=Chapter 1", SpanLeaf.class);

        sec = doc.add("Hello World!{^note}\n!^note=*test* only\\!\n", "=Chapter 1", "", SectionDivision.class);
        lin = sec.add("Hello World!{^note}\n!^note=*test* only\\!\n", "=Chapter 1", "", HeadingLine.class);
        lin.add(      "Hello World!{^note}\n!^note=*test* only\\!\n", "=", "Chapter 1", SpanLeaf.class);
        lxt = lin.add("Hello World!{^note}\n!^note=*test* only\\!\n=", "Chapter 1", "", LineText.class);
        txt = lxt.add("Hello World!{^note}\n!^note=*test* only\\!\n=", "Chapter 1", "", TextSpan.class);
        txt.add(      "Hello World!{^note}\n!^note=*test* only\\!\n=", "Chapter 1", "", SpanLeaf.class);
        // @formatter:on
    }

    private static String getSimpleName(Span span) {
        if (span instanceof SpanLeaf) {
            return ((SpanLeaf) span).getStyle().toString();
        } else if (span instanceof LineSpan) {
            return ((LineSpan) span).getLineStyle().toString();
        }
        return span.getClass().getSimpleName();
    }

    private static Document useDoc;
    private static List<TestData> testData;

    @BeforeAll
    public static void setup() {
        useDoc = new Document();
        useDoc.updateText("Hello World!{^note}\n!^note=*test* only\\!\n=Chapter 1");
        testData = new ArrayList<>();
        provideData();
    }

    @Test
    public void testCount() {
        int count = 1; // << count useDoc
        for (SpanBranch child : useDoc) {
            count += countSpans(child) + 1;
        }
        Assertions.assertEquals(count, testData.size());
    }

    private int countSpans(SpanBranch branch) {
        int count = 0;
        for (Span child : branch) {
            count++;
            if (child instanceof SpanBranch) count += countSpans((SpanBranch) child);
        }
        return count;
    }

    private static Stream<Arguments> provideBasic() {
        Stream.Builder<Arguments> args = Stream.builder();
        int i = 1;
        for (TestData data : testData) {
            args.accept(Arguments.of(i, data));
            i++;
        }
        return args.build();
    }

    @ParameterizedTest(name = "basic[{index}] => {1}")
    @MethodSource("provideBasic")
    public void testBasic(int i, TestData data) {
        String name = data.spanClass.getSimpleName();
        if (name == "") {
            name = "LineSpan";
        }
        System.out.printf("%2d -> %15s:%s", i, name, data.toString());
        data.getSpan(true);
    }

    private static Stream<Arguments> provideLengths() {
        Stream.Builder<Arguments> args = Stream.builder();
        for (TestData data : testData) {
            Optional<Span> found = data.getSpanTry();
            String text = data.toString();
            found.ifPresent((span) -> {
                args.accept(Arguments.of(data.spanText.length(), span, text));
            });
        }
        return args.build();
    }

    @ParameterizedTest(name = "len[{index}] => {2}")
    @MethodSource("provideLengths")
    public void testLengths(int length, Span test, String display) {
        Assertions.assertEquals(length, test.getLength());
    }

    private static Stream<Arguments> provideRaws() {
        Stream.Builder<Arguments> args = Stream.builder();
        int i = 1;
        for (TestData data : testData) {
            if (data.spanClass == SpanLeaf.class) {
                Optional<Span> found = data.getSpanTry();
                String text = data.toString(i);
                found.ifPresent((span) -> args.accept(Arguments.of(data.spanText, span, text)));
            }
            i++;
        }
        return args.build();
    }

    @ParameterizedTest(name = "raw[{index}] => {2}")
    @MethodSource("provideRaws")
    public void testRaws(String expectRaw, SpanLeaf test, String display) {
        Assertions.assertEquals(expectRaw, test.toString());
    }

    @Test
    public void testLeaves() {
        ImmutableList.Builder<String> expected = ImmutableList.builder();
        for (TestData data : testData) {
            if (data.spanClass == SpanLeaf.class) {
                Optional<Span> found = data.getSpanTry();
                found.ifPresent((span) -> expected.add(data.spanText));
            }
        }
        Assertions.assertArrayEquals(
                expected.build().toArray(), useDoc.convertLeaves((leaf) -> leaf.toString()).toArray()
        );
    }

    private static Stream<Arguments> provideStart() {
        Stream.Builder<Arguments> args = Stream.builder();
        for (TestData data : testData) {
            Optional<Span> found = data.getSpanTry();
            String text = data.toString();
            if (found.isPresent()) {
                args.accept(Arguments.of(data.textPrefix.length(), found.get(), text));
            }
        }
        return args.build();
    }

    @ParameterizedTest(name = "start[{index}] => {2}")
    @MethodSource("provideStart")
    public void testStart(int expectStart, Span test, String display) {
        Assertions.assertEquals(expectStart, test.getStartIndex());
    }

    private static Stream<Arguments> provideEnd() {
        Stream.Builder<Arguments> args = Stream.builder();
        for (TestData data : testData) {
            Optional<Span> found = data.getSpanTry();
            String text = data.toString();
            if (found.isPresent()) {
                args.accept(Arguments.of(data.textPrefix.length() + data.spanText.length(), found.get(), text));
            }
        }
        return args.build();
    }

    @ParameterizedTest(name = "end[{index}] => {2}")
    @MethodSource("provideEnd")
    public void testEnd(int expectStart, Span test, String display) {
        Assertions.assertEquals(expectStart, test.getEndIndex());
    }

    private static Stream<Arguments> provideClass() {
        Stream.Builder<Arguments> args = Stream.builder();
        for (TestData data : testData) {
            Optional<Span> found = data.getSpanTry();
            String text = data.toString();
            if (found.isPresent()) {
                args.accept(Arguments.of(data.spanClass, found.get(), text));
            }
        }
        return args.build();
    }

    @ParameterizedTest(name = "class[{index}] => {2}")
    @MethodSource("provideClass")
    public void testClass(Class<?> expectClass, Span test, String display) {
        Assertions.assertInstanceOf(expectClass, test);
    }

    private static Stream<Arguments> provideFindChild() {
        Stream.Builder<Arguments> args = Stream.builder();
        for (TestData data : testData) {
            Optional<Span> found = data.getSpanTry();
            String text = data.toString();
            if (found.isPresent()) {
                args.accept(Arguments.of(data.getIndexes().toArray(), found.get(), text));
            }
        }
        return args.build();
    }

    @ParameterizedTest(name = "findChild[{index}] => {2}")
    @MethodSource("provideFindChild")
    public void testFindChild(Object[] expectedIdexes, Span child, String display) {
        Assertions.assertArrayEquals(expectedIdexes, useDoc.findChild(child).toArray());
    }

    @AfterAll
    public static void printCacheRecord() {
        useDoc.printCacheStats();
    }

    private static Stream<Arguments> provideLocateSpans() {
        Stream.Builder<Arguments> args = Stream.builder();
        for (int i = 0; i < testData.get(0).spanText.length(); i++) {
            TestData expectLeaf = null;
            for (TestData expectSpan : testData) {
                if (expectSpan.spanClass != SpanLeaf.class) continue;
                if (i >= expectSpan.textPrefix.length() &&
                        i <= (expectSpan.textPrefix.length() + expectSpan.spanText.length())) {
                    expectLeaf = expectSpan;
                }
            }
            assert expectLeaf != null;

            ArrayList<Span> expectedSpans = new ArrayList<>();
            Span parent = useDoc;
            String display = "";
            for (int index : expectLeaf.getIndexes()) {
                if (parent instanceof Document) {
                    parent = ((Document) parent).get(index);
                } else if (parent instanceof SpanParent) {
                    expectedSpans.add(parent);
                    parent = ((SpanBranch) parent).get(index);
                }
                display += getSimpleName(parent) + ">";
            }
            assert parent instanceof SpanLeaf;
            expectedSpans.add(parent);
            display += parent.toString().replace("\n", "␤");
            args.accept(Arguments.of(expectedSpans.toArray(new Span[0]), i, display));
        }
        return args.build();
    }

    @ParameterizedTest(name = "locate [{1}] = {2}")
    @MethodSource("provideLocateSpans")
    public void testLocateSpans(Span[] list, int index, String display) {
        for (Span span : list) {
            System.out.print(getSimpleName(span) + ">");
            if (span instanceof SpanLeaf) {
                System.out.print(span.toString());
            }
        }
        System.out.println();
        for (Span span : useDoc.locateChildren(index)) {
            System.out.print(getSimpleName(span) + ">");
            if (span instanceof SpanLeaf) {
                System.out.print(span.toString());
            }
        }
        System.out.println();
        System.out.println();
        Assertions.assertArrayEquals(list, useDoc.locateChildren(index).toArray());
    }
}
