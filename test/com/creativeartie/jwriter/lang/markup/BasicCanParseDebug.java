package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import com.creativeartie.jwriter.lang.*;


/// @see SupplementSectionDebug
@RunWith(JUnit4.class)
public class BasicCanParseDebug {
    private static final List<String> endList = Arrays.asList("+!", "-");
    private static final String ender = "}";

    @Test
    public void simplePass(){
        String test = "abc";
        assertTrue(BasicParseText.canParse(test, endList));
    }

    @Test
    public void noSecondCharPass(){
        String test = "ab+c";
        assertTrue(BasicParseText.canParse(test, endList));
    }

    @Test
    public void escapedPass(){
        String test = "ab\\-c";
        assertTrue(BasicParseText.canParse(test, endList));
    }

    @Test
    public void escapeNewLinePass(){
        String test = "ab\\\nc";
        assertTrue(BasicParseText.canParse(test, endList));
    }


    @Test
    public void escapeNewLineFail(){
        String test = "ab\nc";
        assertFalse(BasicParseText.canParse(test, endList));
    }

    @Test
    public void midOneCharFail(){
        String test = "ab-c";
        assertFalse(BasicParseText.canParse(test, endList));
    }

    @Test
    public void midTwoCharFail(){
        String test = "ab+!c";
        assertFalse(BasicParseText.canParse(test, endList));
    }

    @Test
    public void escapeCutshortFail(){
        String test = "abc\\";
        assertFalse(BasicParseText.canParse(test, endList));
    }

    @Test
    public void escapedFirstFail(){
        String test = "a\\-ab-dd";
        assertFalse(BasicParseText.canParse(test, endList));
    }

    @Test
    public void endWithEnderPass(){
        String test = "aded}";
        assertTrue(BasicParseText.willEndWith(test, ender, endList));
    }

    @Test
    public void endWithEscapedPass(){
        String test = "aded\\}\\-abc}";
        assertTrue(BasicParseText.willEndWith(test, ender, endList));
    }

    @Test
    public void withEnderEarlyFail(){
        String test = "aded}Hloo";
        assertFalse(BasicParseText.willEndWith(test, ender, endList));
    }

    @Test
    public void noEndWithFail(){
        String test = "aded";
        assertFalse(BasicParseText.willEndWith(test, ender, endList));
    }

    @Test
    public void endWithEscapedFail(){
        String test = "aded\\}";
        assertFalse(BasicParseText.willEndWith(test, ender, endList));
    }

    @Test
    public void endWithCharFail(){
        String test = "aded-";
        assertFalse(BasicParseText.willEndWith(test, ender, endList));
    }

    @Test
    public void endWithNewLineFail(){
        String test = "aded\n";
        assertFalse(BasicParseText.willEndWith(test, ender, endList));
    }
}