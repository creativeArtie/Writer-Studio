package com.creativeartie.writer.writing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.writer.writing.LinePhrase.*;
import com.google.common.base.*;

class TextPhraseTest {

    private DocBuilder docBuilder;

    @BeforeEach
    void setUp() throws Exception {
        docBuilder = new DocBuilder(true);
    }

    private LinePhrase createTest(String text) {
        return createTest(text, LineTypes.NORMAL);
    }

    private LinePhrase createTest(String text, LineTypes ender) {
        return new LinePhrase(text, docBuilder, ender);
    }

    @ParameterizedTest
    @CsvSource({ "*,BOLD", "`,ITALICS", "_,UNDERLINE" })
    void testFormat(String format, String type) {
        String textStyle[] = { SpanStyles.PARAGRAPH.getStyle(),
            SpanStyles.TEXT.getStyle() };
        String formatStyle[] = { SpanStyles.PARAGRAPH.getStyle(), SpanStyles
            .valueOf(type).getStyle(), SpanStyles.TEXT.getStyle() };
        String optStyle[] = { SpanStyles.PARAGRAPH.getStyle(),
            SpanStyles.OPERATOR.getStyle() };
        String expectedText[] = { "Start", format, "text", format, "end" };
        String text = Joiner.on("").join(expectedText);

        String[][] expectedStyle = { textStyle, optStyle, formatStyle, optStyle,
            textStyle };
        int[] expectedLengths = { 5, 1, 4, 1, 3 };
        LinePhrase test = createTest(text);

        int i = 0;
        for (Span child : test.getChildren()) {
            int idx = i;
            Assertions.assertAll(
                "children", () -> Assertions.assertEquals(
                    child.getClass(), TextPhrase.class, "class"
                ), () -> Assertions.assertEquals(
                    ((TextPhrase) child).getText(), expectedText[idx], "text"
                )
            );
            i += 2;
        }
        CommonTests.assertSpanStyles(
            docBuilder, 5, (idx) -> expectedLengths[idx], (
                idx) -> expectedStyle[idx]
        );
    }

}
