package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import org.apache.pdfbox.pdmodel.font.*;

@RunWith(JUnit4.class)
public class PdfItemTest{

    private void append(PdfItem item, String text) throws IOException{
        item.appendText(text, PDType1Font.TIMES_ROMAN, 12);
    }

    @Test
    public void BasicTest() throws IOException{
        PdfItem item = new PdfItem(5000).setLeading(1);
        append(item, "Hello World! Testing Joey.");
        assertEquals(131.31f, item.get(0).getWidth(), 0.1);
        PdfDataTest.test(item.get(0), "Hello", " ", "World!", " ",
            "Testing", " ", "Joey.");
    }

    @Test
    public void TwoLines() throws IOException{
        PdfItem item = new PdfItem(50).setLeading(1);
        append(item, "Hello World! Testing Joey.");
        assertEquals(29.66f, item.get(0).getWidth(), 0.1);
        PdfDataTest.test(item.get(0), "Hello", " ");
        //PdfDataTest.test(item.get(0), "World!", " ", "Testing", " ", "Joey.");
    }

    @Test
    public void spaceStart() throws IOException{
        PdfItem item = new PdfItem(5000).setLeading(1);
        append(item, " Hello World! Testing Joey.");
        assertEquals(131.31f, item.get(0).getWidth(), 0.1);
        PdfDataTest.test(item.get(0), "Hello", " ", "World!", " ",
            "Testing", " ", "Joey.");
    }

    //TODO more to come...
}