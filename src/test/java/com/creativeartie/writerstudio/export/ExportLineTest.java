package com.creativeartie.writerstudio.export;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.time.*;
import java.util.*;

import com.creativeartie.writerstudio.export.mock.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExportLineTest{

    private void assertData(String text, int height,
        ExportLineMain<Integer> parent, int line, int span
    ){
        String start = "line " + line + " span " + span;
        ExportData<Integer> test = parent.get(line).get(span);
        assertAll(
            () -> assertEquals(text, test.getCurrentText(), start + " text"),
            () -> assertEquals(height, test.getFillHeight().intValue(),
                start + " height"),
            () -> assertEquals(text.length(), test.getFillWidth().intValue(),
                start + " width")
        );
    }

    @Test@Disabled
    public void fillAll(){
        MockContentLine content = new MockContentLine("a", "b c");
        MockRenderLine render = new MockRenderLine(5, 10);
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(1, test.size(), "lines size");
        assertEquals(2, test.get(0).size(), "line 1 size");
        assertData("a",   10, test, 0, 0);
        assertData("b c", 10, test, 0, 1);
    }

    @ParameterizedTest()@Disabled
    @ValueSource(strings = {"123 ;overflow", "123 overflow"})
    public void appendToNewline(String text){
        MockContentLine content = new MockContentLine(text.split(";"));
        MockRenderLine render = new MockRenderLine(5, 10);
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(2, test.size(), "lines size");
        assertEquals(1, test.get(0).size(), "line 1 size");
        assertEquals(1, test.get(1).size(), "line 2 size");
        assertData("123 ",    10, test, 0, 0);
        assertData("overflow", 10, test, 1, 0);
    }

    @Test@Disabled
    public void emptyText(){
        MockContentLine content = new MockContentLine();
        MockRenderLine render = new MockRenderLine();
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(0, test.size());
    }

    @Test
    public void overflowMany(){
        String[] lines = new String[]{
        /// -01234567890--012345678901234567890
            "If you ",  "fail, just remember ",
        /// -012345678901234567890--012345678901234567890
            "you still have the ", "rest of your life to ", "try."
        };
        String full = String.join("", lines);
        MockContentLine content = new MockContentLine(full);
        MockRenderLine render = new MockRenderLine(10, 20);
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(5, test.size(), "line size");
        for (int i = 0; i < 5; i++){
            assertEquals(1, test.get(i).size(), "line " + i + " size");
            assertData(lines[i], 10, test, i, 0);
        }
    }

    @Test
    public void splitMany(){
        MockContentLine content = new MockContentLine(
            "Row, ", "row, ", "row your boat,",
            "Gently down ", "the stream. ",
            "Merrily, merrily, merrily, merrily, ",
            "Life ", "is ", "but a dream"
        );
        MockRenderLine render = new MockRenderLine(10, 20);
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        System.out.println(test);
    }
}
