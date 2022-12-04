package com.creativeartie.writer.writing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class BasicTextTest {

    public class BasicTextDebugger extends SpanDebugger<BasicText> {
        private String expectedText;

        public BasicTextDebugger(String text) {
            super(BasicText.class);
            expectedText = text;
        }

        @Override
        Executable[] testSelf(BasicText actual) {
            Executable[] list = {
                () -> Assertions.assertEquals(expectedText, actual.getText()) };
            return list;
        }

    }

    private class EscapeDebugger extends SpanDebugger<BasicText.EscapeText> {

        private String expectEscaped;

        public EscapeDebugger(String escaped) {
            super(BasicText.EscapeText.class);
            expectEscaped = escaped + "";
        }

        @Override
        Executable[] testSelf(BasicText.EscapeText actual) {
            Executable[] list = { () -> Assertions
                .assertEquals(expectEscaped, actual.getText()) };
            return list;
        }

    }

    private class AtomicDebugger extends SpanDebugger<BasicText.AtomicText> {

        private String expectText;

        public AtomicDebugger(String text) {
            super(BasicText.AtomicText.class);
            expectText = text;
        }

        @Override
        Executable[] testSelf(BasicText.AtomicText actual) {
            Executable[] list =
                { () -> Assertions.assertEquals(expectText, actual.getText()) };
            return list;
        }

    }

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
    void testAllowedBasic() {
        String raw = "abc\\3dsa123";

        BasicTextDebugger debugger = new BasicTextDebugger("abc3dsa");
        debugger.addStyle(3, basicText);
        debugger.addStyle(2, basicEscape);
        debugger.addStyle(3, basicText);
        debugger.addChild(new AtomicDebugger("abc"));
        debugger.addChild(new EscapeDebugger("3"));
        debugger.addChild(new AtomicDebugger("dsa"));
        debugger.test(builder, new BasicText(raw, builder, "a-zA-Z", true));
    }

    @Test
    void testAvoidBasic() {
        String raw = "abc\\+dsa+";
        BasicTextDebugger debugger = new BasicTextDebugger("abc+dsa");
        debugger.addStyle(3, basicText);
        debugger.addStyle(2, basicEscape);
        debugger.addStyle(3, basicText);
        debugger.addChild(new AtomicDebugger("abc"));
        debugger.addChild(new EscapeDebugger("+"));
        debugger.addChild(new AtomicDebugger("dsa"));
        debugger.test(builder, new BasicText(raw, builder, "\\+", false));
    }

    @Test
    void testAllowedFormmated() {
        String raw = "abc\\3dsa123";

        BasicTextDebugger debugger = new BasicTextDebugger("abc3dsa");
        debugger.addStyle(3, formatedText);
        debugger.addStyle(2, formatedEscapte);
        debugger.addStyle(3, formatedText);
        debugger.addChild(new AtomicDebugger("abc"));
        debugger.addChild(new EscapeDebugger("3"));
        debugger.addChild(new AtomicDebugger("dsa"));
        debugger.test(
            builder,
            new BasicText(raw, builder, "a-zA-Z", true, SpanStyles.BOLD)
        );
    }

    @Test
    void testAvoidFormatted() {
        String raw = "abc\\+dsa+";
        BasicTextDebugger debugger = new BasicTextDebugger("abc+dsa");
        debugger.addStyle(3, formatedText);
        debugger.addStyle(2, formatedEscapte);
        debugger.addStyle(3, formatedText);
        debugger.addChild(new AtomicDebugger("abc"));
        debugger.addChild(new EscapeDebugger("+"));
        debugger.addChild(new AtomicDebugger("dsa"));
        debugger.test(
            builder, new BasicText(raw, builder, "\\+", false, SpanStyles.BOLD)
        );
    }

}
