package com.creativeartie.writerstudio.pdf.value;

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
    public String expect;

    @Parameters
    public static Collection<Object[]> data() throws Exception{
        return Arrays.asList(new Object[][]{
            {1, "i"}, {2, "ii"}, {3, "iii"}, {4, "iv"}, {5, "v"},
            {6, "vi"}, {7, "vii"}, {8, "viii"}, {9, "ix"}, {10, "x"},
            {11, "xi"}, {12, "xii"}, {14, "xiv"}, {15, "xv"},
            {18, "xviii"}, {19, "xix"}, {20, "xx"},
            {50, "l"}, {99, "xcix"}, {100, "c"}, {451, "cdli"}, {900, "cm"},
            {1000, "m"}, {5000, "mmmmm"}, {2111, "mmcxi"}
        });
    }

    @Test
    public void test(){
        assertEquals(expect, TextNumbering.toRomanLower(input));
    }
}