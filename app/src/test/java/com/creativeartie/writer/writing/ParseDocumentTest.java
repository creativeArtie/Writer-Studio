package com.creativeartie.writer.writing;

import java.util.regex.*;

import org.junit.jupiter.api.*;

@DisplayName("Document ID")
class ParseDocumentTest {

private static Pattern pattern =
Pattern.compile(ParseDocument.Phrases.TOKEN.format_syntax);

@Nested
@DisplayName("Allowed ID Matches")
static class MatchIdTest {

@Test
void testEnglish() {
    Matcher match = pattern.matcher("abc");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("abc", match.group());
}

@Test
void testChinese() {
    Matcher match = pattern.matcher("百科全書");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("百科全書", match.group());
}

@Test
void testJapanese() {
    Matcher match = pattern.matcher("あらゆる");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("あらゆる", match.group());
}

@Test
void testKorean() {
    Matcher match = pattern.matcher("한국어");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("한국어", match.group());
}

@Test
void testArabic() {
    Matcher match = pattern.matcher("العربية");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("العربية", match.group());
}

@Test
void testNumber() {
    Matcher match = pattern.matcher("123");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("123", match.group());
}

@Test
void testWords() {
    Matcher match = pattern.matcher("Hello World_fun");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("Hello World_fun", match.group());
}

@Test
void testSpaced() {
    Matcher match = pattern.matcher("  Hello  ");
    Assertions.assertTrue(match.find());
    Assertions.assertEquals("Hello", match.group());
}
}

@Test
void nonMatches() {
    Assertions.assertAll(()->Assertions.assertFalse(pattern.matcher("  ").find()),
        ()->Assertions.assertFalse(pattern.matcher("@#$%").find()));
}

}
