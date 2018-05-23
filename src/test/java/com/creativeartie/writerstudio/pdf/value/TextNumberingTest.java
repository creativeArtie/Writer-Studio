package com.creativeartie.writerstudio.pdf.value;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Text Superscript Numbering")
public class TextNumberingTest{

    @DisplayName("Roman Numerals Superscript")
    @ParameterizedTest(name = "Roman Numerals Superscript: {0}")
    @CsvSource({
        "'ⁱ',     1",   "'ⁱⁱ', 2",     "'ⁱⁱⁱ', 3", "'ⁱᵛ',   4",
        "'ᵛ',     5",   "'ᵛⁱ', 6",     "'ᵛⁱⁱ', 7", "'ᵛⁱⁱⁱ', 8",
        "'ⁱˣ',    9",   "'ˣ', 10",     "'ˣⁱ', 11",
        "'ˣⁱⁱ',   12",   "'ˣⁱᵛ', 14",  "'ˣᵛ', 15",
        "'ˣᵛⁱⁱⁱ', 18",   "'ˣⁱˣ', 19",  "'ˣˣ', 20",
        "'ˡ',     50",   "'ˣᶜⁱˣ', 99", "'ᶜ', 100",
        "'ᶜᵈˡⁱ',  451",  "'ᶜᵐ', 900",  "'ᵐ', 1000",
        "'ᵐᵐᶜˣⁱ', 2111", "'ᵐᵐᵐᵐᵐ', 5000"
    })
    public void testRoman(String roman, int input){
        assertEquals(roman, Utilities.toRomanSuperscript(input));
    }

    @DisplayName("Arabic Numerals Superscript")
    @ParameterizedTest(name = "Arabic Numerals Superscript: {0}")
    @CsvSource({
        "'¹', 1",     "'²', 2",       "'³', 3",  "'⁴', 4",
        "'⁵', 5",     "'⁶', 6",       "'⁷', 7",  " '⁸', 8",
        "'⁹', 9",     "'¹⁰', 10",     "'¹¹', 11",
        "'⁴⁵¹', 451", "'⁵⁰⁰⁰', 5000", "'²¹¹¹', 2111"
    })
    public void testSuperscript(String superscript, int input){
        assertEquals(superscript, Utilities.toNumberSuperscript(input));
    }


}
