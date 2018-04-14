package com.creativeartie.writerstudio.export.value;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.*;

@RunWith(Parameterized.class)
public class NumberRoundingDebug {
    @Parameter
    public int input;
    @Parameter(value = 1)
    public String scaling;
    @Parameter(value = 2)
    public String expect100;
    @Parameter(value = 3)
    public String expect1000;

    @Parameters
    public static Collection<Object[]> data() throws Exception{
        return Arrays.asList(new Object[][]{
            {1, "1", "< 1,000", "< 10,000"},
            {220, "200", "< 1,000", "< 10,000"},
            {499, "500", "< 1,000", "< 10,000"},
            {500, "500", "1,000", "< 10,000"},

            {1001, "1,000", "1,000", "< 10,000"},
            {1499, "1,000", "1,000", "< 10,000"},
            {1500, "2,000", "2,000", "< 10,000"},
            {1923, "2,000", "2,000", "< 10,000"},

            {233344, "200,000", "233,000", "230,000"},
            {255531, "300,000", "256,000", "260,000"}
        });
    }

    @Test
    public void testScaling(){
        assertEquals(scaling, Utilities.round(input));
    }

    @Test
    public void test100(){
        assertEquals(expect100, Utilities.round(input, 3));
    }

    @Test
    public void test1000(){
        assertEquals(expect1000, Utilities.round(input, 4));
    }
}