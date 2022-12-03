package com.creativeartie.writer.writing;

import org.junit.jupiter.api.*;

class BasicTextTest {

    private String[] basicEscape = { SpanStyles.ESCAPE.getStyle() };
    private String[] basicText = { SpanStyles.TEXT.getStyle() };
    private String[] formatedText =
        { SpanStyles.BOLD.getStyle(), SpanStyles.TEXT.getStyle() };
    private String[] formatedEscapte =
        { SpanStyles.BOLD.getStyle(), SpanStyles.ESCAPE.getStyle() };

    private DocBuilder builder;

    @BeforeEach
    void setUp() throws Exception {
        builder = new DocBuilder(true);
    }

    @Test
    void testAllowed() {
        String raw = "abc\\3dsa123";
        BasicText test = new BasicText(raw, builder, "a-zA-Z", true);
        int expectedLegnths[] = { 3, 2, 3 };
        String expectedStyles[][] = { basicText, basicEscape, basicText };
        new SpanStyleTester(builder, 3)
            .addSpanLength((idx) -> expectedLegnths[idx])
            .addSpanStyle((idx) -> expectedStyles[idx]).assertStyles();
        new SpanTester().addAtomicTest("abc").addEscapeText('3')
            .addAtomicTest("dsa").test(test);
    }

    @Test
    void testAvoid() {
        String raw = "abc\\+dsa+";
        System.out.println(raw);
        BasicText test = new BasicText(raw, builder, "\\+", false);
        int expectedLegnths[] = { 3, 2, 3 };
        String expectedStyles[][] = { basicText, basicEscape, basicText };
        new SpanStyleTester(builder, 3)
            .addSpanLength((idx) -> expectedLegnths[idx])
            .addSpanStyle((idx) -> expectedStyles[idx]).assertStyles();
        new SpanTester().addAtomicTest("abc").addEscapeText('+')
            .addAtomicTest("dsa").test(test);

    }

}
