package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import org.apache.pdfbox.pdmodel.font.*;

@RunWith(JUnit4.class)
public class PdfLineTest{

    private ArrayList<PdfText> test(PdfLine line, String text)
            throws IOException{
        return line.appendText(text, PDType1Font.TIMES_ROMAN, 12);
    }

    @Test
    public void BasicTest() throws IOException{
        PdfLine line = new PdfLine(5000, 1);
        ArrayList<PdfText> output = test(line, "Hello World! Testing Joey.");
        assertEquals(131.31f, line.getWidth(), 0.1);
        PdfTextTest.test(output);
    }

    @Test
    public void TwoLines() throws IOException{
        PdfLine line = new PdfLine(50, 1);
        ArrayList<PdfText> output = test(line, "Hello World! Testing Joey.");
        assertEquals(29.66f, line.getWidth(), 0.1);
        PdfTextTest.test(output, "World!", " ", "Testing", " ", "Joey.");
    }

    @Test
    public void spaceStart() throws IOException{
        PdfLine line = new PdfLine(5000, 1);
        ArrayList<PdfText> output = test(line, " Hello World! Testing Joey.");
        assertEquals(131.31f, line.getWidth(), 0.1);
        PdfTextTest.test(output);
    }

    //TODO more to come...
}