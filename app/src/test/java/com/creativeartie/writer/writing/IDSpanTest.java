package com.creativeartie.writer.writing;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class IDSpanTest {

    static final String[] ERROR_STYLE = { TypedStyles.FOOT.getStyle(),
        TypedStyles.IDER.getStyle(), TypedStyles.ERROR.getStyle() };
    static final String[] SEP_STYLE = { TypedStyles.FOOT.getStyle(),
        TypedStyles.IDER.getStyle(), TypedStyles.OPER.getStyle() };
    static final String[] NAME_STYLE = { TypedStyles.FOOT.getStyle(),
        TypedStyles.IDER.getStyle(), TypedStyles.NAME.getStyle() };
    static final String[] SEP_ERR_STYLE =
        { TypedStyles.FOOT.getStyle(), TypedStyles.IDER.getStyle(),
            TypedStyles.OPER.getStyle(), TypedStyles.ERROR.getStyle() };
    static final String[] NAME_ERR_STYLE =
        { TypedStyles.FOOT.getStyle(), TypedStyles.IDER.getStyle(),
            TypedStyles.NAME.getStyle(), TypedStyles.ERROR.getStyle() };

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
                //        12345678          123
                //12345678         123456789   12
                 "cat  see    -  \tjoin_text-  id", "id",
                 Arrays.asList("cat see", "join text"),
                 Arrays.asList(8,    8,     9,    3,     2),
                 Arrays.asList(true, false, true, false, true)
             ), Arguments.arguments(
                //1      123  1
                // 123456   12
                 " 12  34 - id ", "id",
                 Arrays.asList("12 34"),
                 Arrays.asList(1,     6,    3,     2,    1),
                 Arrays.asList(false, true, false, true, false)
            )
        );
        // @formatter:on
    }

    private DocBuilder builder;

    @BeforeEach
    void createBuilder() {
        builder = new DocBuilder();
    }

    @DisplayName("Error ID Test")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @ValueSource(strings = { "  ", " *()&$%&^" })
    void testErrorId(String raw) {
        final IdSpan test = new IdSpan(IdGroups.FOOTNOTE, raw, builder, false);
        Assertions.assertAll(
            () -> Assertions.assertEquals("", test.getName(), "name"),
            () -> Assertions.assertArrayEquals(
                new String[] {}, test.getCategories().toArray(), "categories"
            )
        );
        CommonTests.assertSpanStyles(
            builder, 1, (index) -> raw.length(), (index) -> ERROR_STYLE
        );
    }

    @DisplayName("Id With Categories")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @MethodSource("categroyProvider")
    void testIdwithCategory(
        String raw, String expectName, List<String> expectCat,
        List<Integer> expectedLengths, List<Boolean> isNames
    ) {
        assert expectedLengths.size() == isNames.size() :
            expectedLengths.size() + "==" + isNames.size();
        final IdSpan test = new IdSpan(IdGroups.FOOTNOTE, raw, builder, false);
        Assertions.assertAll(
            "Basic",
            () -> Assertions.assertArrayEquals(
                expectCat.toArray(), test.getCategories().toArray(), "Category"
            ), () -> Assertions.assertEquals(expectName, test.getName(), "Name")
        );
        CommonTests.assertSpanStyles(
            builder, isNames.size(), (idx) -> expectedLengths.get(idx),
            (idx) -> isNames.get(idx) ? NAME_STYLE : SEP_STYLE
        );

        CommonTests.printStyle(raw, builder);
    }

    @DisplayName("Id without Categories")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @ValueSource(
        strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123",
            "Hello World Fun", "Hello" }
    )
    void testIdWithoutCategories(String value) {
        final IdSpan test =
            new IdSpan(IdGroups.FOOTNOTE, value, builder, false);
        Assertions.assertAll(
            "Name",
            () -> Assertions
                .assertTrue(test.getCategories().isEmpty(), "Category"),
            () -> Assertions.assertEquals(value.trim(), test.getName(), "ID")
        );

        CommonTests.assertSpanStyles(
            builder, 1, (idx) -> value.length(), (idx) -> NAME_STYLE
        );
    }

    @Test
    void testReferenceToLaterPointer() {
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, false);
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, true);
        builder.runCleanup();

        // @formatter:off
        //     1      1
        // 1234 123456 12
        // aded-23aded-23
        // @formatter:on
        int expectedLength[] = { 4, 1, 6, 1, 2 };
        String[][] expectedStyles =
            { NAME_STYLE, SEP_STYLE, NAME_STYLE, SEP_STYLE, NAME_STYLE };
        CommonTests.printStyle("aded-23aded-23", builder);
        CommonTests.assertSpanStyles(builder, 5, (idx) -> {
            return expectedLength[idx];
        }, (idx) -> {
            return expectedStyles[idx];
        });
    }

    @Test
    void testReferenceToEarlierPointer() {
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, true);
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();

        // @formatter:off
        //     1      1
        // 1234 123456 12
        // aded-23aded-23
        // @formatter:on
        int expectedLength[] = { 4, 1, 6, 1, 2 };
        String[][] expectedStyles =
            { NAME_STYLE, SEP_STYLE, NAME_STYLE, SEP_STYLE, NAME_STYLE };
        CommonTests.printStyle("aded-23aded-23", builder);
        CommonTests.assertSpanStyles(builder, 5, (idx) -> {
            return expectedLength[idx];
        }, (idx) -> {
            return expectedStyles[idx];
        });
    }

    @Test
    void testReferenceToNoPointer() {
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();

        // @formatter:off
        //     1
        // 1234 12
        // aded-23
        // @formatter:on
        int expectedLength[] = { 4, 1, 2 };
        String[][] expectedStyles =
            { NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };
        CommonTests.printStyle("aded-23aded-23", builder);
        CommonTests.assertSpanStyles(builder, 3, (idx) -> {
            return expectedLength[idx];
        }, (idx) -> {
            return expectedStyles[idx];
        });
    }

    void testMismatchReferencePointer() {
        new IdSpan(IdGroups.ENDNOTE, "aded-23", builder, true);
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();
        String NAME_STYLE[] = { TypedStyles.ENDR.getStyle(),
            TypedStyles.IDER.getStyle(), TypedStyles.NAME.getStyle() };
        String SEP_STYLE[] = { TypedStyles.ENDR.getStyle(),
            TypedStyles.IDER.getStyle(), TypedStyles.OPER.getStyle() };

        // @formatter:off
        //     1  1234 12
        // 1234 12    1
        // aded-23aded-23
        // @formatter:on
        int expectedLength[] = { 4, 1, 2, 4, 1, 2 };
        String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };
        CommonTests.printStyle("aded-23aded-23", builder);
        CommonTests.assertSpanStyles(builder, 6, (idx) -> {
            return expectedLength[idx];
        }, (idx) -> {
            return expectedStyles[idx];
        });
    }

    @Test
    void testDuplicatedPointers() {
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, true);
        new IdSpan(IdGroups.FOOTNOTE, "aded-23", builder, true);
        builder.runCleanup();

        // @formatter:off
        //     1  1234 12
        // 1234 12    1
        // aded-23aded-23
        // @formatter:on
        int expectedLength[] = { 4, 1, 2, 4, 1, 2 };
        String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };
        CommonTests.printStyle("aded-23aded-23", builder);
        CommonTests.assertSpanStyles(builder, 6, (idx) -> {
            return expectedLength[idx];
        }, (idx) -> {
            return expectedStyles[idx];
        });
    }

}
