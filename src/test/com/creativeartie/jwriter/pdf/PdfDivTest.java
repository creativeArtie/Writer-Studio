package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import org.apache.pdfbox.pdmodel.font.*;

@RunWith(JUnit4.class)
public class PdfDivTest{

    private ArrayList<PdfData> test(PdfDiv line, String text)
            throws IOException{
        return line.appendText(text, PDType1Font.TIMES_ROMAN, 12);
    }

    @Test
    public void BasicTest() throws IOException{
        PdfDiv line = new PdfDiv(5000, 1);
        ArrayList<PdfData> output = test(line, "Hello World! Testing Joey.");
        assertEquals(131.31f, line.getWidth(), 0.1);
        PdfDataTest.test(output);
    }

    @Test
    public void TwoLines() throws IOException{
        PdfDiv line = new PdfDiv(50, 1);
        ArrayList<PdfData> output = test(line, "Hello World! Testing Joey.");
        assertEquals(29.66f, line.getWidth(), 0.1);
        PdfDataTest.test(output, "World!", " ", "Testing", " ", "Joey.");
    }

    @Test
    public void spaceStart() throws IOException{
        PdfDiv line = new PdfDiv(5000, 1);
        ArrayList<PdfData> output = test(line, " Hello World! Testing Joey.");
        assertEquals(131.31f, line.getWidth(), 0.1);
        PdfDataTest.test(output);
    }

    //TODO more to come...
}