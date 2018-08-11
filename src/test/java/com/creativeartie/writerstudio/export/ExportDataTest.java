package com.creativeartie.writerstudio.export;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.time.*;
import java.util.*;

import com.creativeartie.writerstudio.export.mock.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExportDataTest{

    private ExportData<Integer> build(String text, DataLineType type){
        return new ExportData<>(new MockContentData(text), type,
            new MockRenderData());
    }

    @ParameterizedTest(name = "{0} -> \"{1}\"")
    @CsvSource({
        "0,'','',''",
        "0,'Hello','','Hello'",
        "1,'Hello','Hello',''",
        "0,'Hello World! One Two Three','','Hello World! One Two Three'",
        "1,'Hello World! One Two Three','Hello ','World! One Two Three'",
        "2,'Hello World! One Two Three','Hello World! ','One Two Three'",
        "3,'Hello World! One Two Three','Hello World! One ','Two Three'",
        "4,'Hello World! One Two Three','Hello World! One Two ','Three'",
        "5,'Hello World! One Two Three','Hello World! One Two Three',''",
        "6,'Hello World! One Two Three','Hello World! One Two Three',''",
    })
    public void spaceSplitLine(int at, String text, String first, String second){
        String[] test = new MockRenderData().splitLine(text, at);
        assertNotNull(test);
        assertAll(
            () -> assertEquals(first, test[0], "first"),
            () -> assertEquals(second, test[1], "second")
        );
    }

    @ParameterizedTest(name = "{1} -> {0}")
    /// ---------------------------0123456789012
    @CsvSource({"Hello World,40", "Hello World!,12", "'Hello',213","'',213"})
    public void fitAllData(String text, int width){
        ExportData<Integer> test = build(text, DataLineType.LEFT);
        Optional<ExportData<Integer>> overflow =
            assertTimeout(Duration.ofSeconds(5), () -> test.split(width));
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
    ///
        "'', 'Hello',3",
    /// --0123
        "'','Hello World',3",
    /// --012345   6789
        "'Hello ','World',9",
    /// --012345   67
        "'Hello ','World',6",
    /// --01234567890123
        "'Twinkle, ','twinkle, little star',13",
        "'', 'How I wonder what you are!',2",
    /// --01234567890
        "'Up above ','the world so high,',10",
    /// --0123456789012345678901
        "'Like a damond in the ','sky',22",
    /// --0123456789   0
        "'When this ','blazing sun is gone,',10", /// cut after space
    /// --01234   56
        "'When ','he nothing shines upon,',6", /// cut before space
    /// --012345678
        "'Then you ','show your little light',10", /// cut at space
    /// --012345678   9
        "'Twinkle, ','twinkle, through the night',9", /// cut after space
    })
    public void fitPartData(String first, String second, int split){
        String full = first + second;
        ExportData<Integer> test = build(full, DataLineType.LEFT);

        Optional<ExportData<Integer>> overflow =
            assertTimeout(Duration.ofSeconds(5), () -> test.split(split));
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

    @ParameterizedTest(name = "{0}{1}{2}")
    @CsvSource({
        "'','Then the traveller in the dark','',2,300",
    /// --0123456
        "'Thanks ','you for your tiny tiny spark','',7,400",
    /// -----012345678   9
        "'','He could ','not see where to go',1,9",
    /// --0123456   012345678
        "'If you ','did not ','twinkle so.',7,10",
    /// --012345 0123456
        "'And ','often ','through my curtain peeks',8,8",
    /// --01234567   012
        "'For you ','','never shut your eye',10,2",
    /// --012345678   01234567890123   456
        "'Till the ','sun is in the ','sky.',9,17"
    })
    public void overflowTwo(String first, String second, String third,
        int line, int spaces
    ){
        String full = first + second + third;

        ExportData<Integer> test = build(full, DataLineType.LEFT);
        Optional<ExportData<Integer>> overflow = test.split(line);

        assertEquals(first, test.getCurrentText(), "first overflow");

        assertTrue(overflow.isPresent());
        assertEquals(second + third, overflow.get().getCurrentText(),
            "first overflow - unsplit");

        Optional<ExportData<Integer>> more = overflow.get().split(spaces);
        assertEquals(second, overflow.get().getCurrentText(),
            "first overflow - split");
        if (third.isEmpty()){
            assertFalse(more.isPresent(), "more overflow: " + more);
        } else {
            assertTrue(more.isPresent(), "more overflow");
            assertEquals(third, more.get().getCurrentText(), "more overflow");
        }
    }

    @ParameterizedTest
    @CsvSource({
    /// --000000000011111111112222222222
    /// --012345678901234567890123456789
        "'As your bright and tiny spark','',35,5",
    /// --00000000001111111111222222222233333
    /// --01234567890123456789012345678901234
        "'Lights the traveller in the dark','',34,2",
    /// --00000000001111111111222---222222233333
    /// --01234567890123456789012---345678901234
        "'Though I know not what ','you are,',32,7",
    /// --0000000000111111111122222--2222233333
    /// --0123456789012345678901234--5678901234
        "'Twinkle, twinkle, little ','star',30,4",
    ///
        "'','Hello',6,6"
    })
    public void extraSpaces(String first, String second, int size, int spaces){
        String full = first + second;

        ExportData<Integer> test = build(full, DataLineType.LEFT);
        test.setKeepNext(spaces);
        Optional<ExportData<Integer>> overflow =
            assertTimeout(Duration.ofSeconds(5), () -> test.split(size));
        assertAll(
            () -> assertEquals(second.length() > 0, overflow.isPresent(),
                "overflow"),
            () -> assertEquals(first, test.getCurrentText(), "text 1"),
            () -> assertEquals(first.length(), test.getFillWidth().intValue(),
                "width 1"),
            () -> assertEquals(10, test.getFillHeight().intValue(),
                "height 1")
        );
        if (! overflow.isPresent()) return;
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
