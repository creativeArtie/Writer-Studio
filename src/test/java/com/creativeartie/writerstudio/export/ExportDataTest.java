package com.creativeartie.writerstudio.export;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;


import java.util.*;

import com.creativeartie.writerstudio.export.mock.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExportDataTest{

    private ExportData<Integer> build(String text, DataLineType type){
        return new ExportData<>(new MockContentData(text), type,
            new MockRenderData());
    }

    @ParameterizedTest(name = "{1} -> {0}")
    /// ---------------------------0123456789012
    @CsvSource({"Hello World,40", "Hello World!,12"})
    public void assertData(String text, int split){
        ExportData<Integer> test = build(text, DataLineType.LEFT);
        Optional<ExportData<Integer>> overflow = test.split(split);
        assertAll(
            () -> assertFalse(overflow.isPresent(),
                () -> "overflow: " + overflow.get().getCurrentText()),
            () -> assertEquals(text, test.getCurrentText(), "text"),
            () -> assertEquals(text.length(), test.getFillWidth().intValue(),
                "width"),
            () -> assertEquals(10, test.getFillHeight().intValue(), "height")
        );
    }

    @ParameterizedTest(name = "{2} -> {0}{1}")
    @CsvSource({
        "'',Hello,1",
    /// ----01234
        "'',Hello,4",
    /// --01234567890123
        "'Twinkle, ','twinkle, little star',13",
        "'', 'How I wonder what you are!',2",
    /// --01234567890
        "'Up above ','the world so high,',10",
    /// --0123456789012345678901
        "'Like a damond in the ','sky',22"
    })
    public void assertData(String first, String second, int split){
        String full = first + second;
        ExportData<Integer> test = build(full, DataLineType.LEFT);

        Optional<ExportData<Integer>> overflow = test.split(split);
        assertAll(
            () -> assertTrue(overflow.isPresent(), "overflow"),
            () -> assertEquals(first, test.getCurrentText(), "text 1"),
            () -> assertEquals(first.length(), test.getFillWidth().intValue(),
                "width 1"),
            () -> assertEquals(10, test.getFillHeight().intValue(),
                "height 1")
        );
        ExportData<Integer> test2 = overflow.get();
        assertAll(
            () -> assertEquals(second, test2.getCurrentText(), "text 2"),
            () -> assertEquals(second.length(), test2.getFillWidth().intValue(),
                "width 2"),
            () -> assertEquals(10, test2.getFillHeight().intValue(),
                "height 2")
        );

    }
}
