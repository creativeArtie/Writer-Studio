package com.creativeartie.jwriter.file;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.*;
import java.io.*;
import java.time.*;

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

    private RecordTable records;
    private int expectedTotal;

    @Before
    public void before() throws Exception{
        records = new RecordTable(new File("data/record1.txt"));
        expectedTotal = expectPublish + expectNote;
    }

    @Test
    public void testRead(){
        testCommon(records.get(index));
    }

    @Test
    public void testReload() throws Exception{
        File tmp = File.createTempFile("recordTmp", ".txt");
        records.saveRecords(tmp);

        try {
            RecordTable reload = new RecordTable(tmp);
            testCommon(reload.get(index));
        } catch(Exception ex){
            System.out.println(tmp);
            throw ex;
        }
        tmp.delete();
    }

    private void testCommon(Record test){
        assertEquals("Wrong date.", expectDate, test.getRecordDate());
        assertEquals("Wrong publish count.", expectPublish, test
            .getPublishCount());
        assertEquals("Wrong note count.", expectNote, test.getNoteCount());
        assertEquals("Wrong totalCount.", expectedTotal, test.getTotalCount());
        if (expectDuration != null){
            assertEquals("Wrong duration.", expectDuration, test.getWriteDuration());
            assertEquals("Wrong publish written.", 100, test.getPublishWritten());
        } else {
            /// System.out.println(test.getWriteDuration());
            assertEquals("Wrong publish written.", 0, test.getPublishWritten());
        }
    }
}