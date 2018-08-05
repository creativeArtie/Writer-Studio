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

    @Test
    public void fillAll(){
        MockContentLine content = new MockContentLine("a", "b c");
        MockRenderLine render = new MockRenderLine();
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(1, test.size(), "lines size");
        assertEquals(2, test.get(0).size(), "line 1 size");
        assertData("a",   10, test, 0, 0);
        assertData("b c", 10, test, 0, 1);
    }

    @ParameterizedTest()
    @ValueSource(strings = {"123 ;overflow", "123 overflow"})
    public void appendToNewline(String text){
        MockContentLine content = new MockContentLine(text.split(";"));
        MockRenderLine render = new MockRenderLine();
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(2, test.size(), "lines size");
        assertEquals(1, test.get(0).size(), "line 1 size");
        assertEquals(1, test.get(1).size(), "line 2 size");
        assertData("123 ",    10, test, 0, 0);
        assertData("overflow", 10, test, 1, 0);
    }
}
