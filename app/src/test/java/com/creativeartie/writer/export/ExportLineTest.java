package com.creativeartie.writer.export;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

import com.creativeartie.writer.export.mock.*;

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
        MockRenderLine render = new MockRenderLine(5, 10);
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(1, test.size(), "lines size");
        assertEquals(2, test.get(0).size(), "line 1 size");
        assertData("a",   10, test, 0, 0);
        assertData("b c", 10, test, 0, 1);
    }

    @ParameterizedTest
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

    @Test
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
    public void longWord(){
        String full = "See Pneumonultramicrospicsilicovolcanconiosis Hey you";
        MockContentLine content = new MockContentLine(full);
        MockRenderLine render = new MockRenderLine();
        ExportLineMain<Integer> test = new ExportLineMain<>(content, render);
        test.render();
        assertEquals(3, test.size(), "line size");
        for (int i = 0 ;i < 3; i++){
            assertEquals(1, test.get(i).size(), "line "+ i + " size");
        }
        assertData("See ", 10, test, 0, 0);
        assertData("Pneumonultramicrospicsilicovolcanconiosis ", 10, test, 1, 0);
        assertData("Hey you", 10, test, 2, 0);
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
        assertEquals(6, test.size(), "line size");
        assertEquals(2, test.get(0).size(), "line 1 size");
        assertEquals(2, test.get(1).size(), "line 2 size");
        assertEquals(2, test.get(2).size(), "line 3 size");
        assertEquals(1, test.get(3).size(), "line 4 size");
        assertEquals(1, test.get(4).size(), "line 5 size");
        assertEquals(3, test.get(5).size(), "line 6 size");

        assertData("Row, ",              10, test, 0, 0);
        assertData("row, ",              10, test, 0, 1);
        assertData("row your boat,",     10, test, 1, 0);
        assertData("Gently ",            10, test, 1, 1);
        assertData("down ",              10, test, 2, 0);
        assertData("the stream. ",       10, test, 2, 1);
        assertData("Merrily, merrily, ", 10, test, 3, 0);
        assertData("merrily, merrily, ", 10, test, 4, 0);
        assertData("Life ",              10, test, 5, 0);
        assertData("is ",                10, test, 5, 1);
        assertData("but a dream",        10, test, 5, 2);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 2, 3, 4, 5})
    public void splitAt(int start){
        MockContentLine content = new MockContentLine(
            "The wheels on the bus go round and round, ",
            "round and round, ", "round and round. ",
            "The wheels on the bus go round and round, ",
            "all day long "
        );
        MockRenderLine render = new MockRenderLine(20, 25);
        ExportLineMain<Integer> base = new ExportLineMain<>(content, render);
        base.render();
        ExportLineMain<Integer> split = start == -1? null : base.splitAt(start);

        assertEquals(start == -1? 6: start, base.size(), "line size old");
        if (split != null) assertEquals(6 - start, split.size(), "line size new");
        ExportLineMain<Integer> test = start == 0? split: base;
        int line = 0;
        int i = 0;

        assertEquals(1, test.get(line).size(), "line 1 size: " + line);
        assertData("The wheels on the ",       10, test, line, 0);
        line++;
        i++;
        if (start == i){
            line = 0;
            test = split;
        }

        assertEquals(1, test.get(line).size(), "line 2 size: " + line);
        assertData("bus go round and round, ", 10, test, line, 0);
        line++;
        i++;
        if (start == i){
            line = 0;
            test = split;
        }

        assertEquals(2, test.get(line).size(), "line 3 size: " + line);
        assertData("round and round, ",        10, test, line, 0);
        assertData("round ",                   10, test, line, 1);
        line++;
        i++;
        if (start == i){
            line = 0;
            test = split;
        }

        assertEquals(2, test.get(line).size(), "line 4 size: " + line);
        assertData("and round. ",              10, test, line, 0);
        assertData("The wheels on ",           10, test, line, 1);
        line++;
        i++;
        if (start == i){
            line = 0;
            test = split;
        }

        assertEquals(1, test.get(line).size(), "line 5 size: " + line);
        assertData("the bus go round and ",    10, test, line, 0);
        line++;
        i++;
        if (start == i){
            line = 0;
            test = split;
        }

        assertEquals(2, test.get(line).size(), "line 6 size: " + line);
        assertData("round, ",                  10, test, line, 0);
        assertData("all day long ",            10, test, line, 1);
    }


    static Stream<Arguments> keepLast(){
        /// -----0000000000111111 1111
        /// -----0123456789012345 6789
        /// For: Simple text line 1234
        return Stream.of(
            Arguments.of( /// Well past
                22, new String[][]{{"Simple text line", "1234"}}
            ), Arguments.of( /// Just past
                20, new String[][]{{"Simple text line", "1234"}}
            ), Arguments.of( /// Just overflow
                19, new String[][]{{"Simple text "}, {"line", "1234"}}
            ), Arguments.of( /// Overflow
                17, new String[][]{{"Simple text "}, {"line", "1234"}}
            ), Arguments.of( /// Well Overflow
                14, new String[][]{{"Simple text "}, {"line", "1234"}}
            )
        );
    }

    @ParameterizedTest
    @MethodSource
    public void keepLast(int size, String[][] expects){
        MockContentLine line = new MockContentLine();
        line.add(new MockContentData("Simple text line"));
        line.add(new MockContentData("1234").setKeepLast(true));
        MockRenderLine render = new MockRenderLine(size, size);
        ExportLineMain<Integer> test = new ExportLineMain<>(line, render);
        test.render();
        assertEquals(expects.length, test.size());
        int i = 0;
        for (String[] expect: expects){
            assertEquals(expect.length, test.get(i).size());
            int j = 0;
            for (String text: expect){
                assertData(text, 10, test, i, j);
                j++;
            }
            i++;
        }
    }

    @Test
    @Disabled("Currently, MockContent.keepLast is only for footnote; not even know what to do")
    public void longFootnote(){
        MockContentLine line = new MockContentLine();
        line.add(new MockContentData("No purchase neccessary."));
        /// --------------------------000000000011111111112
        /// --------------------------012345678901234567890
        line.add(new MockContentData("After first purchase").setKeepLast(true));

        MockRenderLine render = new MockRenderLine(20,25);
        ExportLineMain<Integer> test = new ExportLineMain<>(line, render);
        test.render();
        System.out.println(test);
    }

    @RepeatedTest(4)
    public void lineHeight(RepetitionInfo info){
        int diff = info.getCurrentRepetition() - 1;
        MockContentLine line = new MockContentLine();
        String[] inputs = new String[]{"Hello ", "Great ", "Big ", "World!"};
        int i = 0;
        for (String input: inputs){
            MockContentData data = new MockContentData(input);
            if (i == diff){
                data.getFormats().add(DataContentType.BOLD);
            }
            line.add(data);
            i++;
        }
        MockRenderLine render = new MockRenderLine(1000,1000);
        ExportLineMain<Integer> test = new ExportLineMain<>(line, render);
        test.render();
        assertEquals(30, test.getFillHeight().intValue());
    }
}
