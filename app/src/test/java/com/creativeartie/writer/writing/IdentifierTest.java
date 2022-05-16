package com.creativeartie.writer.writing;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.google.common.base.*;

class IdentifierTest {

    static final String[] ERROR_STYLE = { TypedStyles.FOOTNOTE.getStyle(),
        TypedStyles.ID.getStyle(), TypedStyles.ERROR.getStyle() };
    static final String[] SEP_STYLE = { TypedStyles.FOOTNOTE.getStyle(),
        TypedStyles.ID.getStyle(), TypedStyles.OPERATOR.getStyle() };
    static final String[] NAME_STYLE = { TypedStyles.FOOTNOTE.getStyle(),
        TypedStyles.ID.getStyle(), TypedStyles.NAME.getStyle() };
    static final String[] SEP_ERR_STYLE =
        { TypedStyles.FOOTNOTE.getStyle(), TypedStyles.ID.getStyle(),
            TypedStyles.OPERATOR.getStyle(), TypedStyles.ERROR.getStyle() };
    static final String[] NAME_ERR_STYLE =
        { TypedStyles.FOOTNOTE.getStyle(), TypedStyles.ID.getStyle(),
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

    @Test
    void testDuplicatedPointers() {
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, true);
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, true);
        builder.runCleanup();

        // @formatter:off
        //     1  1234 12
        // 1234 12    1
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 2, 4, 1, 2 };
        final String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };
        CommonTests.assertSpanStyles(builder, 6, (idx) -> {
            return expectedLength[idx];
        }, (idx) -> {
            return expectedStyles[idx];
        });
    }

    @DisplayName("Error ID Test")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @ValueSource(strings = { "  ", " *()&$%&^", "-dfsf", "dfdd-", "-" })
    void testErrorId(String raw) {
        final Identifier test =
            new Identifier(IdGroups.FOOTNOTE, raw, builder, false);
        Assertions.assertAll(
            () -> Assertions.assertEquals("", test.getName(), "name"),
            () -> Assertions.assertArrayEquals(
                new String[] {}, test.getCategories().toArray(), "categories"
            ), () -> Assertions.assertEquals("", test.getId(), "id")
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
        final Identifier test =
            new Identifier(IdGroups.FOOTNOTE, raw, builder, false);
        List<String> names = new ArrayList<>();
        for (String part : Splitter.on("-").split(raw)) {
            names.add(
                Joiner.on(" ").join(
                    Splitter.on(CharMatcher.anyOf(" \t_")).omitEmptyStrings()
                        .split(part)
                )
            );
        }
        String expectedId = Joiner.on("-").join(names);
        Assertions.assertAll(
            "Basic",
            () -> Assertions.assertArrayEquals(
                expectCat.toArray(), test.getCategories().toArray(), "Category"
            ),
            () -> Assertions.assertEquals(expectName, test.getName(), "Name"),
            () -> Assertions.assertEquals(expectedId, test.getId(), "Id")
        );
        CommonTests.assertSpanStyles(
            builder, isNames.size(), (idx) -> expectedLengths.get(idx),
            (idx) -> isNames.get(idx) ? NAME_STYLE : SEP_STYLE
        );
    }

    @DisplayName("Id without Categories")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @ValueSource(
        strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123",
            "Hello World Fun", "Hello" }
    )
    void testIdWithoutCategories(String value) {
        final Identifier test =
            new Identifier(IdGroups.FOOTNOTE, value, builder, false);
        Assertions.assertAll(
            "Name",
            () -> Assertions
                .assertTrue(test.getCategories().isEmpty(), "Category"),
            () -> Assertions.assertEquals(value.trim(), test.getName(), "Name"),
            () -> Assertions.assertEquals(value.trim(), test.getId(), "Id")
        );

        CommonTests.assertSpanStyles(
            builder, 1, (idx) -> value.length(), (idx) -> NAME_STYLE
        );
    }

    void testMismatchReferencePointer() {
        new Identifier(IdGroups.ENDNOTE, "aded-23", builder, true);
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();
        final String NAME_STYLE[] = { TypedStyles.ENDNOTE.getStyle(),
            TypedStyles.ID.getStyle(), TypedStyles.NAME.getStyle() };
        final String SEP_STYLE[] = { TypedStyles.ENDNOTE.getStyle(),
            TypedStyles.ID.getStyle(), TypedStyles.OPERATOR.getStyle() };

        // @formatter:off
        //     1  1234 12
        // 1234 12    1
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 2, 4, 1, 2 };
        final String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };
        CommonTests.assertSpanStyles(
            builder, 6, (idx) -> expectedLength[idx],
            (idx) -> expectedStyles[idx]
        );
    }

    @Test
    void testReferenceToEarlierPointer() {
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, true);
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();

        // @formatter:off
        //     1      1
        // 1234 123456 12
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 6, 1, 2 };
        final String[][] expectedStyles =
            { NAME_STYLE, SEP_STYLE, NAME_STYLE, SEP_STYLE, NAME_STYLE };
        CommonTests.assertSpanStyles(
            builder, 5, (idx) -> expectedLength[idx],
            (idx) -> expectedStyles[idx]
        );
    }

    @Test
    void testReferenceToLaterPointer() {
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, false);
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, true);
        builder.runCleanup();

        // @formatter:off
        //     1      1
        // 1234 123456 12
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 6, 1, 2 };
        final String[][] expectedStyles =
            { NAME_STYLE, SEP_STYLE, NAME_STYLE, SEP_STYLE, NAME_STYLE };
        CommonTests.assertSpanStyles(
            builder, 5, (idx) -> expectedLength[idx],
            (idx) -> expectedStyles[idx]
        );
    }

    @Test
    void testReferenceToNoPointer() {
        new Identifier(IdGroups.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();

        // @formatter:off
        //     1
        // 1234 12
        // aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 2 };
        final String[][] expectedStyles =
            { NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };
        CommonTests.assertSpanStyles(
            builder, 3, (idx) -> expectedLength[idx],
            (idx) -> expectedStyles[idx]
        );
    }

}
