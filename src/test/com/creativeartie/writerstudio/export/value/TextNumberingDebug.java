package com.creativeartie.writerstudio.export.value;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.*;

@RunWith(Parameterized.class)
public class TextNumberingDebug {
    @Parameter
    public int input;
    @Parameter(value = 1)
    public String roman;
    @Parameter(value = 2)
    public String superscript;

    @Parameters
    public static Collection<Object[]> data() throws Exception{
        return Arrays.asList(new Object[][]{
            {1, "ⁱ", "¹"}, {2, "ⁱⁱ", "²"}, {3, "ⁱⁱⁱ", "³"}, {4, "ⁱᵛ", "⁴"},
            {5, "ᵛ", "⁵"}, {6, "ᵛⁱ", "⁶"}, {7, "ᵛⁱⁱ", "⁷"}, {8, "ᵛⁱⁱⁱ", "⁸"},
            {9, "ⁱˣ", "⁹"}, {10, "ˣ", "¹⁰"}, {11, "ˣⁱ", "¹¹"},
            {12, "ˣⁱⁱ", "¹²"}, {14, "ˣⁱᵛ", "¹⁴"}, {15, "ˣᵛ","¹⁵"},
            {18, "ˣᵛⁱⁱⁱ", "¹⁸"}, {19, "ˣⁱˣ", "¹⁹"}, {20, "ˣˣ", "²⁰"},
            {50, "ˡ","⁵⁰"}, {99, "ˣᶜⁱˣ", "⁹⁹"}, {100, "ᶜ", "¹⁰⁰"},
            {451, "ᶜᵈˡⁱ", "⁴⁵¹"}, {900, "ᶜᵐ", "⁹⁰⁰"}, {1000, "ᵐ", "¹⁰⁰⁰"},
            {5000, "ᵐᵐᵐᵐᵐ", "⁵⁰⁰⁰"}, {2111, "ᵐᵐᶜˣⁱ", "²¹¹¹"}
        });
    }

    @Test
    public void testRoman(){
        assertEquals(roman, Utilities.toRomanSuperscript(input));
    }

    @Test
    public void testSuperscript(){
        assertEquals(superscript, Utilities.toNumberSuperscript(input));
    }


}
