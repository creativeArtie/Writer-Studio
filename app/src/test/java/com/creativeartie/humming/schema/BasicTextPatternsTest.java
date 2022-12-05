package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class BasicTextPatternsTest {

    @BeforeEach
    void setUp() throws Exception {
    }

    @ParameterizedTest
    @ValueSource(
        strings = { "abc", "百科全書", "あらゆる", "한국어", "العربية", "123",
            "Hello World Fun", "Hello" }
    )
    void testBasicId(String raw) {
        Matcher matcher = BasicTextPatterns.ID.match(raw);
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(raw, BasicTextPart.TEXT.match(matcher));
    }

    @Test
    void testEscapedId() {
        Matcher matcher = BasicTextPatterns.ID.match("avdd\\-ade");
        Assertions.assertTrue(matcher.find(), "group 1");
        Assertions
            .assertEquals("avdd", BasicTextPart.TEXT.match(matcher), "group 1");

        Assertions.assertTrue(matcher.find(), "group 2");
        Assertions.assertEquals(
            "\\-", BasicTextPart.ESCAPE.match(matcher), "group 2"
        );

        Assertions.assertTrue(matcher.find(), "group 3");
        Assertions
            .assertEquals("ade", BasicTextPart.TEXT.match(matcher), "group 3");
    }

}
