package com.creativeartie.writer.writing;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.google.common.base.*;

class IdentifierTest {

    static final String[] ERROR_STYLE = { SpanStyles.FOOTNOTE.getStyle(),
        SpanStyles.ID.getStyle(), SpanStyles.ERROR.getStyle() };
    static final String[] SEP_STYLE = { SpanStyles.FOOTNOTE.getStyle(),
        SpanStyles.ID.getStyle(), SpanStyles.OPERATOR.getStyle() };
    static final String[] NAME_STYLE = { SpanStyles.FOOTNOTE.getStyle(),
        SpanStyles.ID.getStyle(), SpanStyles.NAME.getStyle() };
    static final String[] SEP_ERR_STYLE = { SpanStyles.FOOTNOTE.getStyle(),
        SpanStyles.ID.getStyle(), SpanStyles.OPERATOR.getStyle(),
        SpanStyles.ERROR.getStyle() };
    static final String[] NAME_ERR_STYLE = { SpanStyles.FOOTNOTE.getStyle(),
        SpanStyles.ID.getStyle(), SpanStyles.NAME.getStyle(), SpanStyles.ERROR
            .getStyle() };

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
        builder = new DocBuilder(true);
    }

    @Test
    void testDuplicatedPointers() {
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, true);
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, true);
        builder.runCleanup();

        // @formatter:off
        //     1  1234 12
        // 1234 12    1
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 2, 4, 1, 2 };
        final String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };

        new SpanTester(builder, 6).addSpanLength((idx) -> {
            return expectedLength[idx];
        }).addSpanStyle((idx) -> {
            return expectedStyles[idx];
        }).assertAll();
    }

    @DisplayName("Error ID Test")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @ValueSource(strings = { "  ", " *()&$%&^", "-dfsf", "dfdd-", "-" })
    void testErrorId(String raw) {
        final IdMarkerPhrase test = new IdMarkerPhrase(
            IdTypes.FOOTNOTE, raw, builder, false
        );
        Assertions.assertAll(
            () -> Assertions.assertEquals("", test.getName(), "name"),
            () -> Assertions.assertArrayEquals(
                new String[] {}, test.getCategories().toArray(), "categories"
            ), () -> Assertions.assertEquals("", test.getId(), "id")
        );
        new SpanTester(builder, 1).addSpanLength((index) -> raw.length())
            .addSpanStyle((index) -> ERROR_STYLE).assertAll();
    }

    @DisplayName("Id With Categories")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @MethodSource("categroyProvider")
    void testIdwithCategory(
        String raw, String expectName, List<String> expectCat, List<
            Integer> expectedLengths, List<Boolean> isNames
    ) {
        assert expectedLengths.size() == isNames.size() : expectedLengths
            .size() + "==" + isNames.size();
        final IdMarkerPhrase test = new IdMarkerPhrase(
            IdTypes.FOOTNOTE, raw, builder, false
        );
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
            "Basic", () -> Assertions.assertArrayEquals(
                expectCat.toArray(), test.getCategories().toArray(), "Category"
            ), () -> Assertions.assertEquals(
                expectName, test.getName(), "Name"
            ), () -> Assertions.assertEquals(expectedId, test.getId(), "Id")
        );
        new SpanTester(builder, isNames.size()).addSpanLength(
            (idx) -> expectedLengths.get(idx)
        ).addSpanStyle((idx) -> isNames.get(idx) ? NAME_STYLE : SEP_STYLE)
            .assertAll();

    }

    @DisplayName("Id without Categories")
    @ParameterizedTest(name = "{displayName}[{index}]: \"{0}\"")
    @ValueSource(
        strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123",
            "Hello World Fun", "Hello" }
    )
    void testIdWithoutCategories(String value) {
        final IdMarkerPhrase test = new IdMarkerPhrase(
            IdTypes.FOOTNOTE, value, builder, false
        );
        Assertions.assertAll(
            "Name", () -> Assertions.assertTrue(
                test.getCategories().isEmpty(), "Category"
            ), () -> Assertions.assertEquals(
                value.trim(), test.getName(), "Name"
            ), () -> Assertions.assertEquals(value.trim(), test.getId(), "Id")
        );

        new SpanTester(builder, 1).addSpanLength((idx) -> value.length())
            .addSpanStyle((idx) -> NAME_STYLE).assertAll();
    }

    void testMismatchReferencePointer() {
        new IdMarkerPhrase(IdTypes.ENDNOTE, "aded-23", builder, true);
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();
        final String NAME_STYLE[] = { SpanStyles.ENDNOTE.getStyle(),
            SpanStyles.ID.getStyle(), SpanStyles.NAME.getStyle() };
        final String SEP_STYLE[] = { SpanStyles.ENDNOTE.getStyle(),
            SpanStyles.ID.getStyle(), SpanStyles.OPERATOR.getStyle() };

        // @formatter:off
        //     1  1234 12
        // 1234 12    1
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 2, 4, 1, 2 };
        final String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            NAME_ERR_STYLE, SEP_ERR_STYLE, NAME_ERR_STYLE };
        new SpanTester(builder, 6).addSpanLength((idx) -> expectedLength[idx])
            .addSpanStyle((idx) -> expectedStyles[idx]).assertAll();
    }

    @Test
    void testReferenceToEarlierPointer() {
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, true);
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();

        // @formatter:off
        //     1      1
        // 1234 123456 12
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 6, 1, 2 };
        final String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            SEP_STYLE, NAME_STYLE };
        new SpanTester(builder, 5).addSpanLength((idx) -> expectedLength[idx])
            .addSpanStyle((idx) -> expectedStyles[idx]).assertAll();
    }

    @Test
    void testReferenceToLaterPointer() {
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, false);
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, true);
        builder.runCleanup();

        // @formatter:off
        //     1      1
        // 1234 123456 12
        // aded-23aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 6, 1, 2 };
        final String[][] expectedStyles = { NAME_STYLE, SEP_STYLE, NAME_STYLE,
            SEP_STYLE, NAME_STYLE };
        new SpanTester(builder, 5).addSpanLength((idx) -> expectedLength[idx])
            .addSpanStyle((idx) -> expectedStyles[idx]).assertAll();
    }

    @Test
    void testReferenceToNoPointer() {
        new IdMarkerPhrase(IdTypes.FOOTNOTE, "aded-23", builder, false);
        builder.runCleanup();

        // @formatter:off
        //     1
        // 1234 12
        // aded-23
        // @formatter:on
        final int expectedLength[] = { 4, 1, 2 };
        final String[][] expectedStyles = { NAME_ERR_STYLE, SEP_ERR_STYLE,
            NAME_ERR_STYLE };
        new SpanTester(builder, 3).addSpanLength((idx) -> expectedLength[idx])
            .addSpanStyle((idx) -> expectedStyles[idx]).assertAll();
    }

}
