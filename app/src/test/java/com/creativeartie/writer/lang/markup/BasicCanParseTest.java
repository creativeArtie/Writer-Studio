package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuxiliaryChecker Checkers")
public class BasicCanParseTest {
    private static final List<String> endList = Arrays.asList("+!", "-");
    private static final String ender = "}";

    @Nested
    @DisplayName("Section End Checker")
    class SectionEnd{
        @Test
        @DisplayName("Text + '\\n'")
        public void sectionSingleEnd(){
            String text = "abc\n";
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
        }

        @Test
        @DisplayName("Text")
        public void sectionNoEnd(){
            String text = "abc";
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
            assertFalse(AuxiliaryChecker.checkSectionEnd(text, false));
        }

        @Test
        @DisplayName("Text + '\\n' + '\\n'")
        public void sectionDoubleEnd(){
            String text = "abc\n\n";
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
        }

        @Test
        @DisplayName("Text + '\\n' + Text + '\\n'")
        public void sectionDoubleLine(){
            String text = "abc\ndeed\n";
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
        }

        @Test
        @DisplayName("Text + Escape '\\n' + '\\n'")
        public void sectionDoubleWithEscape(){
            String text = "abc\\\n\n";
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
        }

        @Test
        @DisplayName("Text + Escaped '\\n'")
        public void sectionEscapedEnd(){
            String text = "abc\\\n";
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
            assertFalse(AuxiliaryChecker.checkSectionEnd(text, false));
        }

        @Test
        @DisplayName("Text + '\\0'")
        public void sectionEscapedNothing(){
            String text = "abc\\";
            assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
            assertFalse(AuxiliaryChecker.checkSectionEnd(text, false));
        }
    }

    @Nested
    @DisplayName("Token (notCutOff) Checker")
    class Token{
        @Test
        @DisplayName("Text")
        public void tokenSimple(){
            String test = "abc";
            assertTrue(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + Ender + Text")
        public void tokenPartialEnder(){
            String test = "ab+c";
            assertTrue(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + Escaped Ender + Text")
        public void tokenEscapeEnder(){
            String test = "ab\\-c";
            assertTrue(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + Escaped '\\n' + Text")
        public void tokenEscapeLineEnd(){
            String test = "ab\\\nc";
            assertTrue(AuxiliaryChecker.notCutoff(test, endList));
        }


        @Test
        @DisplayName("Text + '\\n' + Text")
        public void tokenLineEnd(){
            String test = "ab\nc";
            assertFalse(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + 1-Char Ender + Text")
        public void tokenSingleEnder(){
            String test = "ab-c";
            assertFalse(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + 2-Char Ender + Text")
        public void tokenDoubleEnder(){
            String test = "ab+!c";
            assertFalse(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + '\\'")
        public void tokenEscapeNull(){
            String test = "abc\\";
            assertFalse(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + ...")
        public void tokenComplexEnder(){
            String test = "a\\-ab-dd";
            assertFalse(AuxiliaryChecker.notCutoff(test, endList));
        }

        @Test
        @DisplayName("Text + Ender")
        public void tokenWithEnder(){
            String test = "ab-";
            assertFalse(AuxiliaryChecker.notCutoff(test, endList));
        }
    }

    @Nested
    @DisplayName("Span (willEndWith) Checker")
    class Span{

        @Test
        @DisplayName("Text + Ending")
        public void spanEnderPass(){
            String test = "aded}";
            assertTrue(AuxiliaryChecker.willEndWith(test, ender));
        }

        @Test
        @DisplayName("Text + ...")
        public void spanEnderEscapedPass(){
            String test = "aded\\}\\-abc}";
            assertTrue(AuxiliaryChecker.willEndWith(test, ender));
        }

        @Test
        @DisplayName("Text + Ender + Text")
        public void spanEnderFail(){
            String test = "aded}Hloo";
            assertFalse(AuxiliaryChecker.willEndWith(test, ender));
        }

        @Test
        @DisplayName("Text")
        public void spanNoEnder(){
            String test = "aded";
            assertFalse(AuxiliaryChecker.willEndWith(test, ender));
        }

        @Test
        @DisplayName("Text + Escaped Ending")
        public void spanEscapeEnder(){
            String test = "aded\\}";
            assertFalse(AuxiliaryChecker.willEndWith(test, ender));
        }

        @Test
        @DisplayName("Text + '\\n'")
        public void spanLineEnd(){
            String test = "aded\n";
            assertFalse(AuxiliaryChecker.willEndWith(test, ender));
        }
    }

    @Nested
    @DisplayName("Last Line (checkLineEnd) Checker")
    class LastLine{
        @Test
        @DisplayName("Text + '\\n'")
        public void lastLineEnded(){
            String text = "abc\n";
            assertTrue(AuxiliaryChecker.checkLineEnd(text, true));
        }

        @Test
        @DisplayName("Text")
        public void lastLineNoEnd(){
            String text = "abc";
            assertTrue(AuxiliaryChecker.checkLineEnd(text, true));
        }

        @Test
        @DisplayName("Last Line + Early '\\n'")
        public void lineEndEarly(){
            String text = "abc\nabc";
            assertFalse(AuxiliaryChecker.checkLineEnd(text, true));
        }
    }

    @Nested
    @DisplayName("Mid Line (checkLineEnd) Checker")
    class MidLine{
        @Test
        @DisplayName("Text + '\\n'")
        public void midLineEnd(){
            String text = "abc\n";
            assertTrue(AuxiliaryChecker.checkLineEnd(text, false));
        }

        @Test
        @DisplayName("Text")
        public void midLineNoEnd(){
            String text = "abc";
            assertFalse(AuxiliaryChecker.checkLineEnd(text, false));
        }

        @Test
        @DisplayName("Text + '\\n' + Text")
        public void midLineEndEarly(){
            String text = "abc\nabc";
            assertFalse(AuxiliaryChecker.checkLineEnd(text, false));
        }
    }
}
