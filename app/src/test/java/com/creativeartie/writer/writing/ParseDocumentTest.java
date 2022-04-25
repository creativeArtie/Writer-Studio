package com.creativeartie.writer.writing;

import java.lang.reflect.*;
import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@DisplayName("Document ID")
class ParseDocumentTest {

    private static Pattern idText;

    @BeforeAll
    static void createPatterns() throws Exception {
        Field word = ParseDocument.class.getDeclaredField("ID_TEXT");
        word.setAccessible(true);
        idText = Pattern.compile((String) word.get(new String()));
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123",
        "Hello World_fun", "  Hello  " })
    void matchIdTextTest(String value) {
        Matcher match = idText.matcher(value);
        Assertions.assertTrue(match.find());
        Assertions.assertEquals(value.trim(), match.group());
    }

    @ParameterizedTest
    @ValueSource(strings = { "   ", "@#$%" })
    void noIdTextMatches(String value) {
        Assertions.assertFalse(idText.matcher(value).find());
    }

    @ParameterizedTest
    @ValueSource(strings = { "abc-231", "  cat-id  ", "   adf dsf - ad- dfd" })
    void matchIdTest(String value) {
        Pattern idPhrase =
            Pattern.compile(ParseDocument.Phrases.ID.format_syntax);
        Matcher match = idPhrase.matcher(value);
        Assertions.assertTrue(match.find());
        Assertions.assertEquals(value.trim(), match.group());
    }

    @ParameterizedTest
    @CsvSource({ "{^ade},1,true", "{^123},1,true", "{^end,1,true",
        "{$dfas},1,false" })
    void matchFootnote(String value, int enumIdx, boolean pass) {
        ParseDocument.Phrases use = ParseDocument.Phrases.values()[enumIdx];
        Pattern footnote = Pattern.compile(use.format_syntax);
        Matcher match = footnote.matcher(value);

        if (pass) {
            Assertions.assertTrue(match.find());

            int index = value.lastIndexOf('}');
            String expect =
                value.substring(2, index == -1 ? value.length() : index);
            Assertions.assertAll(
                () -> Assertions.assertEquals(expect, match.group("ID")),
                () -> Assertions.assertEquals(value, match.group(use.name()))
            );
        } else {
            Assertions.assertFalse(match.find());
        }
    }
}
