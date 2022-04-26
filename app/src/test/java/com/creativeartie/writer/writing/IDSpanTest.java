package com.creativeartie.writer.writing;

import java.util.*;
import java.util.stream.*;

import org.fxmisc.richtext.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class IDSpanTest {

    private StyleBuilder builder;

    @BeforeEach
    void createBuilder() {
        builder = new StyleBuilder();
    }

    @ParameterizedTest
    @ValueSource(
        strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123",
            "Hello World Fun", "Hello" }
    )
    void notCatTest(String value) {
        IDSpan test = new IDSpan(value, builder);
        Assertions.assertAll(
            "Name",
            () -> Assertions
                .assertTrue(test.getCategories().isEmpty(), "Category"),
            () -> Assertions.assertEquals(value.trim(), test.getName(), "ID")
        );

        String[] expectStyles =
            { TypedStyles.NAME.getStyle(), TypedStyles.IDER.getStyle() };
        boolean isFirst = true;
        for (StyleSpan<Collection<String>> span : builder.getStyles()) {
            boolean current = isFirst;
            Assertions.assertAll(
                "Style",
                () -> Assertions
                    .assertEquals(value.length(), span.getLength(), "Length"),
                () -> Assertions.assertArrayEquals(
                    expectStyles, span.getStyle().toArray(new String[2]),
                    "Classes"
                ),
                () -> Assertions.assertTrue(current, "More then one span found")
            );
            isFirst = false;
        }
    }

    static Stream<Arguments> categroyProvider() {
        // @formatter:off
        return Stream.of(
            Arguments.arguments(
               //   1
               //123 12
                "cat-id", "id", Arrays.asList("cat"),
                Arrays.asList(3,    1,     2),
                Arrays.asList(true, false, true)
            ),
            Arguments.arguments(
               //    1    1
               //1234 1234 12
                "cat2-cat1-id", "id", Arrays.asList("cat2", "cat1"),
                Arrays.asList(4,    1,     4,    1,     2),
                Arrays.asList(true, false, true, false, true)
            ),
            Arguments.arguments(
               //   12345
               //123     12
                "cat  -  id", "id", Arrays.asList("cat"),
                Arrays.asList(3,    5,     2),
                Arrays.asList(true, false, true)
            ),
            Arguments.arguments(
                //        12345678        123
                //12345678         123456789   12
                 "cat  see    -  \tjoin_text-  id", "id",
                 Arrays.asList("cat see", "join text"),
                 Arrays.asList(8,    8,     9,    3,     2),
                 Arrays.asList(true, false, true, false, true)
             )
        );
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("categroyProvider")
    void withCatTest(
        String raw, String expectName, List<String> expectCat,
        List<Integer> expectedLengths, List<Boolean> isNames
    ) {
        assert expectedLengths.size() == isNames.size() :
            expectedLengths.size() + "==" + isNames.size();
        IDSpan test = new IDSpan(raw, builder);
        Assertions.assertAll(
            "Names",
            () -> Assertions.assertArrayEquals(
                expectCat.toArray(), test.getCategories().toArray(), "Category"
            ), () -> Assertions.assertEquals(expectName, test.getName(), "Name")
        );

        int i = 0;
        String[] expectedIdStyles =
            { TypedStyles.NAME.getStyle(), TypedStyles.IDER.getStyle() };
        String[] expectedSepStyles =
            { TypedStyles.OPER.getStyle(), TypedStyles.IDER.getStyle() };

        for (StyleSpan<Collection<String>> span : builder.getStyles()) {
            Assertions
                .assertTrue(i < expectedLengths.size(), "Too many spans.");
            int idx = i;
            Assertions.assertAll(
                "Styles at " + i,
                () -> Assertions.assertArrayEquals(
                    isNames.get(idx) ? expectedIdStyles : expectedSepStyles,
                    span.getStyle().toArray(new String[2]), "Styles"
                ),
                () -> Assertions.assertEquals(
                    expectedLengths.get(idx), span.getLength(), "Length"
                )
            );
            i++;
        }
        Assertions.assertEquals(isNames.size(), i, "Too little spans.");
    }

}
