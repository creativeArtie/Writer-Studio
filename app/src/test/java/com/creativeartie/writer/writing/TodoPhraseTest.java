package com.creativeartie.writer.writing;

import org.junit.jupiter.api.*;

class TodoPhraseTest {

    private static final String textStyle[] = { SpanStyles.TODO.getStyle(),
        SpanStyles.TEXT.getStyle() }, optStyle[] = { SpanStyles.TODO
            .getStyle(), SpanStyles.OPERATOR.getStyle() };

    private static final int startLength = 2, textLength = 4, endLength = 1;

    private DocBuilder builder;

    private void assertCommon(
        TodoPhrase test, int[] lengths, String[][] styles, boolean hasText
    ) {
        Assertions.assertAll(
            () -> Assertions.assertEquals(
                hasText ? "help" : "", test.getTodoText(), "text"
            ), () -> CommonTests.assertSpanStyles(
                true, builder, lengths.length, (idx) -> lengths[idx], (
                    idx) -> styles[idx]
            )
        );
    }

    @BeforeEach
    void setUp() throws Exception {
        builder = new DocBuilder(true);
    }

    @Test
    void testFull() {
        TodoPhrase test = new TodoPhrase("{!help}", builder);
        assertCommon(
            test, new int[] { startLength, textLength, endLength },
            new String[][] { optStyle, textStyle, optStyle }, true
        );
    }

    @Test
    void testNoEnd() {
        TodoPhrase test = new TodoPhrase("{!help", builder);
        assertCommon(
            test, new int[] { startLength, textLength }, new String[][] {
                optStyle, textStyle }, true
        );
    }

    @Test
    void testNoText() {
        TodoPhrase test = new TodoPhrase("{!}", builder);
        assertCommon(test, new int[] { 3 }, new String[][] { optStyle }, false);
    }

    @Test
    void testStartOnly() {
        TodoPhrase test = new TodoPhrase("{!", builder);
        assertCommon(
            test, new int[] { startLength }, new String[][] { optStyle }, false
        );
    }

}
