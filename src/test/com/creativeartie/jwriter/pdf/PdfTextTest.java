package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import org.apache.pdfbox.pdmodel.font.*;

@RunWith(JUnit4.class)
public class PdfTextTest{

    private static void test(String test, String ... expects)
            throws IOException{
        test(PdfText.createWords(test, PDType1Font.TIMES_ROMAN, 12), expects);
    }

    public static void test(ArrayList<PdfText> test, String ... expects){
        assertEquals(expects.length, test.size());
        int i = 0;
        for(PdfText text: test){
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