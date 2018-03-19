package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;

@RunWith(JUnit4.class)
public class PdfDataTest{
    private static final SizedFont baseFont = new SizedFont(PDType1Font
        .TIMES_ROMAN, 12);

    private static void test(String test, String ... expects)
            throws IOException{
        test(PdfData.createWords(test, baseFont), expects);
    }

    public static void test(List<PdfData> test, String ... expects){
        assertEquals(expects.length, test.size());
        int i = 0;
        for(PdfData text: test){
            assertEquals("Positional:" + i, expects[i], text.getText());
            i++;
        }
    }

    @Test
    public void basicText() throws IOException{
        test("Hello World!", "Hello", " ", "World!");
    }

    @Test
    public void spaceStart() throws IOException{
        test(" Hello World!", " ", "Hello", " ", "World!");
    }

    @Test
    public void spaceMiddle() throws IOException{
        test("Hello    World!", "Hello", " ", "World!");
    }

    @Test
    public void spaceEnd() throws IOException{
        test("Hello World!   ", "Hello", " ", "World!", " ");
    }
}