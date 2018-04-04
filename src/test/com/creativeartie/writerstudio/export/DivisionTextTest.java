package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.export.value.*;

@RunWith(JUnit4.class)
public class DivisionTextTest{

    private void append(DivisionText item, String text) throws IOException{
        item.appendText(text, new DefaultFont());
    }

    @Test
    public void BasicTest() throws IOException{
        DivisionText item = new DivisionText(5000).setLeading(1);
        append(item, "Hello World! Testing Joey.");
        assertEquals(131.31f, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ", "World!", " ",
            "Testing", " ", "Joey.");
    }

    @Test
    public void spaceStart() throws IOException{
        DivisionText item = new DivisionText(5000).setLeading(1);
        append(item, " Hello World! Testing Joey.");
        assertEquals(131.31f, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ", "World!", " ",
            "Testing", " ", "Joey.");
    }

    @Test
    public void spaceEnd() throws IOException{
        DivisionText item = new DivisionText(5000).setLeading(1);
        append(item, "Hello World! Testing Joey. ");
        assertEquals(134.31f, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ", "World!", " ",
            "Testing", " ", "Joey.", " ");
    }

    @Test
    public void TewoLines() throws IOException{
        DivisionText item = new DivisionText(70).setLeading(1);
        append(item, "Hello World! Testing Joey.");

        assertEquals(67.22f, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ", "World!", " ");

        assertEquals(63.99f, item.get(1).getWidth(), 0.1);
        ContentTextTest.test(item.get(1), "Testing", " ", "Joey.");
    }

    @Test
    public void WordPreLine() throws IOException{
        DivisionText item = new DivisionText(50).setLeading(1);
        append(item, "Hello World! Testing Joey.");

        assertEquals(29.66f, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ");

        assertEquals(37.65f, item.get(1).getWidth(), 0.1);
        ContentTextTest.test(item.get(1), "World!", " ");

        assertEquals(39.00f, item.get(2).getWidth(), 0.1);
        ContentTextTest.test(item.get(2), "Testing", " ");

        assertEquals(24.99f, item.get(3).getWidth(), 0.1);
        ContentTextTest.test(item.get(3), "Joey.");
    }

    //TODO more to come...

    @Test
    public void spaceAppend() throws IOException{
        DivisionText item = new DivisionText(70).setLeading(1);
        append(item, "Hello ");
        append(item, "World!");

        assertEquals(64.31, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ", "World!");
    }

    @Test
    public void textAppend() throws IOException{
        DivisionText item = new DivisionText(70).setLeading(1);
        append(item, "Hello Wor");
        append(item, "ld!");

        assertEquals(64.31, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ", "Wor", "ld!");
    }

    @Test
    public void wordTooLong() throws IOException{
        DivisionText item = new DivisionText(30).setLeading(1);
        append(item, "Hello ");
        append(item, "World!");

        assertEquals(29.66, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ");

        assertEquals(34.65, item.get(1).getWidth(), 0.1);
        ContentTextTest.test(item.get(1), "World!");
    }

    @Test
    public void appendFromSpace() throws IOException{
        DivisionText item = new DivisionText(35).setLeading(1);
        append(item, "Hello ");
        append(item, "World!");

        assertEquals(29.66, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ");

        assertEquals(34.65, item.get(1).getWidth(), 0.1);
        ContentTextTest.test(item.get(1), "World!");
    }

    @Test
    public void appendToWord() throws IOException{
        DivisionText item = new DivisionText(35).setLeading(1);
        append(item, "Hello Wor");
        append(item, "ld!");

        assertEquals(29.66, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello", " ");

        assertEquals(34.65, item.get(1).getWidth(), 0.1);
        ContentTextTest.test(item.get(1), "Wor", "ld!");
    }

    @Test
    public void longText() throws IOException{
        DivisionText item = new DivisionText(12).setLeading(1);
        append(item, "Hello");

        assertEquals(26.66, item.get(0).getWidth(), 0.1);
        ContentTextTest.test(item.get(0), "Hello");
    }
}