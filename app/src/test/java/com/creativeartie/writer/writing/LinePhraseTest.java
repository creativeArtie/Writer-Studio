package com.creativeartie.writer.writing;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.google.common.base.*;

class LinePhraseTest {

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
        String rawText[] = { "Start", format, "text", format, "end" };
        String text = Joiner.on("").join(rawText);

        String[][] expectedStyle = { textStyle, optStyle, formatStyle, optStyle,
            textStyle };
        int[] expectedLengths = { 5, 1, 4, 1, 3 };
        LinePhrase test = createTest(text);

        String expectText[] = { "Start", "text", "end" };
        String actualText[] = new String[test.getChildren().size()];
        Iterator<Span> texts = test.getChildren().iterator();
        for (int i = 0; i < actualText.length; i++) {
            Span child = texts.next();
            Assertions.assertEquals(
                LinePhrase.TextSpan.class, child.getClass(), "Class for span " +
                    Integer.toString(i)
            );
            actualText[i] = ((LinePhrase.TextSpan) child).getText();
        }
        Assertions.assertArrayEquals(expectText, actualText, "Output text");

        new SpanTester(docBuilder, 5).addSpanLength(
            (idx) -> expectedLengths[idx]
        ).addSpanStyle((idx) -> expectedStyle[idx]).assertAll();
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
        new SpanTester(docBuilder, 5).addSpanLength(
            (idx) -> expectedLenghts[idx]
        ).addSpanStyle((idx) -> expectedStyles[idx]).assertAll();
    }

}
