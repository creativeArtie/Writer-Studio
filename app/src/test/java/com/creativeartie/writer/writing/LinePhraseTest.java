package com.creativeartie.writer.writing;

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
        SpanStyles font = SpanStyles.valueOf(type);
        String textStyle[] = { SpanStyles.TEXT.getStyle() };
        String formatStyle[] = { font.getStyle(), SpanStyles.TEXT.getStyle() };
        String optStyle[] = { SpanStyles.OPERATOR.getStyle() };
        String rawText[] = { "Start", format, "text", format, "end" };
        String text = Joiner.on("").join(rawText);

        String[][] expectedStyle = { textStyle, optStyle, formatStyle, optStyle,
            textStyle };
        int[] expectedLengths = { 5, 1, 4, 1, 3 };

        LinePhrase test = createTest(text);

        ChildSpanTester childrenTester = new ChildSpanTester();
        childrenTester.addTextSpan("Start", false, false, false);
        switch (font) {
        case BOLD:
            childrenTester.addTextSpan("text", true, false, false);
            break;
        case ITALICS:
            childrenTester.addTextSpan("text", false, true, false);
            break;
        case UNDERLINE:
            childrenTester.addTextSpan("text", false, false, true);
            break;
        default:
            break;
        }
        childrenTester.addTextSpan("end", false, false, false);
        childrenTester.test(test);

        new SpanStyleTester(docBuilder, 5).addSpanLength(
            (idx) -> expectedLengths[idx]
        ).addSpanStyle((idx) -> expectedStyle[idx]).assertStyles();
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
        //                  123  123456789 123456
        String inputText = "abc{!todo text} other";
        // @formatter:on
        String[][] expectedStyles = { basic, todoOp, todoText, todoOp, basic };
        int[] expectedLenghts = { 3, 2, 9, 1, 6 };

        LinePhrase test = createTest(inputText);
        new ChildSpanTester().addTextSpan("abc", false, false, false)
            .addTodoSpan("todo text").addTextSpan(" other", false, false, false)
            .test(test);

        new SpanStyleTester(docBuilder, 5).addSpanLength(
            (idx) -> expectedLenghts[idx]
        ).addSpanStyle((idx) -> expectedStyles[idx]).assertStyles();
    }

}
