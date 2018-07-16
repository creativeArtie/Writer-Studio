package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import com.creativeartie.writerstudio.lang.*;


/// @see SupplementSectionDebug
@RunWith(JUnit4.class)
public class BasicCanParseDebug {
    private static final List<String> endList = Arrays.asList("+!", "-");
    private static final String ender = "}";

    @Test
    public void sectionSingleEnd(){
        String text = "abc\n";
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
    }

    @Test
    public void sectionNoEnd(){
        String text = "abc";
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
        assertFalse(AuxiliaryChecker.checkSectionEnd(text, false));
    }

    @Test
    public void sectionDoubleEnd(){
        String text = "abc\n\n";
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
    }

    @Test
    public void sectionDoubleLine(){
        String text = "abc\ndeed\n";
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
    }

    @Test
    public void sectionDoubleWithEscape(){
        String text = "abc\\\n\n";
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, false));
    }

    @Test
    public void sectionEscapedEnd(){
        String text = "abc\\\n";
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
        assertFalse(AuxiliaryChecker.checkSectionEnd(text, false));
    }

    @Test
    public void sectionEscapedNothing(){
        String text = "abc\\";
        assertTrue(AuxiliaryChecker.checkSectionEnd(text, true));
        assertFalse(AuxiliaryChecker.checkSectionEnd(text, false));
    }

    @Test
    public void simplePass(){
        String test = "abc";
        assertTrue(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void noSecondCharPass(){
        String test = "ab+c";
        assertTrue(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void escapedPass(){
        String test = "ab\\-c";
        assertTrue(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void escapeNewLinePass(){
        String test = "ab\\\nc";
        assertTrue(AuxiliaryChecker.notCutoff(test, endList));
    }


    @Test
    public void escapeNewLineFail(){
        String test = "ab\nc";
        assertFalse(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void midOneCharFail(){
        String test = "ab-c";
        assertFalse(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void midTwoCharFail(){
        String test = "ab+!c";
        assertFalse(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void escapeCutshortFail(){
        String test = "abc\\";
        assertFalse(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void escapedFirstFail(){
        String test = "a\\-ab-dd";
        assertFalse(AuxiliaryChecker.notCutoff(test, endList));
    }

    @Test
    public void endWithEnderPass(){
        String test = "aded}";
        assertTrue(AuxiliaryChecker.willEndWith(test, ender));
    }

    @Test
    public void endWithEscapedPass(){
        String test = "aded\\}\\-abc}";
        assertTrue(AuxiliaryChecker.willEndWith(test, ender));
    }

    @Test
    public void withEnderEarlyFail(){
        String test = "aded}Hloo";
        assertFalse(AuxiliaryChecker.willEndWith(test, ender));
    }

    @Test
    public void noEndWithFail(){
        String test = "aded";
        assertFalse(AuxiliaryChecker.willEndWith(test, ender));
    }

    @Test
    public void endWithEscapedFail(){
        String test = "aded\\}";
        assertFalse(AuxiliaryChecker.willEndWith(test, ender));
    }

    @Test
    public void endWithNewLineFail(){
        String test = "aded\n";
        assertFalse(AuxiliaryChecker.willEndWith(test, ender));
    }

    @Test
    public void lineEndedPass(){
        String text = "abc\n";
        assertTrue(AuxiliaryChecker.checkLineEnd(text, true));
    }

    @Test
    public void noLineEndPass(){
        String text = "abc";
        assertTrue(AuxiliaryChecker.checkLineEnd(text, true));
    }

    @Test
    public void midDocPass(){
        String text = "abc\n";
        assertTrue(AuxiliaryChecker.checkLineEnd(text, false));
    }

    @Test
    public void midDocFail(){
        String text = "abc\nabc";
        assertFalse(AuxiliaryChecker.checkLineEnd(text, false));
    }
}