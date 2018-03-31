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
            {1, "i", "¹"}, {2, "ii", "²"}, {3, "iii", "³"}, {4, "iv", "⁴"},
            {5, "v", "⁵"}, {6, "vi", "⁶"}, {7, "vii", "⁷"}, {8, "viii", "⁸"},
            {9, "ix", "⁹"}, {10, "x", "¹⁰"}, {11, "xi", "¹¹"},
            {12, "xii", "¹²"}, {14, "xiv", "¹⁴"}, {15, "xv","¹⁵"},
            {18, "xviii", "¹⁸"}, {19, "xix", "¹⁹"}, {20, "xx", "²⁰"},
            {50, "l","⁵⁰"}, {99, "xcix", "⁹⁹"}, {100, "c", "¹⁰⁰"},
            {451, "cdli", "⁴⁵¹"}, {900, "cm", "⁹⁰⁰"}, {1000, "m", "¹⁰⁰⁰"},
            {5000, "mmmmm", "⁵⁰⁰⁰"}, {2111, "mmcxi", "²¹¹¹"}
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