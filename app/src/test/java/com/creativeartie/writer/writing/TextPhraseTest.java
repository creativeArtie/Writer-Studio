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
        return createTest(text, LinePhrase.TextEnders.NONE);
    }

    private LinePhrase createTest(String text, LinePhrase.TextEnders ender) {
        return new LinePhrase(text, docBuilder, ender);
    }

    @ParameterizedTest
    @CsvSource({ "*,BOLD", "`,ITALICS", "_,UNDERLINE" })
    void testFormat(String format, String type) {
        String textStyle[] = { SpanStyles.TEXT.getStyle() };
        String formatStyle[] = { SpanStyles.valueOf(type).getStyle(),
            SpanStyles.TEXT.getStyle() };
        String optStyle[] = { SpanStyles.OPERATOR.getStyle() };
        String expectedText[] = { "Start", format, "text", format, "end" };
        String text = Joiner.on("").join(expectedText);

        String[][] expectedStyle = { textStyle, optStyle, formatStyle, optStyle,
            textStyle };
        int[] expectedLengths = { 5, 1, 4, 1, 3 };
        LinePhrase test = createTest(text);

        int i = 0;
        for (TextSpan child : test.getChildren()) {
            int idx = i;
            Assertions.assertAll(
                "children", () -> Assertions.assertEquals(
                    child.getClass(), TextSpan.class, "class"
                ), () -> Assertions.assertEquals(
                    expectedText[idx], ((TextSpan) child).getText(), "text"
                )
            );
            i += 2;
        }
        CommonTests.assertSpanStyles(
            true, docBuilder, 5, (idx) -> expectedLengths[idx], (
                idx) -> expectedStyle[idx]
        );
    }

    @Test
    void testTodo() {
        String basic[] = { SpanStyles.TEXT.getStyle() };
        String todoOp[] = { SpanStyles.TODO.getStyle(), SpanStyles.OPERATOR
            .getStyle() };
        String todoText[] = { SpanStyles.TODO.getStyle(), SpanStyles.TEXT
            .getStyle() };
        // @formatter:off
        //                     12         1
        //                  123  123456789 12345
        String inputText = "abc{!todo text} todo";
        // @formatter:on
        String[][] expectedStyles = { basic, todoOp, todoText, todoOp, basic };
        int[] expectedLenghts = { 3, 2, 9, 1, 5 };

        createTest(inputText);
        CommonTests.assertSpanStyles(
            true, docBuilder, 5, (idx) -> expectedLenghts[idx], (
                idx) -> expectedStyles[idx]
        );
    }

}
