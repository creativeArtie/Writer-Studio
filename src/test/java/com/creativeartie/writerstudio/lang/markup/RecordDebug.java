package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.*;
import java.io.*;
import java.time.*;

import com.google.common.io.*;

@RunWith(Parameterized.class)
public class RecordDebug {

    @Parameter
    public int index;
    @Parameter(value = 1)
    public LocalDate expectDate;
    @Parameter(value = 2)
    public int expectPublish;
    @Parameter(value = 3)
    public int expectNote;
    @Parameter (value = 4)
    public Duration expectDuration;

    @Parameters
    public static Collection<Object[]> data() throws Exception{
        ArrayList<Object[]> data = new ArrayList<>();
        int day = 12;
        int publish = 100;
        int note = 20;
        for (int i = 0; i <= 6; i++){
            data.add(new Object[]{i, LocalDate.ofYearDay(2017, day), publish,
                note, i % 2 == 0? Duration.ofMinutes(30): Duration.ofMinutes(25)
                });
            publish += 100;
            note += 20;
            day++;
        }
        data.add(new Object[]{7, LocalDate.now(), publish - 100, note - 20, null});
        return data;
    }

    private RecordList records;
    private int expectedTotal;

    @Before
    public void before() throws Exception{
        try (FileReader reader = new FileReader(new File("build/resources/test/record1.txt"))){
            records = new RecordList(CharStreams.toString(reader));
        }
        expectedTotal = expectPublish + expectNote;
    }

    @Test
    public void testRead(){
        testCommon(records.get(index));
    }

    @Test
    public void testReload() throws Exception{
        String saved = records.getSaveText();

        RecordList reload = new RecordList(saved);
        testCommon(reload.get(index));
    }

    private void testCommon(Record test){
        assertEquals("Wrong date.", expectDate, test.getRecordDate());
        assertEquals("Wrong publish count.", expectPublish, test
            .getPublishTotal());
        assertEquals("Wrong note count.", expectNote, test.getNoteTotal());
        assertEquals("Wrong totalCount.", expectedTotal, test.getTotalCount());
        if (expectDuration != null){
            assertEquals("Wrong duration.", expectDuration, test.getWriteTime());
            assertEquals("Wrong publish written.", 100, test.getPublishWritten());
        } else {
            /// System.out.println(test.getWriteTime());
            assertEquals("Wrong publish written.", 0, test.getPublishWritten());
        }
    }
}
